package com.devPontes.LeialaoME.model.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Pagamento implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	private Long id;
	
	private LocalDateTime horarioDoPagamento;
	
	private boolean foiConcluido;
	
	private Double totalPagamento;

	public Pagamento(Long id, LocalDateTime horarioDoPagamento, boolean foiConcluido, Double totalPagamento) {
		this.id = id;
		this.horarioDoPagamento = horarioDoPagamento;
		this.foiConcluido = foiConcluido;
		this.totalPagamento = totalPagamento;
	}
	
	
	
	

}
