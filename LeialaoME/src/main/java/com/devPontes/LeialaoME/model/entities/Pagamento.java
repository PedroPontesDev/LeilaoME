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
		this.setHorarioDoPagamento(horarioDoPagamento);
		this.setFoiConcluido(foiConcluido);
		this.setTotalPagamento(totalPagamento);
	}

	public boolean isFoiConcluido() {
		return foiConcluido;
	}

	public void setFoiConcluido(boolean foiConcluido) {
		this.foiConcluido = foiConcluido;
	}

	public Double getTotalPagamento() {
		return totalPagamento;
	}

	public void setTotalPagamento(Double totalPagamento) {
		this.totalPagamento = totalPagamento;
	}

	public LocalDateTime getHorarioDoPagamento() {
		return horarioDoPagamento;
	}

	public void setHorarioDoPagamento(LocalDateTime horarioDoPagamento) {
		this.horarioDoPagamento = horarioDoPagamento;
	}
	
	
	
	

}
