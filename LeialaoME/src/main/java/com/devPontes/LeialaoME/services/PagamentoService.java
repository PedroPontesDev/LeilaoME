package com.devPontes.LeialaoME.services;

import com.devPontes.LeialaoME.model.DTO.v1.PagamentoRequestDTO;
import com.devPontes.LeialaoME.model.DTO.v1.PagamentoResponseDTO;
import com.devPontes.LeialaoME.model.DTO.v1.WebHookPagamentoDTO;

public interface PagamentoService {

	
	PagamentoResponseDTO gerarCobrancaPix(PagamentoRequestDTO requestPix);
	
	void handleWebHookPagamento(WebHookPagamentoDTO webHook);
	
}
