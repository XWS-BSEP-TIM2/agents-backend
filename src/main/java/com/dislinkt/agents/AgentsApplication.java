package com.dislinkt.agents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableAsync(proxyTargetClass = true)
public class AgentsApplication {
	public static void main(String[] args) {
		SpringApplication.run(AgentsApplication.class, args);
	}
}
