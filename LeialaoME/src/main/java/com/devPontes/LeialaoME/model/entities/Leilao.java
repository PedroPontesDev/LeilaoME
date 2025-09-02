package com.devPontes.LeialaoME.model.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_leilao")
public class Leilao {

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
	private BigDecimal valorIncrementadoAposLance;

	@Column(name = "lance_inicial")
	private BigDecimal lanceInicial;

	@ManyToOne
	@JoinColumn(name = "vendedor_id")
	private UsuarioVendedor vendedor;

	@ManyToOne
	@JoinColumn(name = "comprador_id", nullable = false)
	private UsuarioComprador comprador;

	@OneToMany(mappedBy = "leilao")
	private List<Oferta> ofertas = new ArrayList<>();
	
	@Column
	private boolean indaAtivo = false;

	public Leilao(Long id, String descricao, byte[] fotoProduto, LocalDateTime inicio, LocalDateTime termino,
			BigDecimal valorIncrementadoAposLance, BigDecimal lanceInicial, UsuarioVendedor vendedor,
			List<Oferta> ofertas) {
		this.id = id;
		this.descricao = descricao;
		this.fotoProduto = fotoProduto;
		this.inicio = inicio;
		this.termino = termino;
		this.valorIncrementadoAposLance = valorIncrementadoAposLance;
		this.lanceInicial = lanceInicial;
		this.vendedor = vendedor;
		this.ofertas = ofertas;
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

	public BigDecimal getValorIncrementadoAposLance() {
		return valorIncrementadoAposLance;
	}

	public void setValorIncrementadoAposLance(BigDecimal valorIncrementadoAposLance) {
		this.valorIncrementadoAposLance = valorIncrementadoAposLance;
	}

	public BigDecimal getLanceInicial() {
		return lanceInicial;
	}

	public void setLanceInicial(BigDecimal lanceInicial) {
		this.lanceInicial = lanceInicial;
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
				+ ", inicio=" + inicio + ", termino=" + termino + ", valorIncrementadoAposLance="
				+ valorIncrementadoAposLance + ", lanceInicial=" + lanceInicial + ", vendedor=" + vendedor
				+ ", ofertas=" + ofertas + "]";
	}

}
