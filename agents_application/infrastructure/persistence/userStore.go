package persistence

import (
	"context"
	"github.com/XWS-BSEP-TIM2/agents-backend/agents_application/domain"
	"go.mongodb.org/mongo-driver/bson/primitive"
)

// Interface for the service (store)

type UserStore interface {
	Get(ctx context.Context, id primitive.ObjectID) (*domain.User, error)
	GetAll(ctx context.Context) ([]*domain.User, error)
	Insert(ctx context.Context, profile *domain.User) (error, string)
	Update(ctx context.Context, profile *domain.User) error
	GetByUsername(ctx context.Context, Username string) (*domain.User, error)
	DeleteAll(ctx context.Context)
}
