package com.devPontes.LeialaoME.services.impl;

import java.util.UUID;

import com.devPontes.LeialaoME.exceptions.LeilaoException;
import com.devPontes.LeialaoME.model.DTO.v1.PagamentoRequestDTO;
import com.devPontes.LeialaoME.model.DTO.v1.PagamentoResponseDTO;
import com.devPontes.LeialaoME.model.DTO.v1.WebHookPagamentoDTO;
import com.devPontes.LeialaoME.model.entities.Leilao;
import com.devPontes.LeialaoME.model.entities.Pagamento;
import com.devPontes.LeialaoME.repositories.LeilaoRepositories;
import com.devPontes.LeialaoME.repositories.PagamentosRepositories;
import com.devPontes.LeialaoME.services.PagamentoService;

public class PagamentoServiceImpl implements PagamentoService {

	private final PagamentosRepositories pagamentoRepository;
    private final LeilaoRepositories leilaoRepository;

    public PagamentoServiceImpl(PagamentosRepositories pagamentoRepository, LeilaoRepositories leilaoRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.leilaoRepository = leilaoRepository;
    }

	@Override
	public PagamentoResponseDTO gerarCobrancaPix(PagamentoRequestDTO requestPix) {

		Leilao leilaoCobranca = leilaoRepository.findById(requestPix.getCompradorId())
											    .orElseThrow(() -> new LeilaoException("Leilao não encontrado ID:" + requestPix.getLeilaoId()));
		
		
		
		String txid = UUID.randomUUID().toString();
		String qrCode = "00020126580014BR.GOV.BCB.PIX0114chave-pix-fake5204000053039865406"
                   + requestPix.getValor() + "5802BR5920LeilaoMe Mock System";
		
		Pagamento novoPagamento = new Pagamento();
		novoPagamento.setTxid(txid);
		novoPagamento.setQrCodeText(qrCode);
		
		return null;
	
	}

	@Override
	public void handleWebHookPagamento(WebHookPagamentoDTO webHook) {
		// TODO Auto-generated method stub
		
	}

    
    
	
}
