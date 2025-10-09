package com.devPontes.LeialaoME.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.security.DTO.UsuarioLoginRequestDTO;
import com.devPontes.LeialaoME.security.DTO.UsuarioLoginResponseDTO;
import com.devPontes.LeialaoME.security.JWT.services.JwtService;
import com.devPontes.LeialaoME.security.services.CustomUserDetailsService;

@Service
public class AuthServices {

    private final AuthenticationManager auth;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    private static final Logger log = LoggerFactory.getLogger(AuthServices.class);

    public AuthServices(AuthenticationManager auth, JwtService jwtService,
                        CustomUserDetailsService userDetailsService) {
        this.auth = auth;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    public UsuarioLoginResponseDTO login(UsuarioLoginRequestDTO request) {
        try {
            // autentica usuário
            auth.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            // carrega dados do usuário
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

            // gera token
            String token = jwtService.generateToken(userDetails);
            log.info("Login bem-sucedido para usuário {}. Token gerado.", request.getUsername());

            return new UsuarioLoginResponseDTO(request.getUsername(), token);

        } catch (Exception e) {
            log.warn("Falha no login para usuário {}: {}", request.getUsername(), e.getMessage());
            throw new RuntimeException("Usuário inexistente ou senha inválida");
        }
    }
}
