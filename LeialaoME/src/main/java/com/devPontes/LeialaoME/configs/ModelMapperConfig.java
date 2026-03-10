package com.devPontes.LeialaoME.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@Configuration
@Component
public class ModelMapperConfig {

	@Bean
	public ModelMapper getModelMapper() {
		ModelMapper mapper = new ModelMapper();
		return mapper;

	}
	
	
	
}
