package com.devPontes.LeialaoME.repositories;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.devPontes.LeialaoME.model.entities.Leilao;
import com.devPontes.LeialaoME.model.entities.enums.StatusOferta;

@Component
public interface LeilaoRepositories extends JpaRepository<Leilao, Long> {

	@Query("SELECT DISTINCT l FROM Leilao l JOIN FETCH l.ofertas o WHERE o.statusOferta = :status")
	List<Leilao> findLeilaoPorStatus(@Param("status") StatusOferta status); 
	
	@Query("SELECT l FROM Leilao l WHERE l.inicio > :agora AND l.inicio <= :tempoLimite")
	Set<Leilao> buscarLeiloesFuturosAteData(@Param("agora") LocalDateTime agora, 
											@Param("tempoLimite")LocalDateTime tempoLimite);
	
}
