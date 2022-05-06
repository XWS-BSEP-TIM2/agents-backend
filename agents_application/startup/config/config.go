package config

type Config struct {
	Port         string
	AgentsDBHost string
	AgentsDBPort string
}

func NewConfig() *Config {
	return &Config{
		Port:         "8020",      // os.Getenv("POST_SERVICE_PORT"),
		AgentsDBHost: "localhost", // os.Getenv("CATALOGUE_DB_HOST"),
		AgentsDBPort: "27017",     // os.Getenv("CATALOGUE_DB_PORT"),
	}
}
