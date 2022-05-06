package application

import (
	"context"
	"github.com/XWS-BSEP-TIM2/agents-backend/agents_application/domain"
	"github.com/XWS-BSEP-TIM2/agents-backend/agents_application/infrastructure/persistence"
	"go.mongodb.org/mongo-driver/bson/primitive"
)

// Specification of the service and it's methods (STORE <-> SERVICE)

type UserService struct {
	store persistence.UserStore
}

func NewUserService(store persistence.UserStore) *UserService {
	return &UserService{store: store}
}

func (service *UserService) Get(ctx context.Context, id primitive.ObjectID) (*domain.User, error) {
	return service.store.Get(ctx, id)
}

func (service *UserService) Insert(ctx context.Context, profile *domain.User) {
	service.store.Insert(ctx, profile)
}

func (service *UserService) Update(ctx context.Context, profile *domain.User) {
	service.store.Update(ctx, profile)
}
