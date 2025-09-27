package com.devPontes.LeialaoME.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devPontes.LeialaoME.model.entities.Usuario;

public interface UsuarioCompradorRepositories extends JpaRepository<Usuario, Long> {
	
	Optional<Usuario> findByUsername(String username);
	

}
