package com.devPontes.LeialaoME.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/**")
				.allowCredentials(true)
				.allowedOrigins("http://192.168.1.2:5173", "http://localhost:3000", "http://localhost:5173")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.allowedHeaders("*");;
	}

	
	
	

}
