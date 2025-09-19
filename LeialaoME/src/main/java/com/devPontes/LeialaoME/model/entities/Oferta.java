package com.devPontes.LeialaoME.model.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.devPontes.LeialaoME.model.entities.enums.StatusOferta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_oferta")
public class Oferta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Double valorOferta;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StatusOferta statusOferta;

	@ManyToOne
	@JoinColumn(name = "comprador_id", nullable = false)
	private UsuarioComprador comprador;

	@ManyToOne
	@JoinColumn(name = "leilao_id", nullable = false)
	private Leilao leilao;

	@Column(nullable = false)
	private LocalDateTime momentoOferta = LocalDateTime.now();

	public Oferta() {
	}

	public Oferta(Long id, Double valorOferta, StatusOferta statusOferta, UsuarioComprador comprador, Leilao leilao,
			LocalDateTime momentoOferta) {
		super();
		this.id = id;
		this.valorOferta = valorOferta;
		this.statusOferta = statusOferta;
		this.comprador = comprador;
		this.leilao = leilao;
		this.momentoOferta = momentoOferta;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StatusOferta getStatusOferta() {
		return statusOferta;
	}

	public void setStatusOferta(StatusOferta statusOferta) {
		this.statusOferta = statusOferta;
	}

	public UsuarioComprador getComprador() {
		return comprador;
	}

	public void setComprador(UsuarioComprador comprador) {
		this.comprador = comprador;
	}

	public Leilao getLeilao() {
		return leilao;
	}

	public void setLeilao(Leilao leilao) {
		this.leilao = leilao;
	}

	public Double getValorOferta() {
		return valorOferta;
	}

	public void setValorOferta(Double valorOferta) {
		this.valorOferta = valorOferta;
	}

	public LocalDateTime getMomentoOferta() {
		return momentoOferta;
	}

	public void setMomentoOferta(LocalDateTime momentoOferta) {
		this.momentoOferta = momentoOferta;
	}

	@Override
	public String toString() {
		return "Oferta [id=" + id + ", valorOferta=" + valorOferta + ", statusOferta=" + statusOferta + ", comprador="
				+ comprador + ", leilao=" + leilao + ", momentoOferta=" + momentoOferta + "]";
	}

}
