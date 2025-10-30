package com.devPontes.LeialaoME.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devPontes.LeialaoME.model.entities.Usuario;

public interface UsuarioRepositories extends JpaRepository<Usuario, Long> {
	
	@Query("SELECT u FROM Usuario u WHERE LOWER(u.username) = LOWER(:username)")
	Optional<Usuario> findByUsername(@Param("username") String username);

	
}
