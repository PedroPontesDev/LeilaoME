package com.devPontes.LeialaoME.services.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.exceptions.LeilaoEncerradoException;
import com.devPontes.LeialaoME.exceptions.LeilaoException;
import com.devPontes.LeialaoME.exceptions.UsuarioNaoEncontradoException;
import com.devPontes.LeialaoME.model.dto.OfertaDTO;
import com.devPontes.LeialaoME.model.entities.Leilao;
import com.devPontes.LeialaoME.model.entities.Oferta;
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
	public OfertaDTO fazerPropostaParaLeilao(OfertaDTO ofertaDTO, Long leilaoId, Long compradorId) {
		// Busca leilão
		Leilao leilaoExistente = leilaoRepository.findById(leilaoId)
				.orElseThrow(() -> new LeilaoException("Leilão não encontrado com ID " + leilaoId));

		// Verifica se leilão está ativo
		if (!leilaoExistente.isAindaAtivo()) {
			throw new LeilaoEncerradoException("Leilão encerrado ou desativado!");
		}

		// Busca comprador
		UsuarioComprador comprador = (UsuarioComprador) compradorRepository.findById(compradorId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado com ID " + compradorId));

		// Converte DTO para entidade
		Oferta ofertaNova = MyMaper.parseObject(ofertaDTO, Oferta.class);
		ofertaNova.setComprador(comprador);
		ofertaNova.setStatusOferta(StatusOferta.ATIVA);

		if (ofertaNova.getMomentoOferta().isAfter(leilaoExistente.getTermino())) {
			throw new LeilaoException("A oferta so deve ser feita quando leilão estiver aberto!");
		}

		// Calcula valor mínimo permitido
		Double valorMinimo = calcularNovoLanceMinimo(leilaoId);

		// Valida valor da oferta
		if (ofertaNova.getValorOferta() < valorMinimo) {
			throw new LeilaoException("O valor da oferta deve ser igual ou maior que: " + valorMinimo);
		}

		// Atualiza valor incrementado no leilão
		leilaoExistente.setValorDeIncremento(ofertaNova.getValorOferta());

		// Adiciona oferta ao leilão
		leilaoExistente.getOfertas().add(ofertaNova);

		// Salva oferta e leilão
		ofertaRepository.save(ofertaNova);
		leilaoRepository.save(leilaoExistente);

		// Retorna DTO atualizado
		return MyMaper.parseObject(ofertaNova, OfertaDTO.class);
	}

	@Override
	// Trabalhar com datas verificar se ofertas subiR
	public OfertaDTO fazerNovoLanceCasoOfertasSubam(Double novoValor, Long leilaoId, Long compradorId) {
		// Verificar se o comprador deu o lance no leilao e percorrer os lances de
		// determinado leilao verificar o ultimo lance
		// caso ultimo lance subir e o status ainda estr ativo fazer nova oferta maior q
		// a ultina
		return null;
	}

	@Override
	public Double calcularNovoLanceMinimo(Long leilaoId) {
		Leilao leilao = leilaoRepository.findById(leilaoId)
				.orElseThrow(() -> new LeilaoException("Leilao não encontrado com ID" + leilaoId));
		// Lance inicial do leilão

		double lanceMinimo;

		if (leilao.getLanceInicial() != null) {
			lanceMinimo = leilao.getLanceInicial();
		} else {
			lanceMinimo = 0.0;
		}

		// Valor de incremento definido pelo leilão
		double incremento = leilao.getValorDeIncremento();

		for (Oferta oferta : leilao.getOfertas()) {
			if (!ofertaValida(oferta)) {
				continue;
			}

			double proximoLance = lanceMinimo + incremento;

			if (proximoLance > lanceMinimo) {
				lanceMinimo = proximoLance;
			}
		}

		return lanceMinimo;

	}

	private boolean ofertaValida(Oferta oferta) {
		if (oferta.getComprador() == null)
			return false;
		if (oferta.getMomentoOferta() == null && oferta.getMomentoOferta().isBefore(LocalDateTime.now()))
			return false;
		if (oferta.getStatusOferta() != StatusOferta.ATIVA)
			return false;
		return true;
	}

}
