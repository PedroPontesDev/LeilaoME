package com.devPontes.LeialaoME.services.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.security.dtos.UsuarioLoginRequestDTO;
import com.devPontes.LeialaoME.security.dtos.UsuarioLoginResponseDTO;
import com.devPontes.LeialaoME.security.services.CustomUserDetailsServices;
import com.devPontes.LeialaoME.security.services.JwtService;

@Service
public class AuthServices {

	private final AuthenticationManager auth;
	private final JwtService jwtService;
	private final CustomUserDetailsServices userDetailsService;

	public AuthServices(AuthenticationManager auth, JwtService jwtService,
			CustomUserDetailsServices userDetailsService) {
		this.auth = auth;
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
	}

	public UsuarioLoginResponseDTO login(UsuarioLoginRequestDTO request) {
		 // autenticação correta
        auth.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // carregar detalhes do usuário
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        // gerar token JWT
        String token = jwtService.generateToken(userDetails);

        return new UsuarioLoginResponseDTO(request.getUsername(), token);
    }

	}


