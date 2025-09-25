package com.devPontes.LeialaoME.services;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.model.dto.LeilaoDTO;
import com.devPontes.LeialaoME.model.dto.UsuarioCompradorDTO;

@Service
public interface LeilaoServices {

	LeilaoDTO criarLeilao(Long vendedorId, LeilaoDTO novoLeilao);

	LeilaoDTO abrirLeilaoComPoucaMargemDeTempo(LeilaoDTO novoLeilao, Long tempoNecessario);

	LeilaoDTO verificarEstadoDeLeilao(Long leilaoId);

	UsuarioCompradorDTO definirGanhador(Long leilaoId) throws Exception;
	
	Set<LeilaoDTO> findLeiloesFuturos(LocalDate proximoMes, String descricaoLeilao);
	
	void fecharLeilao(Long leilaoId, String status);
}
