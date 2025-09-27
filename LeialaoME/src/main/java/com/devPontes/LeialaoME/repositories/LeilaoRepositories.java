package com.devPontes.LeialaoME.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.devPontes.LeialaoME.model.entities.Leilao;

@Component
public interface LeilaoRepositories extends JpaRepository<Leilao, Long> {

	//@Query("SELECT l Leilao WHERE")
	//List<Leilao> findLeilaoPorStatus(@Param("status") StatusOferta status); 
	
}
