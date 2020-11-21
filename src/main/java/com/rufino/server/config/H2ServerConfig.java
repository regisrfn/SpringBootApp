package com.rufino.server.config;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class H2ServerConfig {
    @Bean(initMethod = "start", destroyMethod = "stop")
	public Server server() throws SQLException {
		return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "5050");
	}
    
}
