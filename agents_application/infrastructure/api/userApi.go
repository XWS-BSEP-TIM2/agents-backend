package api

import (
	"github.com/XWS-BSEP-TIM2/agents-backend/agents_application/application"
	pb "github.com/XWS-BSEP-TIM2/agents-backend/agents_application/proto/agents"
)

type UserHandler struct {
	pb.UnimplementedAgentsProtoServiceServer
	service *application.UserService
}

func NewUserHandler(service *application.UserService) *UserHandler {
	return &UserHandler{service: service}
}
