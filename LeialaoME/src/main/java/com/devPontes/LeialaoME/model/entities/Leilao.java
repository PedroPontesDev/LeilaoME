package com.devPontes.LeialaoME.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_leilao")
public class Leilao {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Lob
	private String descricao;
	
	private Usuario comprador;
		

}
