package com.devPontes.LeialaoME.services;

import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.model.entities.Leilao;
import com.devPontes.LeialaoME.model.entities.Oferta;

@Service
public interface LeilaoServices {

	Leilao criarLeialaoSobVendedor(Long vendedorId, Leilao leilao);
	Leilao abrirLeilaoComPoucaMargemDeTempo(Leilao leilao, Oferta oferta, Long compradorId, Long vendedorId);
	Leilao receberPorpostaDeOferta(Long compradorId, Oferta oferta);
}
