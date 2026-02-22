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

	LeilaoDTO criarLeilaoReduzido(LeilaoDTO leilao, Long tempoMinimoHoras, Usuario usuarioLogado);
	
	LeilaoDTO criarLeilaoFuturo(LeilaoDTO novoLeilao, LocalDateTime tempoInicio, LocalDateTime tempoFim,
			Usuario usuarioLogado);
	
	List<LeilaoDTO> findLeilaoPorStatus(String status);

	LeilaoDTO definirGanhador(Long leilaoId, Usuario usuarioLogado) throws Exception; 
	
	Set<OfertaDTO> visualizarOfertasDeLeilao(Usuario usuarioLogado, Long leilaoId);
	
	Set<LeilaoDTO> findLeiloesFuturos(LocalDate proximoMes) throws Exception;
	
	List<LeilaoDTO> findAll();
	
	void fecharLeilao(Long leilaoId);

	List<LeilaoDTO> findLeiloesDeUsuarioComprador(Usuario usuarioLogado, String cpf, Long leilãoId);
	
	List<LeilaoDTO> findLeiloesDeUsuarioVendedor(Usuario usuarioLogado, String cnpj, Long leilãoId);



}