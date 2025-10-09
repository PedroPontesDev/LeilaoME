package com.devPontes.LeialaoME.security.JWT.services;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.devPontes.LeialaoME.model.entities.Usuario;

@Service
public class JwtService {


	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private long expiration;

	public String generateToken(UserDetails userDetails) {

		Long userId = ((Usuario) userDetails).getId(); // PAra passar pro front
		
		try {

			Algorithm alg = Algorithm.HMAC256(secret);

			String token = JWT.create()
					.withIssuer("Leilao-WebService")
					.withSubject(userDetails.getUsername())
					.withExpiresAt(calculateExpiration())
					.withClaim("id", userId)
					.withClaim("roles", userDetails.getAuthorities()
													.stream().map(GrantedAuthority::getAuthority)
													.collect(Collectors.toList())).sign(alg);
			return token;

		} catch (JWTCreationException ex) {
			throw new JWTCreationException("Erro ao gerar token" + ex.getMessage(), ex);
		}

	}

	public String validateToken(String token) {
		Algorithm alg = Algorithm.HMAC256(secret);

		return JWT.require(alg)
				.withIssuer("Leilao-WebService").build()
				.verify(token)
				.getSubject();
	}

	private Date calculateExpiration() {
		return Date.from(Instant.now().plusSeconds(expiration));
	}

}

