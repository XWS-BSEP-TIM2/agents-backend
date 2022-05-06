package startup

import (
	"context"
	"github.com/XWS-BSEP-TIM2/agents-backend/agents_application/application"
	"github.com/XWS-BSEP-TIM2/agents-backend/agents_application/infrastructure/api"
	"github.com/XWS-BSEP-TIM2/agents-backend/agents_application/infrastructure/persistence"
	pb "github.com/XWS-BSEP-TIM2/agents-backend/agents_application/proto/agents"
	"github.com/XWS-BSEP-TIM2/agents-backend/agents_application/startup/config"
	"github.com/XWS-BSEP-TIM2/agents-backend/agents_application/tracer"
	grpc_opentracing "github.com/grpc-ecosystem/go-grpc-middleware/tracing/opentracing"
	"github.com/grpc-ecosystem/grpc-gateway/v2/runtime"
	otgo "github.com/opentracing/opentracing-go"
	"github.com/opentracing/opentracing-go/ext"
	"go.mongodb.org/mongo-driver/mongo"
	"google.golang.org/grpc"
	"io"
	"log"
	"net"
	"net/http"
)

var grpcGatewayTag = otgo.Tag{Key: string(ext.Component), Value: "grpc-gateway"}

func tracingWrapper(h http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		parentSpanContext, err := otgo.GlobalTracer().Extract(
			otgo.HTTPHeaders,
			otgo.HTTPHeadersCarrier(r.Header))
		if err == nil || err == otgo.ErrSpanContextNotFound {
			serverSpan := otgo.GlobalTracer().StartSpan(
				"ServeHTTP",
				// this is magical, it attaches the new span to the parent parentSpanContext, and creates an unparented one if empty.
				ext.RPCServerOption(parentSpanContext),
				grpcGatewayTag,
			)
			r = r.WithContext(otgo.ContextWithSpan(r.Context(), serverSpan))
			defer serverSpan.Finish()
		}
		h.ServeHTTP(w, r)
	})
}

type Server struct {
	config *config.Config
	pb.UnimplementedAgentsProtoServiceServer
	tracer otgo.Tracer
	closer io.Closer
}

const name = "agents_service"

func NewServer(config *config.Config) *Server {

	tracer, closer := tracer.Init(name)
	otgo.SetGlobalTracer(tracer)

	return &Server{
		config: config,
		tracer: tracer,
		closer: closer,
	}
}

func (server *Server) Start() {
	mongoClient := server.initMongoClient()
	userStore := server.initUserStore(mongoClient)
	userService := server.initUserService(userStore)

	userHandler := server.initUserHandler(userService)

	server.startGrpcServer(userHandler)
}

func (server *Server) GetTracer() otgo.Tracer {
	return server.tracer
}

func (server *Server) GetCloser() io.Closer {
	return server.closer
}

func (server *Server) initMongoClient() *mongo.Client {
	client, err := persistence.GetClient(server.config.AgentsDBHost, server.config.AgentsDBPort)
	if err != nil {
		log.Fatal(err)
	}
	return client
}

func (server *Server) initUserStore(client *mongo.Client) persistence.UserStore {
	store := persistence.NewUserMongoDbStore(client)
	store.DeleteAll(context.TODO())
	for _, user := range users {
		err, _ := store.Insert(context.TODO(), user)
		if err != nil {
			log.Fatal(err)
		}
	}
	return store
}

func (server *Server) initUserService(store persistence.UserStore) *application.UserService {
	return application.NewUserService(store)
}

func (server *Server) initUserHandler(service *application.UserService) *api.UserHandler {
	return api.NewUserHandler(service)
}

func (server *Server) startGrpcServer(userHandler *api.UserHandler) {
	// Create a listener on TCP port
	lis, err := net.Listen("tcp", ":8031")
	if err != nil {
		log.Fatalln("Failed to listen:", err)
	}

	// Create a gRPC server object
	grpcServer := grpc.NewServer()
	pb.RegisterAgentsProtoServiceServer(grpcServer, userHandler)

	// Serve gRPC server
	log.Println("Serving gRPC on 0.0.0.0:8031")
	go func() {
		log.Fatalln(grpcServer.Serve(lis))
	}()

	// Create a client connection to the gRPC server we just started
	// This is where the gRPC-Gateway proxies the requests
	conn, err := grpc.DialContext(
		context.Background(),
		"0.0.0.0:8031",
		grpc.WithBlock(),
		grpc.WithInsecure(),
		grpc.WithUnaryInterceptor(
			grpc_opentracing.UnaryClientInterceptor(
				grpc_opentracing.WithTracer(otgo.GlobalTracer()),
			),
		),
		grpc.WithStreamInterceptor(
			grpc_opentracing.StreamClientInterceptor(
				grpc_opentracing.WithTracer(otgo.GlobalTracer()),
			),
		),
	)
	if err != nil {
		log.Fatalln("Failed to dial server:", err)
	}

	gwmux := runtime.NewServeMux()
	err = pb.RegisterAgentsProtoServiceHandler(context.Background(), gwmux, conn)
	if err != nil {
		log.Fatalln("Failed to register gateway:", err)
	}

	gwServer := &http.Server{
		Addr:    ":8030",
		Handler: tracingWrapper(gwmux),
	}

	log.Println("Serving gRPC-Gateway on http://0.0.0.0:8030")
	log.Fatalln(gwServer.ListenAndServe())
}
