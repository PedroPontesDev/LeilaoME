package com.devPontes.LeialaoME.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devPontes.LeialaoME.security.dtos.UsuarioLoginRequestDTO;
import com.devPontes.LeialaoME.security.dtos.UsuarioLoginResponseDTO;
import com.devPontes.LeialaoME.services.impl.AuthServices;

@CrossOrigin(origins = "http://192.168.1.2:5173")
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

	@Autowired
	AuthServices authServices;
	
	@PostMapping(path = "/login")
	public ResponseEntity<UsuarioLoginResponseDTO> login(@RequestBody UsuarioLoginRequestDTO request) {
		UsuarioLoginResponseDTO token = authServices.login(request);
		return new ResponseEntity<UsuarioLoginResponseDTO>(token, HttpStatus.OK);
	}
	
}
