package com.devPontes.LeialaoME.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.security.dtos.UsuarioLoginRequestDTO;
import com.devPontes.LeialaoME.security.dtos.UsuarioLoginResponseDTO;
import com.devPontes.LeialaoME.security.services.CustomUserDetailsServices;
import com.devPontes.LeialaoME.security.services.JwtService;
import com.devPontes.LeialaoME.utils.CnpjCpfValidadorClient;

@Service
public class AuthServices {

	private final AuthenticationManager auth;
	private final JwtService jwtService;
	private final CustomUserDetailsServices userDetailsService;
	
	private static final Logger log = LoggerFactory.getLogger(AuthServices.class);

	public AuthServices(AuthenticationManager auth, JwtService jwtService,
			CustomUserDetailsServices userDetailsService) {
		this.auth = auth;
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
	}

	public UsuarioLoginResponseDTO login(UsuarioLoginRequestDTO request) {
        try {
            // 1️ Autenticar usuário (lança exceção se inválido)
            auth.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // 2️ Carregar detalhes do usuário
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

            // 3️ Gerar token JWT
            String token = jwtService.generateToken(userDetails);
            log.info("Login bem-sucedido para usuário {}. Token gerado.", request.getUsername());

            // 4️ Retornar resposta com token
            return new UsuarioLoginResponseDTO(request.getUsername(), token);

        } catch (Exception e) {
            log.warn("Falha no login para usuário {}: {}", request.getUsername(), e.getMessage());
            throw new RuntimeException("Usuário inexistente ou senha inválida");
        }
    }
}


