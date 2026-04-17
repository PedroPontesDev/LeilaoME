package com.devPontes.LeialaoME.configs;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JacksonFilters {

	@Bean
	public ObjectMapper objectMapper() { //ObjectMapper pra serializar e deserializa json 
		ObjectMapper mapper = new ObjectMapper();
		
		//Filtro pra Deserializar Sensitive Datas
		FilterProvider filters = new SimpleFilterProvider() 
									 .addFilter("UserFilters", SimpleBeanPropertyFilter.serializeAllExcept("passowrd"))
									 .addFilter("UsuarioResponseFilters", SimpleBeanPropertyFilter.serializeAllExcept("token"));
											
														   
													
				
																		
		mapper.setFilterProvider(filters);
		mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
		return mapper;
	}
	
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jsonCostumizer() {
	    return builder -> { builder.modules(new JavaTimeModule());}; //Deserializar a caceta da data com JsonFormatter
    }
	
	
}
