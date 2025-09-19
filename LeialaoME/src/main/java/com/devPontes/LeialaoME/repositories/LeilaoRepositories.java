package com.devPontes.LeialaoME.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.devPontes.LeialaoME.model.entities.Leilao;
import com.devPontes.LeialaoME.model.entities.enums.StatusOferta;

@Component
public interface LeilaoRepositories extends JpaRepository<Leilao, Long> {

	
	List<Leilao> findLeilaoPorStatus(@Param("status") StatusOferta status); 
	
}
