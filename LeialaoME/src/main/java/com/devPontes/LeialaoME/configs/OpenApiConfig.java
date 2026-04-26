package com.devPontes.LeialaoME.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class OpenApiConfig {
	
	@Bean
	public OpenAPI openApiConfig() {
		return new OpenAPI();
	}
	
}
