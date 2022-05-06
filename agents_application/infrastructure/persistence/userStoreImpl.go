package persistence

import (
	"context"
	"github.com/XWS-BSEP-TIM2/agents-backend/agents_application/domain"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"go.mongodb.org/mongo-driver/mongo"
)

const (
	DATABASE   = "agents_users_db"
	COLLECTION = "users"
)

type UserMongoDbStore struct {
	users *mongo.Collection
}

func NewUserMongoDbStore(client *mongo.Client) UserStore {
	users := client.Database(DATABASE).Collection(COLLECTION)
	return &UserMongoDbStore{
		users: users,
	}
}

func (store UserMongoDbStore) Get(ctx context.Context, id primitive.ObjectID) (*domain.User, error) {
	//TODO implement me
	return nil, nil
}

func (store UserMongoDbStore) GetAll(ctx context.Context) ([]*domain.User, error) {
	//TODO implement me

	return nil, nil
}

func (store UserMongoDbStore) Insert(ctx context.Context, user *domain.User) (error, string) {
	_, err := store.users.InsertOne(context.TODO(), user)
	if err != nil {
		return err, ""
	}
	return nil, "User added"
}

func (store UserMongoDbStore) Update(ctx context.Context, profile *domain.User) error {
	//TODO implement me

	return nil
}

func (store UserMongoDbStore) GetByUsername(ctx context.Context, Username string) (*domain.User, error) {
	//TODO implement me

	return nil, nil
}

func (store UserMongoDbStore) DeleteAll(ctx context.Context) {
	//TODO implement me
}
