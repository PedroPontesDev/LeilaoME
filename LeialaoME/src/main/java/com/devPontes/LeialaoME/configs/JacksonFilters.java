package com.devPontes.LeialaoME.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@Configuration
public class JacksonFilters {

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		FilterProvider filters = new SimpleFilterProvider()
														   .addFilter("UserFilters", SimpleBeanPropertyFilter.serializeAllExcept("passowrd"))
														   .addFilter("UsuarioResponseFilters", SimpleBeanPropertyFilter.serializeAllExcept("token"));
											
														   
													
				
																			//Filtro pra Deserializar Sensitive Datas
		mapper.setFilterProvider(filters);
		return mapper;
	}
	
}
