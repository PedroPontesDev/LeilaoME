package com.devPontes.LeialaoME.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.model.DTO.v1.LeilaoDTO;
import com.devPontes.LeialaoME.model.DTO.v1.OfertaDTO;
import com.devPontes.LeialaoME.model.entities.Usuario;

@Service
public interface LeilaoServices {


	LeilaoDTO criarLeilao(LeilaoDTO novoLeilao, Usuario usernameLogado);
	
	LeilaoDTO criarLeilaoFuturo(LeilaoDTO novoLeilao, LocalDateTime tempoInicio, LocalDateTime tempoFim,
			Usuario usuarioLogado);
	
	LeilaoDTO abrirLeilaoComPoucaMargemDeTempo(LeilaoDTO novoLeilao, Long tempoNecessario, Usuario usuarioLogado);

	LeilaoDTO verificarEstadoDeLeilao(Long leilaoId);

	LeilaoDTO definirGanhador(Long leilaoId, Usuario usuarioLogado) throws Exception; //SOMENTE ADMINS EXECUTAM ESSE METODO
	
	Set<OfertaDTO> visualizarOfertasDeLeilao(Usuario usuarioLogado, Long leilaoId);
	
	Set<LeilaoDTO> findLeiloesFuturos(LocalDate proximoMes);
	
	List<LeilaoDTO> findAll();
	
	List<LeilaoDTO> findLeiloesDeUsuarioLogado(Usuario usuarioLogado, String cnpj, Long leilãoId);
	
	void fecharLeilao(Long leilaoId, String status);



}