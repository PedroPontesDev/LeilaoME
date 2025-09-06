package com.devPontes.LeialaoME.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class LeilaoDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String descricao;
	private LocalDateTime inicio;
	private LocalDateTime termino;
	private BigDecimal lanceInicial;
	private BigDecimal valorIncrementadoAposLance;
	private boolean ativo;
	private UsuarioVendedorDTO vendedor;
	private List<OfertaDTO> ofertas;
	private UsuarioCompradorDTO possivelVencedor;
	
	public LeilaoDTO() {}

	public LeilaoDTO(Long id, String descricao, LocalDateTime inicio, LocalDateTime termino, BigDecimal lanceInicial,
			BigDecimal valorIncrementadoAposLance, boolean ativo, UsuarioVendedorDTO vendedor, List<OfertaDTO> ofertas,
			UsuarioCompradorDTO possivelVencedor) {
		this.id = id;
		this.descricao = descricao;
		this.inicio = inicio;
		this.termino = termino;
		this.lanceInicial = lanceInicial;
		this.valorIncrementadoAposLance = valorIncrementadoAposLance;
		this.ativo = ativo;
		this.vendedor = vendedor;
		this.ofertas = ofertas;
		this.possivelVencedor = possivelVencedor;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDateTime getInicio() {
		return inicio;
	}

	public void setInicio(LocalDateTime inicio) {
		this.inicio = inicio;
	}

	public LocalDateTime getTermino() {
		return termino;
	}

	public void setTermino(LocalDateTime termino) {
		this.termino = termino;
	}

	public BigDecimal getLanceInicial() {
		return lanceInicial;
	}

	public void setLanceInicial(BigDecimal lanceInicial) {
		this.lanceInicial = lanceInicial;
	}

	public BigDecimal getValorIncrementadoAposLance() {
		return valorIncrementadoAposLance;
	}

	public void setValorIncrementadoAposLance(BigDecimal valorIncrementadoAposLance) {
		this.valorIncrementadoAposLance = valorIncrementadoAposLance;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public UsuarioVendedorDTO getVendedor() {
		return vendedor;
	}

	public void setVendedor(UsuarioVendedorDTO vendedor) {
		this.vendedor = vendedor;
	}

	public UsuarioCompradorDTO getPossivelVencedor() {
		return possivelVencedor;
	}

	public void setPossivelVencedor(UsuarioCompradorDTO possivelVencedor) {
		this.possivelVencedor = possivelVencedor;
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
		LeilaoDTO other = (LeilaoDTO) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "LeilaoDTO [id=" + id + ", descricao=" + descricao + ", inicio=" + inicio + ", termino=" + termino
				+ ", lanceInicial=" + lanceInicial + ", valorIncrementadoAposLance=" + valorIncrementadoAposLance
				+ ", ativo=" + ativo + ", vendedor=" + vendedor + ", ofertas=" + ofertas + ", possivelVencedor="
				+ possivelVencedor + "]";
	}
	
	
	

	// Getters e Setters
}
