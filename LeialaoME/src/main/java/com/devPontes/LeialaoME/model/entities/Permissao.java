package com.devPontes.LeialaoME.model.entities;

import com.devPontes.LeialaoME.model.entities.enums.UsuarioRole;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_permissao")
public class Permissao  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated
	UsuarioRole usuarioRole;
	
	
}
