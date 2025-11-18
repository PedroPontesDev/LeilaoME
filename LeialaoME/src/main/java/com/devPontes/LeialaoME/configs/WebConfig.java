package com.devPontes.LeialaoME.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		   registry.addMapping("/**")
           .allowedOrigins("http://localhost:5173", "http://localhost:8080")
           .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
           .allowedHeaders("*")
           .allowCredentials(true);
   }

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.ignoreAcceptHeader(false)
				  .favorParameter(false)
				  .mediaType("json", MediaType.APPLICATION_JSON)
				  .mediaType("xml", MediaType.APPLICATION_XML)
				  .defaultContentType(MediaType.APPLICATION_JSON);
				  
	}
	
	

	
	
	

}
