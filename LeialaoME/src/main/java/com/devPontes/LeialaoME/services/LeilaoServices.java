package com.devPontes.LeialaoME.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.model.dto.LeilaoDTO;
import com.devPontes.LeialaoME.model.dto.UsuarioCompradorDTO;
import com.devPontes.LeialaoME.model.entities.Usuario;

@Service
public interface LeilaoServices {


	LeilaoDTO criarLeilao(LeilaoDTO novoLeilao, Usuario usernameLogado);
	
	LeilaoDTO criarLeilaoFuturo(LeilaoDTO novoLeilao, LocalDateTime tempoInicio, LocalDateTime tempoFim,
			Usuario usuarioLogado);
	
	
	LeilaoDTO abrirLeilaoComPoucaMargemDeTempo(LeilaoDTO novoLeilao, Long tempoNecessario);

	LeilaoDTO verificarEstadoDeLeilao(Long leilaoId);

	LeilaoDTO definirGanhador(Long leilaoId) throws Exception;
	
	Set<LeilaoDTO> findLeiloesFuturos(LocalDate proximoMes, String descricaoLeilao);
	
	void fecharLeilao(Long leilaoId, String status);



}