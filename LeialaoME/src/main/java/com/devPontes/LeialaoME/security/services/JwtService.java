package com.devPontes.LeialaoME.security.services;


import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.devPontes.LeialaoME.exceptions.JWTCreationException;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private long expiration;

	public String generateToken(UserDetails userDetails) {

		try {

			Algorithm alg = Algorithm.HMAC256(secret);

			String token = JWT.create().withIssuer("Leilao-WebService")
					.withSubject(userDetails.getUsername())
					.withExpiresAt(calculateExpiration())
					.sign(alg);

			return token;

		} catch (JWTCreationException ex) {
			throw new JWTCreationException("Erro ao gerar token" + ex.getMessage());
		}

	}
	
	public String validateToken(String token) {
		Algorithm alg = Algorithm.HMAC512(secret);
		
		return JWT.require(alg)
				  .withIssuer("Leilao-WebService")
				  .build()
				  .verify(token).getSubject();
	}


	private Date calculateExpiration() {
    return Date.from(Instant.now().plusSeconds(expiration));
}
	
}
