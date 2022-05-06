package config

type Config struct {
	GrpcPort     string
	HttpPort     string
	AgentsDBHost string
	AgentsDBPort string
}

func NewConfig() *Config {
	return &Config{
		GrpcPort:     "8031",
		HttpPort:     "8030",
		AgentsDBHost: "localhost",
		AgentsDBPort: "27017",
	}
}
