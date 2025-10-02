package com.devPontes.LeialaoME.security.services;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.exceptions.UsuarioNaoEncontradoException;
import com.devPontes.LeialaoME.model.entities.Usuario;
import com.devPontes.LeialaoME.repositories.UsuarioRepositories;

@Service
public class CustomUserDetailsServices implements UserDetailsService {

		@Autowired
		private final UsuarioRepositories userRepository;

		public CustomUserDetailsServices(UsuarioRepositories userRepository) {
			this.userRepository = userRepository;
		}

		@Override
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			// busca usuário concreto (subclasse de Usuario)
			Usuario usuario = userRepository.findByUsername(username).orElseThrow(
					() -> new UsuarioNaoEncontradoException("Usuário não encontrado com username: " + username));

			// mapeia roles dinamicamente, funciona para qualquer subclasse
			var authorities = usuario.getAuthorities().stream()
					.map(r -> new SimpleGrantedAuthority("ROLE_" + r.getAuthority()))
					.collect(Collectors.toList());

			// retorna UserDetails concreto do Spring Security pode criar new a partir de um pacote contantoq seja implementado o objeto ou interfae!
			
			return new org.springframework.security.core.userdetails.User(usuario.getUsername(), usuario.getPassword(), authorities);
		}
	
}
