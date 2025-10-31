package com.devPontes.LeialaoME.model.DTO.v1;

import com.devPontes.LeialaoME.model.entities.enums.StatusPagamento;

public class PagamentoResponseDTO {

	private String txId;
	private String qrCode;
	private StatusPagamento status;

	public String getTxId() {
		return txId;
	}

	public String getQrCode() {
		return qrCode;
	}

	public StatusPagamento getStatus() {
		return status;
	}
}
