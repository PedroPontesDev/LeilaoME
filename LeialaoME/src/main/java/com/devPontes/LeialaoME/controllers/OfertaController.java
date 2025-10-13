package com.devPontes.LeialaoME.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8080"})
@RestController
@RequestMapping("/v1/oferta")
public class OfertaController {

	private static final Logger log = LoggerFactory.getLogger(OfertaController.class);

	
}
