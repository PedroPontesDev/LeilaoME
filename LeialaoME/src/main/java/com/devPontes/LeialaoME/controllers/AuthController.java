package com.devPontes.LeialaoME.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.devPontes.LeialaoME.services.impl.UsuarioVendedorServicesImpl;

@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8080"})
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

	@Autowired
	AuthServicesImpl authServices;
	
	@Autowired
	UsuarioVendedorServicesImpl vendedorServices;
	
	@PostMapping(path = "/login")
	public ResponseEntity<UsuarioLoginResponseDTO> login(@RequestBody UsuarioLoginRequestDTO request) {
		UsuarioLoginResponseDTO token = authServices.login(request);
		return new ResponseEntity<UsuarioLoginResponseDTO>(token, HttpStatus.OK);
	}
	

	//SO ADMINS
	@PostMapping(path = "/cadastrar-vendedor")
	public ResponseEntity<UsuarioDTO> registrarUsuarioVendedor(@RequestBody UsuarioDTO usuario) throws Exception {
		UsuarioDTO saved = vendedorServices.cadastrarUsuarioVendedor(usuario);
	    return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}

}
