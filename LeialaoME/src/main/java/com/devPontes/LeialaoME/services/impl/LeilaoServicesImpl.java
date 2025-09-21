package com.devPontes.LeialaoME.services.impl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.exceptions.LeilaoException;
import com.devPontes.LeialaoME.exceptions.UsuarioNaoEncontradoException;
import com.devPontes.LeialaoME.model.dto.LeilaoDTO;
import com.devPontes.LeialaoME.model.dto.UsuarioCompradorDTO;
import com.devPontes.LeialaoME.model.entities.Leilao;
import com.devPontes.LeialaoME.model.entities.Oferta;
import com.devPontes.LeialaoME.model.entities.UsuarioComprador;
import com.devPontes.LeialaoME.model.entities.UsuarioVendedor;
import com.devPontes.LeialaoME.model.entities.mapper.MyMaper;
import com.devPontes.LeialaoME.repositories.LeilaoRepositories;
import com.devPontes.LeialaoME.repositories.UsuarioCompradorRepositories;
import com.devPontes.LeialaoME.repositories.UsuarioVendedorRepositories;
import com.devPontes.LeialaoME.services.LeilaoServices;

@Service
public class LeilaoServicesImpl implements LeilaoServices {

	@Autowired
	private LeilaoRepositories leilaoRepository;

	@Autowired
	private UsuarioCompradorRepositories compradorRepository;

	@Autowired
	private UsuarioVendedorRepositories vendedorRepository;

	@Override
	public LeilaoDTO abrirLeilaoComValorInicial(LeilaoDTO novoLeilao, Double lanceIniciaç) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LeilaoDTO criarLeilao(Long vendedorId, LeilaoDTO novoLeilao) {
		UsuarioVendedor vendedor = vendedorRepository.findById(vendedorId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException("Vendedor não encontrado com o ID" + vendedorId));
		Leilao leilao = MyMaper.parseObject(novoLeilao, Leilao.class);
		leilao.setVendedor(vendedor);

		leilaoRepository.save(leilao);
		return MyMaper.parseObject(leilao, LeilaoDTO.class);
	}

	@Override // ACHO Q ESTA ERRADO
	public LeilaoDTO abrirLeilaoComPoucaMargemDeTempo(LeilaoDTO leilao, Long tempoMinimoHoras) {

		Leilao entity = MyMaper.parseObject(leilao, Leilao.class);

		if (!entity.isAindaAtivo())
			throw new LeilaoException("Leilão está inativo!");

		// Calcula duração atual
		Duration duracaoAtual = Duration.between(entity.getInicio(), entity.getTermino());

		// Se o leilão já é menor que o mínimo, lança exceção
		if (duracaoAtual.toHours() < tempoMinimoHoras)
			throw new LeilaoException("O leilão não possui margem/tempo suficiente para abrir.");

		// Se duracaoAtual do leilao for maior que tempo minimo esperado em horas eu
		// seto o tempo
		// calculado
		LocalDateTime tempoCalculado = entity.getInicio().plusHours(tempoMinimoHoras);
		if (duracaoAtual.toHours() > tempoMinimoHoras) {
			entity.setTermino(tempoCalculado);
		}

		leilaoRepository.save(entity);

		return MyMaper.parseObject(entity, LeilaoDTO.class);
	}

	@Override
	public void fecharLeilao(Long leilaoId, String statusOferta) {

	}

	@Override
	public LeilaoDTO verificarEstadoDeLeilao(Long leilaoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	// Precisa verificar a maior oferta dentro do leilao, verificar se esta inativo
	// e setar o ganhador verificando qual é a oferta mais cara baseado
	// Nas ofertas dadas pelos compradores
	public UsuarioCompradorDTO definirGanhador(Long leilaoId) throws Exception {
		Leilao leilaoExistente = leilaoRepository.findById(leilaoId)
				.orElseThrow(() -> new LeilaoException("Vendedor não encontrado com o ID" + leilaoId));
		//Fazer nova validações
		
		// Garantir que existem ofertas
	    List<Oferta> ofertas = leilaoExistente.getOfertas();
	    if (ofertas == null || ofertas.isEmpty()) {
	        throw new LeilaoException("Nenhuma oferta encontrada neste leilão.");
	    }
	    
		Oferta maiorOferta = leilaoExistente.getOfertas().stream().max(null).get();


	}

	@Override
	public Set<LeilaoDTO> findLeiloesFuturos(LocalDate proximoMes, String descricaoLeilao) {
		// TODO Auto-generated method stub
		return null;
	}

}
