package com.devPontes.LeialaoME.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devPontes.LeialaoME.model.entities.Permissao;
import com.devPontes.LeialaoME.model.entities.enums.UsuarioRole;

@Repository
public interface PermissaoRepositories extends JpaRepository<Permissao, Long> {

	Permissao findByUsuarioRole(UsuarioRole roleComprador);
	
	
}
