package com.devPontes.LeialaoME.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devPontes.LeialaoME.model.dto.UsuarioDTO;
import com.devPontes.LeialaoME.security.dtos.UsuarioLoginRequestDTO;
import com.devPontes.LeialaoME.security.dtos.UsuarioLoginResponseDTO;
import com.devPontes.LeialaoME.security.services.CustomUserDetailsServices;
import com.devPontes.LeialaoME.security.services.JwtService;
import com.devPontes.LeialaoME.services.impl.UsuarioCompradorServicesImpl;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UsuarioCompradorServicesImpl userServices;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private CustomUserDetailsServices userDetailsService;

	@PostMapping(path = "/login")
	public UsuarioLoginResponseDTO login(@RequestBody UsuarioLoginRequestDTO request) {
		Authentication auth = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), 
																	 request.getPassword()));
		UserDetails userDeitals = userDetailsService.loadUserByUsername(request.getUsername());
		
		String token = jwtService.generateToken(userDeitals);
		
		return new UsuarioLoginResponseDTO(request.getUsername(), token);
	}
	

	@PostMapping(path = "/cadastrar-comprador")
	public ResponseEntity<UsuarioDTO> registrarUsuarioComprador(@RequestBody UsuarioDTO usuario) throws Exception {
		UsuarioDTO saved =  userServices.cadastrarUsuarioComprador(usuario);
	    return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}
	
	

}