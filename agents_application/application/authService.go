package application

import (
	"context"
	"github.com/XWS-BSEP-TIM2/agents-backend/agents_application/domain"
	"github.com/XWS-BSEP-TIM2/agents-backend/agents_application/infrastructure/persistence"
	"go.mongodb.org/mongo-driver/bson/primitive"
)

// Specification of the service and it's methods (STORE <-> SERVICE)

type AuthService struct {
	store                 persistence.UserStore
	profileServiceAddress string
}

func NewAuthService(store persistence.UserStore, profileServiceAddress string) *AuthService {
	return &AuthService{
		store:                 store,
		profileServiceAddress: profileServiceAddress,
	}
}

func (service *AuthService) Create(ctx context.Context, user *domain.User) (string, error) {
	err, id := service.store.Insert(ctx, user)
	if err != nil {
		return "", err
	}
	return id, nil
}

func (service *AuthService) Get(ctx context.Context, id primitive.ObjectID) (*domain.User, error) {
	return service.store.Get(ctx, id)
}

func (service *AuthService) GetAll(ctx context.Context) ([]*domain.User, error) {
	return service.store.GetAll(ctx)
}

func (service *AuthService) GetByUsername(ctx context.Context, username string) (*domain.User, error) {
	return service.store.GetByUsername(ctx, username)
}
