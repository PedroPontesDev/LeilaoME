package com.devPontes.LeialaoME.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.exceptions.LeilaoEncerradoException;
import com.devPontes.LeialaoME.exceptions.LeilaoException;
import com.devPontes.LeialaoME.exceptions.UsuarioNaoEncontradoException;
import com.devPontes.LeialaoME.model.dto.OfertaDTO;
import com.devPontes.LeialaoME.model.entities.Leilao;
import com.devPontes.LeialaoME.model.entities.Oferta;
import com.devPontes.LeialaoME.model.entities.UsuarioComprador;
import com.devPontes.LeialaoME.model.entities.UsuarioVendedor;
import com.devPontes.LeialaoME.model.entities.enums.StatusOferta;
import com.devPontes.LeialaoME.model.entities.mapper.MyMaper;
import com.devPontes.LeialaoME.repositories.LeilaoRepositories;
import com.devPontes.LeialaoME.repositories.OfertaRepositories;
import com.devPontes.LeialaoME.repositories.UsuarioCompradorRepositories;
import com.devPontes.LeialaoME.repositories.UsuarioVendedorRepositories;
import com.devPontes.LeialaoME.services.OfertaService;

import jakarta.transaction.Transactional;

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
		Leilao leilaoExistente = leilaoRepository.findById(leilaoId)
				.orElseThrow(() -> new LeilaoException("Leilão não encontrado com ID " + leilaoId));

		if (!leilaoExistente.isAindaAtivo()) {
			throw new LeilaoEncerradoException("Leilão encerrado ou desativado!");
		}

		UsuarioComprador comprador = (UsuarioComprador) compradorRepository.findById(compradorId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado com ID " + compradorId));

		// Converte DTO para entidade
		Oferta ofertaNova = MyMaper.parseObject(ofertaDTO, Oferta.class);
		ofertaNova.setComprador(comprador);
		ofertaNova.setStatusOferta(StatusOferta.ATIVA);

		if (ofertaNova.getMomentoOferta().isAfter(leilaoExistente.getTermino()))
			throw new LeilaoException("A oferta so deve ser feita quando leilão estiver aberto!");

		// Calcula valor mínimo permitido
		Double valorMinimo = calcularNovoLanceMinimo(leilaoId);

		// Valida valor da oferta
		if (ofertaNova.getValorOferta() < valorMinimo) {
			throw new LeilaoException("O valor da oferta deve ser igual ou maior que: " + valorMinimo);
		}

		// Atualiza valor incrementado no leilão
		leilaoExistente.setValorDeIncremento(ofertaNova.getValorOferta() + valorMinimo);

		// Adiciona oferta ao leilão
		leilaoExistente.getOfertas().add(ofertaNova);

		// Salva oferta e leilão
		ofertaRepository.save(ofertaNova);
		leilaoRepository.save(leilaoExistente);

		// Retorna DTO atualizado
		return MyMaper.parseObject(ofertaNova, OfertaDTO.class);
	}

	@Override
	@Transactional
	/**
	 * Faz novos lances apos ofertas chegarem ao maximo
	 * 
	 * @param leilaoId  id do leilao que fara o PUT de novo lance
	 * @param novoValor novoValor que vem da request
	 * @return OfertaDTO com as info no JSON da atulização do novo lance com as
	 *         ofertas atualizadas
	 * @throws Exception caso algo dê errado mas sera tratado
	 */
	public OfertaDTO fazerNovoLanceCasoOfertasSubam(Double novoValor, Long leilaoId, Long compradorId) {

		// 1. Buscar leilão e comprador pra ver se ecoincidem na mesma transação

		Leilao leilao = leilaoRepository.findById(leilaoId)
				.orElseThrow(() -> new LeilaoException("Leillão não encontraado com Id" + leilaoId));

		if (!leilao.isAindaAtivo()) {
			throw new LeilaoEncerradoException("Leilão encerrado ou desativado!");
		}

		UsuarioComprador usuarioComprador = (UsuarioComprador) compradorRepository.findById(compradorId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não encontraado com Id" + leilaoId));

		// 2. Pegar a oferta mais alta do leilão
		Oferta ofertaMaisAlta = leilao.getOfertas().stream().filter(o -> o.getStatusOferta() == StatusOferta.ATIVA)
				.max(Comparator.comparingDouble(Oferta::getValorOferta)).orElse(null);

		// 3. Pegar a oferta anterior do mesmo comprador (se houver)
		Oferta ofertaAnteriorDoMesmoComprador = leilao.getOfertas().stream()
				.filter(o -> o.getComprador().getId().equals(compradorId))
				.max(Comparator.comparingDouble(Oferta::getValorOferta)).orElse(null);

		Double lanceMaisAltoAtual = (ofertaMaisAlta != null) ? ofertaMaisAlta.getValorOferta()
				: leilao.getLanceInicial();

		Double lanceMinimoPermitido = lanceMaisAltoAtual
				+ (leilao.getValorDeIncremento() != null ? leilao.getValorDeIncremento() : 0.0);

		// Faz validações de ofertas caso subam e de compradores anteriores

		if (novoValor < lanceMinimoPermitido) {
			throw new IllegalArgumentException("O lance mínimo permitido é de R$ " + lanceMinimoPermitido);
		}

		if (ofertaAnteriorDoMesmoComprador != null && novoValor <= ofertaAnteriorDoMesmoComprador.getValorOferta()) {
			throw new IllegalArgumentException("Seu novo lance deve ser maior que seu lance anterior de R$ "
					+ ofertaAnteriorDoMesmoComprador.getValorOferta());
		}

		if (ofertaMaisAlta != null && ofertaMaisAlta.getStatusOferta() != StatusOferta.ATIVA)
			throw new IllegalArgumentException("Oferta de leilão não é mais válida");

		if (ofertaMaisAlta != null && novoValor <= ofertaMaisAlta.getValorOferta())
			throw new IllegalArgumentException("Seu novo lance deve ser maior que o seu lance anterior.");

		if (ofertaAnteriorDoMesmoComprador != null) {
			ofertaAnteriorDoMesmoComprador.setStatusOferta(StatusOferta.INTATIVA);
			ofertaRepository.save(ofertaAnteriorDoMesmoComprador);
		}

		leilao.setComprador(usuarioComprador);

		Oferta novaOferta = new Oferta();
		novaOferta.setComprador(usuarioComprador);
		novaOferta.setValorOferta(novoValor);
		novaOferta.setMomentoOferta(LocalDateTime.now());
		novaOferta.setStatusOferta(StatusOferta.ATIVA);
		novaOferta.setLeilao(leilao);

		leilao.getOfertas().add(novaOferta);

		// Persistir alterações
		leilaoRepository.save(leilao);
		return MyMaper.parseObject(novaOferta, OfertaDTO.class);
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

	@Override
	public OfertaDTO aceitarOfertaDeLeilao(Long usarioVendedorId, Long leilaoId, Long ofertaId) {
		UsuarioVendedor usuarioVendedor = (UsuarioVendedor) vendedorRepository.findById(usarioVendedorId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException("Vendedor não encontradro!"));
		
		Leilao leilaoVendedor = usuarioVendedor
				.getLeilaoCadastrado()
				.stream()
				.filter(l -> l.getVendedor().getId().equals(usarioVendedorId))
				.findFirst().orElse(null);
		
		Oferta ofertaNegada = null;
		
		for(Oferta oferta : leilaoVendedor.getOfertas()) {
			if(oferta.getId().equals(ofertaId) && oferta.getStatusOferta() == StatusOferta.ATIVA) {
				oferta.setStatusOferta(StatusOferta.INTATIVA);
				ofertaNegada = ofertaRepository.save(oferta);
				break;
			}
		}
		
		return MyMaper.parseObject(ofertaNegada, OfertaDTO.class);
	}

	@Override
	public OfertaDTO negarOfertaDeLeilao(Long usarioVendedorId, Long LeilaoId) {
		// TODO Auto-generated method stub
		return null;
	}

}
