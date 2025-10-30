package com.devPontes.LeialaoME.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.devPontes.LeialaoME.model.entities.Pagamento;

@Component
public interface PagamentosRepositories extends JpaRepository<Pagamento, Long> {

	
	
}
