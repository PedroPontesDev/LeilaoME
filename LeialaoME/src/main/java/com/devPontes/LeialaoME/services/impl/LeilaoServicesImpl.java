package com.devPontes.LeialaoME.services.impl;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.exceptions.LeilaoEncerradoException;
import com.devPontes.LeialaoME.exceptions.LeilaoException;
import com.devPontes.LeialaoME.exceptions.UsuarioNaoEncontradoException;
import com.devPontes.LeialaoME.model.dto.LeilaoDTO;
import com.devPontes.LeialaoME.model.dto.UsuarioCompradorDTO;
import com.devPontes.LeialaoME.model.entities.Leilao;
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
	public LeilaoDTO criarLeilaoSobVendedor(Long vendedorId, LeilaoDTO novoLeilao) {
		UsuarioVendedor vendedor = vendedorRepository.findById(vendedorId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException("Vendedor não encontrado com o ID" + vendedorId));
		Leilao leilao = MyMaper.parseObject(novoLeilao, Leilao.class);
		leilao.setVendedor(vendedor);

		leilaoRepository.save(leilao);
		return MyMaper.parseObject(leilao, LeilaoDTO.class);
	}

	@Override
	public LeilaoDTO abrirLeilaoComPoucaMargemDeTempo(LeilaoDTO leilao, LocalDateTime tempoNecessario) {
		Leilao entity = MyMaper.parseObject(leilao, Leilao.class);

		var tempoInicial = entity.getInicio();
		var termino = entity.getTermino();

		if (!entity.isIndaAtivo())
			throw new LeilaoEncerradoException("Leilão encerrado ou inativo");

		Duration duracao = Duration.between(tempoInicial, termino);

		if (duracao.isNegative() || duracao.isZero())
			throw new LeilaoException("Duracao de leilão deve ser iniciada!");

		long horasNecessarias = tempoNecessario.getHour();

		if (duracao.toHours() < horasNecessarias) 
			throw new LeilaoException("Leilão não tem tempo suficiente para abrir com a margem desejada!");
		
		entity.setTermino(termino.minusHours(horasNecessarias));
		
		leilaoRepository.save(entity);
		
		return MyMaper.parseObject(entity, LeilaoDTO.class);
	}

	@Override
	public void fecharLeilao(Long leilaoId, String statusOferta) {
		// TODO Auto-generated method stub

	}

	@Override
	public LeilaoDTO verificarEstadoDeLeilao(Long leilaoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	//Precisa verificar a maior oferta dentro do leilao, verificar se esta inativo e setar o ganhador verificando qual é a oferta mais cara baseado
	//Nas ofertas dadas pelos compradores
	public UsuarioCompradorDTO definirGanhador(Long leilaoId) {
		// TODO Auto-generated method stub
		return null;
	}

}
