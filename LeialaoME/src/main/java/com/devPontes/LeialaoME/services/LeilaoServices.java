package com.devPontes.LeialaoME.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.model.dto.LeilaoDTO;
import com.devPontes.LeialaoME.model.dto.UsuarioCompradorDTO;

@Service
public interface LeilaoServices {

	LeilaoDTO criarLeilaoSobVendedor(Long vendedorId, LeilaoDTO novoLeilao);
	
	LeilaoDTO abrirLeilaoComValorInicial(LeilaoDTO novoLeilao, Double lanceIniciaç);

	LeilaoDTO abrirLeilaoComPoucaMargemDeTempo(LeilaoDTO leilao, LocalDateTime tempoNecessario);

	void fecharLeilao(Long leilaoId, String status);

	LeilaoDTO verificarEstadoDeLeilao(Long leilaoId);

	UsuarioCompradorDTO definirGanhador(Long leilaoId);
}
