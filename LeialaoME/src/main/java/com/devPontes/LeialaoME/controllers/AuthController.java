package com.devPontes.LeialaoME.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devPontes.LeialaoME.model.DTO.v1.UsuarioDTO;
import com.devPontes.LeialaoME.security.DTO.v1.UsuarioLoginRequestDTO;
import com.devPontes.LeialaoME.security.DTO.v1.UsuarioLoginResponseDTO;
import com.devPontes.LeialaoME.security.services.AuthServicesImpl;
import com.devPontes.LeialaoME.services.impl.UsuarioCompradorServicesImpl;
import com.devPontes.LeialaoME.services.impl.UsuarioVendedorServicesImpl;
import com.fasterxml.jackson.annotation.JsonFilter;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

	@Autowired
	AuthServicesImpl authServices;
	
	@Autowired
	UsuarioVendedorServicesImpl vendedorServices;
	
	@Autowired
	UsuarioCompradorServicesImpl compradorServicesImpl;
	
	private final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@PostMapping(path = "/login", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> login(@RequestBody UsuarioLoginRequestDTO request, HttpServletResponse response) {
		UsuarioLoginResponseDTO token = authServices.login(request, response);
		logger.info("TOKEN GERADO ->" + token.getToken());
		return new ResponseEntity<UsuarioLoginResponseDTO>(token, HttpStatus.OK);
	}
	
	@PostMapping(path = "/cadastrar-vendedor", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<UsuarioDTO> registrarUsuarioVendedor(@RequestBody UsuarioDTO usuario) throws Exception {
		UsuarioDTO saved = vendedorServices.cadastrarUsuarioVendedor(usuario);
	    return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}

	@PostMapping(path = "/cadastrar-comprador", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<UsuarioDTO> registrarUsuarioComprador(@RequestBody UsuarioDTO usuario) throws Exception {
		UsuarioDTO saved = compradorServicesImpl.cadastrarUsuarioComprador(usuario);
	    return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}

}
