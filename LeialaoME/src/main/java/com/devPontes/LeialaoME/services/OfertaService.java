package com.devPontes.LeialaoME.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.model.dto.OfertaDTO;

@Service
public interface OfertaService {

	OfertaDTO fazerPropostaParaLeilao(OfertaDTO oferta, Long leilaoId, Long compradorId);

	OfertaDTO fazerNovoLanceCasoOfertasSubam(Double novoValor, Long leilaoId, Long compradorId);

	Double calcularNovoLanceMinimo(Long leilaoId);


}
