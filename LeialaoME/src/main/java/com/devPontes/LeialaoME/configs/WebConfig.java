package com.devPontes.LeialaoME.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {  // Interface WebMvcConfigurer configura content-negotion e cors

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.ignoreAcceptHeader(false)
				  .useRegisteredExtensionsOnly(true)
				  .mediaType("pdf", MediaType.APPLICATION_PDF)
				  .mediaType("json", MediaType.APPLICATION_JSON)
				  .mediaType("xml", MediaType.APPLICATION_XML)
				  .mediaType("html", MediaType.TEXT_HTML)
				  .defaultContentType(MediaType.APPLICATION_JSON);
				  
	}

	

	
	
	

}
