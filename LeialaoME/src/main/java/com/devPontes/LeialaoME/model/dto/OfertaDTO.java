package com.devPontes.LeialaoME.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import com.devPontes.LeialaoME.model.entities.enums.StatusOferta;

public class OfertaDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Double valorOferta;
	private StatusOferta statusOferta;
	private LocalDateTime tempoDuravel;
	private boolean aceita;
	private UsuarioCompradorDTO comprador;
	private LeilaoDTO leilao;

	public OfertaDTO() {
	}

	public OfertaDTO(Long id, Double valorOferta, StatusOferta statusOferta, LocalDateTime tempoDuravel, boolean aceita,
			UsuarioCompradorDTO comprador, LeilaoDTO leilao) {
		this.id = id;
		this.valorOferta = valorOferta;
		this.statusOferta = statusOferta;
		this.tempoDuravel = tempoDuravel;
		this.aceita = aceita;
		this.comprador = comprador;
		this.leilao = leilao;
	}

	public LeilaoDTO getLeilao() {
		return leilao;
	}

	public void setLeilao(LeilaoDTO leilao) {
		this.leilao = leilao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getValorOferta() {
		return valorOferta;
	}

	public void setValorOferta(Double valorOferta) {
		this.valorOferta = valorOferta;
	}

	public StatusOferta getStatusOferta() {
		return statusOferta;
	}

	public void setStatusOferta(StatusOferta statusOferta) {
		this.statusOferta = statusOferta;
	}

	public LocalDateTime getTempoDuravel() {
		return tempoDuravel;
	}

	public void setTempoDuravel(LocalDateTime tempoDuravel) {
		this.tempoDuravel = tempoDuravel;
	}

	public boolean isAceita() {
		return aceita;
	}

	public void setAceita(boolean aceita) {
		this.aceita = aceita;
	}

	public UsuarioCompradorDTO getComprador() {
		return comprador;
	}

	public void setComprador(UsuarioCompradorDTO comprador) {
		this.comprador = comprador;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OfertaDTO other = (OfertaDTO) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "OfertaDTO [id=" + id + ", valorOferta=" + valorOferta + ", statusOferta=" + statusOferta
				+ ", tempoDuravel=" + tempoDuravel + ", aceita=" + aceita + ", comprador=" + comprador + "]";
	}

}
