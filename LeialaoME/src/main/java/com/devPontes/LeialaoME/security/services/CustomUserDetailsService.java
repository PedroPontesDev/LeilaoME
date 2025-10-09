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
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepositories usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

}
