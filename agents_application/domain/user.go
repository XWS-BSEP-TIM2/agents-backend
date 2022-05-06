package domain

import (
	"github.com/XWS-BSEP-TIM2/agents-backend/agents_application/domain/enums"
	"go.mongodb.org/mongo-driver/bson/primitive"
)

// Specification of the model

type User struct {
	Id       primitive.ObjectID `bson:"_id,omitempty"`
	Username string             `bson:"username"`
	Password string             `bson:"password"`
	Role     enums.Role         `bson:"role"`
	Name     string             `bson:"name"`
	Surname  string             `bson:"surname"`
	Email    string             `bson:"email"`
}

func NewUser(id primitive.ObjectID, username string, password string, role enums.Role, name string, surname string, email string) *User {
	return &User{Id: id, Username: username, Password: password, Role: role, Name: name, Surname: surname, Email: email}
}
