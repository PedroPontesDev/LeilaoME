package com.devPontes.LeialaoME.configs;

import java.util.TimeZone;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class TimeZoneConfig { //Classe de configuração de configuração de tempo do servidor igual de SP
	
	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Sao Paulo"));
	}

}
