package com.devPontes.LeialaoME.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.devPontes.LeialaoME.services.impl.InformationEnviroment;

@RestController
public class DockerController {

	@Autowired
	private InformationEnviroment dockerInfo;
	
	
}
