package com.devPontes.LeialaoME.security.configs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.devPontes.LeialaoME.security.JWT.services.JwtTokenFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Primary
@Order(1)
public class WebSecurityConfig {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	 http
         .cors(Customizer.withDefaults())
         .csrf(csrf -> csrf.disable())
         
         // O filtro só será aplicado a qualquer rota exceto /v1/auth/**
         .securityMatcher("/**")
         
         .authorizeHttpRequests(auth -> auth
             .requestMatchers("/v1/auth/**").permitAll()
             .requestMatchers("/v1/comprador/*/upload-foto").hasRole("COMPRADOR")
             .requestMatchers("/v1/leilao/criar-leilao").hasRole("VENDEDOR")
             .anyRequest().authenticated()
         )
         
         // Aqui está o segredo:
         // Só adicionamos o filtro nas rotas autenticadas
         .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

    	 return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Origens permitidas
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://192.168.0.100:5173"));

        // Métodos HTTP permitidos
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH" , "OPTIONS"));

        // Cabeçalhos permitidos
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "Origin"));

        // Expõe o header Authorization para o frontend
        configuration.setExposedHeaders(List.of("Authorization"));

        // Permite envio de cookies e credenciais
        configuration.setAllowCredentials(true);

        // Registra para todas as rotas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
