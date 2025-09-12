package com.devPontes.LeialaoME.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.exceptions.LeilaoEncerradoException;
import com.devPontes.LeialaoME.exceptions.LeilaoException;
import com.devPontes.LeialaoME.exceptions.UsuarioNaoEncontradoException;
import com.devPontes.LeialaoME.model.dto.OfertaDTO;
import com.devPontes.LeialaoME.model.entities.Leilao;
import com.devPontes.LeialaoME.model.entities.Oferta;
import com.devPontes.LeialaoME.model.entities.Usuario;
import com.devPontes.LeialaoME.model.entities.UsuarioComprador;
import com.devPontes.LeialaoME.model.entities.enums.StatusOferta;
import com.devPontes.LeialaoME.model.entities.mapper.MyMaper;
import com.devPontes.LeialaoME.repositories.LeilaoRepositories;
import com.devPontes.LeialaoME.repositories.OfertaRepositories;
import com.devPontes.LeialaoME.repositories.UsuarioCompradorRepositories;
import com.devPontes.LeialaoME.repositories.UsuarioVendedorRepositories;
import com.devPontes.LeialaoME.services.OfertaService;

@Service
public class OfertaServicesImpl implements OfertaService {

	@Autowired
	private LeilaoRepositories leilaoRepository;

	@Autowired
	private OfertaRepositories ofertaRepository;

	@Autowired
	private UsuarioCompradorRepositories compradorRepository;

	@Autowired
	private UsuarioVendedorRepositories vendedorRepository;

	@Override
	public OfertaDTO fazerPropostaParaLeilao(OfertaDTO oferta, Long leilaoId, Long compradorId) {
		Leilao leilaoExistente = leilaoRepository.findById(leilaoId)
				.orElseThrow(() -> new LeilaoException("Leilão não encontrado com ID" + leilaoId));

		UsuarioComprador comprador = (UsuarioComprador) compradorRepository.findById(compradorId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado com ID" + compradorId));
	
		Oferta ofertaNova = MyMaper.parseObject(oferta, Oferta.class);
		ofertaNova.setAceita(true);
		ofertaNova.setStatusOferta(StatusOferta.ATIVA);
		ofertaNova.setComprador(comprador);

		if(!leilaoExistente.isIndaAtivo()) {
			throw new LeilaoEncerradoException("Leilão encerrado ou desativado!");
		}
		
		if()
		
		
		ofertaRepository.save(ofertaNova);
		leilaoRepository.save(leilaoExistente);

	}

	@Override
	public OfertaDTO fazerNovoLanceCasoOfertasSubam(Double novoValor, Long leilaoId, Long compradorId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double calcularNovoLanceMinimo(Long leilaoId) {
		// Para cada novo valor em leiloes>ofertas calcular novo valor
		return 0;
	}

}
