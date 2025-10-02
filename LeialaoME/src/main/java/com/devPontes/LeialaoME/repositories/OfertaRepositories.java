package com.devPontes.LeialaoME.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devPontes.LeialaoME.model.entities.Oferta;

public interface OfertaRepositories extends JpaRepository<Oferta, Long> {

	/*@Query("  SELECT o FROM Oferta JOIN "
			+ "FETCH o.usuarioComprador uc WHERE uc.id =: userId "
			+ "AND o.valorOferta <= valorMaximo ")
	Optional<Oferta> ofertaMaisBaixa(@Param("userId") Long userId, 
									@Param("valorMaximo") Double valorMaximo); */
}