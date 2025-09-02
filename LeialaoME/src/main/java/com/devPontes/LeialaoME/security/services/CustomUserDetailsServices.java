package com.devPontes.LeialaoME.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.exceptions.UsuarioNaoEncontradoException;
import com.devPontes.LeialaoME.repositories.UsuarioRepositories;

@Service
public class CustomUserDetailsServices  implements UserDetailsService{

	@Autowired
	UsuarioRepositories userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username)
							.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado com  username:" + username));
	}

}
