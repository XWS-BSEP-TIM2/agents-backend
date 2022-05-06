package persistence

import (
	"context"
	"github.com/XWS-BSEP-TIM2/agents-backend/agents_application/domain"
	"go.mongodb.org/mongo-driver/bson"
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

func (store *UserMongoDbStore) filterOne(filter interface{}) (profile *domain.User, err error) {
	result := store.users.FindOne(context.TODO(), filter)
	err = result.Decode(&profile)
	return
}

func (store *UserMongoDbStore) filter(filter interface{}) ([]*domain.User, error) {
	cursor, err := store.users.Find(context.TODO(), filter)
	defer cursor.Close(context.TODO())

	if err != nil {
		return nil, err
	}
	return decode(cursor)
}

func decode(cursor *mongo.Cursor) (profiles []*domain.User, err error) {
	for cursor.Next(context.TODO()) {
		var profile domain.User
		err = cursor.Decode(&profile)
		if err != nil {
			return
		}
		profiles = append(profiles, &profile)
	}
	err = cursor.Err()
	return
}

func (store UserMongoDbStore) Get(ctx context.Context, id primitive.ObjectID) (*domain.User, error) {
	//TODO implement me
	return nil, nil
}

func (store UserMongoDbStore) GetAll(ctx context.Context) ([]*domain.User, error) {
	filter := bson.D{{}}
	return store.filter(filter)
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
