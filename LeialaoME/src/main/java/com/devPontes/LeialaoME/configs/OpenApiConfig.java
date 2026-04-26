package com.devPontes.LeialaoME.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {
	
	@Bean
	public OpenAPI customOpenApiConfig() {
		return new OpenAPI()
						.info(new Info()
						.title("LeilaoME - Plataforma de Leilao Online!")
						.version("v1"));
	}
	
}
