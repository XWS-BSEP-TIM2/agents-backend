package main

import (
	"github.com/XWS-BSEP-TIM2/agents-backend/agents_application/startup"
	"github.com/XWS-BSEP-TIM2/agents-backend/agents_application/startup/config"
)

func main() {
	config := config.NewConfig()
	server := startup.NewServer(config)
	server.Start()
}
