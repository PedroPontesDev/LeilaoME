package com.devPontes.LeialaoME.model.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import com.devPontes.LeialaoME.model.entities.enums.StatusPagamento;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name  =  "tb_pagamentos")
public class Pagamento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	private String txid; // ID único da cobrança Pix (EfiPay gera)
	private Double valor;
	private String qrCodeImage;
	private String qrCodeText;
	private String providerChargeId;

	@Enumerated(EnumType.STRING)
	private StatusPagamento status = StatusPagamento.PENDENTE;

	@OneToOne
	private Oferta oferta; // ou Leilao, depende de onde o pagamento acontece

	private LocalDateTime criadoEm = LocalDateTime.now();

	public Pagamento(Long id, String txid, Double valor, String qrCodeImage, String qrCodeText, StatusPagamento status,
			Oferta oferta, LocalDateTime criadoEm) {
		this.id = id;
		this.txid = txid;
		this.valor = valor;
		this.qrCodeImage = qrCodeImage;
		this.qrCodeText = qrCodeText;
		this.status = status;
		this.oferta = oferta;
		this.criadoEm = criadoEm;
	}

	public Pagamento(Long id, String txid, Double valor, String qrCodeImage, String qrCodeText, String providerChargeId,
			StatusPagamento status, Oferta oferta, LocalDateTime criadoEm) {
		super();
		this.id = id;
		this.txid = txid;
		this.valor = valor;
		this.qrCodeImage = qrCodeImage;
		this.qrCodeText = qrCodeText;
		this.providerChargeId = providerChargeId;
		this.status = status;
		this.oferta = oferta;
		this.criadoEm = criadoEm;
	}

	public Pagamento() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getQrCodeImage() {
		return qrCodeImage;
	}

	public void setQrCodeImage(String qrCodeImage) {
		this.qrCodeImage = qrCodeImage;
	}

	public String getQrCodeText() {
		return qrCodeText;
	}

	public void setQrCodeText(String qrCodeText) {
		this.qrCodeText = qrCodeText;
	}

	public StatusPagamento getStatus() {
		return status;
	}

	public void setStatus(StatusPagamento status) {
		this.status = status;
	}

	public Oferta getOferta() {
		return oferta;
	}

	public void setOferta(Oferta oferta) {
		this.oferta = oferta;
	}

	public LocalDateTime getCriadoEm() {
		return criadoEm;
	}

	public void setCriadoEm(LocalDateTime criadoEm) {
		this.criadoEm = criadoEm;
	}

	public String getProviderChargeId() {
		return providerChargeId;
	}

	public void setProviderChargeId(String providerChargeId) {
		this.providerChargeId = providerChargeId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, txid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pagamento other = (Pagamento) obj;
		return Objects.equals(id, other.id) && Objects.equals(txid, other.txid);
	}

	@Override
	public String toString() {
		return "Pagamento [id=" + id + ", txid=" + txid + ", valor=" + valor + ", qrCodeImage=" + qrCodeImage
				+ ", qrCodeText=" + qrCodeText + ", providerChargeId=" + providerChargeId + ", status=" + status
				+ ", oferta=" + oferta + ", criadoEm=" + criadoEm + "]";
	}

}
