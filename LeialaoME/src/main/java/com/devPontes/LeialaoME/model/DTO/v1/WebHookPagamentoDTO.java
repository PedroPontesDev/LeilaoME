package com.devPontes.LeialaoME.model.DTO.v1;

import com.devPontes.LeialaoME.model.entities.enums.StatusPagamento;

public class WebHookPagamentoDTO {

    private String txId;
    private StatusPagamento status;

    public String getTxId() { return txId; }
    public void setTxId(String txId) { this.txId = txId; }
    public StatusPagamento getStatus() { return status; }
    public void setStatus(StatusPagamento status) { this.status = status; }
	
}
