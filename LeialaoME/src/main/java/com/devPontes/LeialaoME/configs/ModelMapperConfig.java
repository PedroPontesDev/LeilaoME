package com.devPontes.LeialaoME.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class ModelMapperConfig {

	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}
	
}
