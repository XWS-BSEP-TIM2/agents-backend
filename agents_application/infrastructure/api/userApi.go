package api

import (
	"context"
	"github.com/XWS-BSEP-TIM2/agents-backend/agents_application/application"
	"github.com/XWS-BSEP-TIM2/agents-backend/agents_application/domain"
	pb "github.com/XWS-BSEP-TIM2/agents-backend/agents_application/proto/agents"
)

type UserHandler struct {
	pb.UnimplementedAgentsProtoServiceServer
	service *application.UserService
}

func NewUserHandler(service *application.UserService) *UserHandler {
	return &UserHandler{service: service}
}

func (handler *UserHandler) GetAll(ctx context.Context, request *pb.EmptyRequest) (*pb.GetAllUsersResponse, error) {
	usersFromDb, err := handler.service.GetAll(ctx)
	if err != nil {
		return nil, err
	}
	response := &pb.GetAllUsersResponse{
		Users: []*pb.User{},
	}
	for _, user := range usersFromDb {
		current := mapUser(user)
		response.Users = append(response.Users, current)
	}
	return response, nil
}

func mapUser(profile *domain.User) *pb.User {
	profilePb := &pb.User{
		Id:       profile.Id.Hex(),
		Name:     profile.Name,
		Surname:  profile.Surname,
		Username: profile.Username,
		Email:    profile.Email,
	}

	return profilePb
}
