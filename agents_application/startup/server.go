package startup

import (
	"context"
	"fmt"
	"github.com/XWS-BSEP-TIM2/agents-backend/agents_application/application"
	"github.com/XWS-BSEP-TIM2/agents-backend/agents_application/infrastructure/api"
	"github.com/XWS-BSEP-TIM2/agents-backend/agents_application/infrastructure/persistence"
	pb "github.com/XWS-BSEP-TIM2/agents-backend/agents_application/proto/agents"
	"github.com/XWS-BSEP-TIM2/agents-backend/agents_application/startup/config"
	grpc_middleware "github.com/grpc-ecosystem/go-grpc-middleware"
	"go.mongodb.org/mongo-driver/mongo"
	"google.golang.org/grpc"
	"log"
	"net"
)

type Server struct {
	config *config.Config
}

func NewServer(config *config.Config) *Server {
	return &Server{
		config: config,
	}
}

func (server *Server) Start() {
	mongoClient := server.initMongoClient()

	userStore := server.initUserStore(mongoClient)

	userService := server.initUserService(userStore)

	userHandler := server.initUserHandler(userService)

	server.startGrpcServer(userHandler)
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
	listener, err := net.Listen("tcp", fmt.Sprintf(":%s", server.config.Port))
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}
	grpcServer := grpc.NewServer(
		grpc.UnaryInterceptor(
			grpc_middleware.ChainUnaryServer(
			// interceptors.TokenAuthInterceptor,
			),
		),
	)
	pb.RegisterAgentsProtoServiceServer(grpcServer, userHandler)
	if err := grpcServer.Serve(listener); err != nil {
		log.Fatalf("failed to serve: %s", err)
	}
}
