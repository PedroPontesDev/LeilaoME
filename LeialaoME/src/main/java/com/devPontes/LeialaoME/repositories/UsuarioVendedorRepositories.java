package com.devPontes.LeialaoME.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devPontes.LeialaoME.model.DTO.v1.LeilaoDTO;
import com.devPontes.LeialaoME.model.entities.UsuarioVendedor;

public interface UsuarioVendedorRepositories extends JpaRepository<UsuarioVendedor, Long> {

	@Query("SELECT CASE WHEN COUNT (u) > 0 THEN TRUE ELSE FALSE END "
			+ "FROM UsuarioVendedor u"
			+ " WHERE u.username = :username")
	boolean existsByUsername(String username);
	
	
	@Query("SELECT l FROM Leilao l WHERE l.vendedor.id = :usuarioVendedorId")
	Set<LeilaoDTO> findLeiloesParticipados(Long usuarioVendedorId);
	
	
}