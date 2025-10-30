package com.devPontes.LeialaoME.model.DTO.v1;

import java.time.LocalDateTime;
import java.util.Objects;

import com.devPontes.LeialaoME.model.entities.enums.StatusOferta;

public class PagamentoRequestDTO {

	public Long leilaoId;
	public Long ofertaId;
	public Long compradorId;
	public Double valor;
	public String descricao;

	public PagamentoRequestDTO(Long leilaoId, Long ofertaId, Long compradorId, Double valor, String descricao) {
		this.leilaoId = leilaoId;
		this.ofertaId = ofertaId;
		this.compradorId = compradorId;
		this.valor = valor;
		this.descricao = descricao;
	}

	public PagamentoRequestDTO() {

	}

	public Long getLeilaoId() {
		return leilaoId;
	}

	public void setLeilaoId(Long leilaoId) {
		this.leilaoId = leilaoId;
	}

	public Long getOfertaId() {
		return ofertaId;
	}

	public void setOfertaId(Long ofertaId) {
		this.ofertaId = ofertaId;
	}

	public Long getCompradorId() {
		return compradorId;
	}

	public void setCompradorId(Long compradorId) {
		this.compradorId = compradorId;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public int hashCode() {
		return Objects.hash(compradorId, descricao, leilaoId, ofertaId, valor);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PagamentoRequestDTO other = (PagamentoRequestDTO) obj;
		return Objects.equals(compradorId, other.compradorId) && Objects.equals(descricao, other.descricao)
				&& Objects.equals(leilaoId, other.leilaoId) && Objects.equals(ofertaId, other.ofertaId)
				&& Objects.equals(valor, other.valor);
	}

	@Override
	public String toString() {
		return "PagamentoRequestDTO [leilaoId=" + leilaoId + ", ofertaId=" + ofertaId + ", compradorId=" + compradorId
				+ ", valor=" + valor + ", descricao=" + descricao + "]";
	}

}
