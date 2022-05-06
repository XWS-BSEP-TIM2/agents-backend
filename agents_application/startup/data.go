package startup

import (
	"github.com/XWS-BSEP-TIM2/agents-backend/agents_application/domain"
	"github.com/XWS-BSEP-TIM2/agents-backend/agents_application/domain/enums"
	"go.mongodb.org/mongo-driver/bson/primitive"
)

var users = []*domain.User{
	{
		Id:       getObjectId("ID000001"),
		Username: "srdjan",
		Password: "pass",
		Role:     enums.USER,
	},
	{
		Id:       getObjectId("ID000002"),
		Username: "nikola93",
		Password: "pass",
		Role:     enums.COMPANY_OWNER,
	},
}

func getObjectId(id string) primitive.ObjectID {
	if objectId, err := primitive.ObjectIDFromHex(id); err == nil {
		return objectId
	}
	return primitive.NewObjectID()
}
