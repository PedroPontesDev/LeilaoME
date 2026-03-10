package com.devPontes.LeialaoME.security.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.security.DTO.v1.UsuarioLoginRequestDTO;
import com.devPontes.LeialaoME.security.DTO.v1.UsuarioLoginResponseDTO;
import com.devPontes.LeialaoME.security.JWT.services.JwtService;

@Service
public class AuthServicesImpl {

	@Autowired
    private AuthenticationManager auth; //Manager de autenticação do SPring Sec
	
	@Autowired
	private JwtService jwtService;

	@Autowired
	private CustomUserDetailsService userDetailsService;

    private static final Logger log = LoggerFactory.getLogger(AuthServicesImpl.class);

    public UsuarioLoginResponseDTO login(UsuarioLoginRequestDTO request) {
            auth.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));  // autentica usuário

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());   // carrega dados do usuário direto do banco
   
            String token = jwtService.generateToken(userDetails);   // gera token
            log.info("Login bem-sucedido para usuário {}. Token gerado.", request.getUsername());
            return new UsuarioLoginResponseDTO(request.getUsername(), token);

    }
}
 //Sempre utilizar UserDetails pra carregar o usuario do banco 