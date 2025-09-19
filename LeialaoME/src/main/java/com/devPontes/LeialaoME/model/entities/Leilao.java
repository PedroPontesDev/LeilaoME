package com.devPontes.LeialaoME.model.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_leilao")
public class Leilao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@Lob
	private String descricao;

	@Lob
	private byte[] fotoProduto;

	private LocalDateTime inicio;

	private LocalDateTime termino;

	@Column
	private Double valorDeIncremento; //Valor de Incremento == ValorDaforera?

	@Column(name = "lance_inicial")
	private Double lanceInicial;

	@ManyToOne
	@JoinColumn(name = "vendedor_id")
	private UsuarioVendedor vendedor;

	@OneToMany(mappedBy = "leilao", fetch = FetchType.EAGER)
	private List<Oferta> ofertas = new ArrayList<>();

	@Column
	private boolean aindaAtivo = false;

	public Leilao(Long id, String descricao, byte[] fotoProduto, LocalDateTime inicio, LocalDateTime termino,
			Double valorDeIncremento, Double lanceInicial, UsuarioVendedor vendedor, List<Oferta> ofertas,
			boolean aindaAtivo) {
		this.id = id;
		this.descricao = descricao;
		this.fotoProduto = fotoProduto;
		this.inicio = inicio;
		this.termino = termino;
		this.valorDeIncremento = valorDeIncremento;
		this.lanceInicial = lanceInicial;
		this.vendedor = vendedor;
		this.ofertas = ofertas;
		this.aindaAtivo = aindaAtivo;
	}

	public Leilao() {
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

	public byte[] getFotoProduto() {
		return fotoProduto;
	}

	public void setFotoProduto(byte[] fotoProduto) {
		this.fotoProduto = fotoProduto;
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
	public Double getValorDeIncremento() {
		return valorDeIncremento;
	}

	public void setValorDeIncremento(Double valorDeIncremento) {
		this.valorDeIncremento = valorDeIncremento;
	}

	public Double getLanceInicial() {
		return lanceInicial;
	}

	public void setLanceInicial(Double lanceInicial) {
		this.lanceInicial = lanceInicial;
	}

	public boolean isAindaAtivo() {
		return aindaAtivo;
	}

	public void setAindaAtivo(boolean aindaAtivo) {
		this.aindaAtivo = aindaAtivo;
	}

	public UsuarioVendedor getVendedor() {
		return vendedor;
	}

	public void setVendedor(UsuarioVendedor vendedor) {
		this.vendedor = vendedor;
	}

	public List<Oferta> getOfertas() {
		return ofertas;
	}

	public void setOfertas(List<Oferta> ofertas) {
		this.ofertas = ofertas;
	}

	public boolean isIndaAtivo() {
		return aindaAtivo;
	}

	public void setIndaAtivo(boolean aindaAtivo) {
		this.aindaAtivo = aindaAtivo;
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
		Leilao other = (Leilao) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Leilao [id=" + id + ", descricao=" + descricao + ", fotoProduto=" + Arrays.toString(fotoProduto)
				+ ", inicio=" + inicio + ", termino=" + termino + ", valorDeIncremento=" + valorDeIncremento
				+ ", lanceInicial=" + lanceInicial + ", vendedor=" + vendedor + ", ofertas=" + ofertas + ", aindaAtivo="
				+ aindaAtivo + "]";
	}

	
}
