
package com.devPontes.LeialaoME.services.impl;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.exceptions.LeilaoEncerradoException;
import com.devPontes.LeialaoME.exceptions.LeilaoException;
import com.devPontes.LeialaoME.exceptions.UsuarioNaoEncontradoException;
import com.devPontes.LeialaoME.model.DTO.v1.OfertaDTO;
import com.devPontes.LeialaoME.model.entities.Leilao;
import com.devPontes.LeialaoME.model.entities.Oferta;
import com.devPontes.LeialaoME.model.entities.Usuario;
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
	public OfertaDTO fazerPropostaParaLeilao(OfertaDTO ofertaDTO, Long leilaoId, Usuario usuarioLogado) {
		Leilao leilaoExistente = leilaoRepository.findById(leilaoId)
				.orElseThrow(() -> new LeilaoException("Leilão não encontrado com ID " + leilaoId));

		if (!leilaoExistente.isAindaAtivo()) {
			throw new LeilaoEncerradoException("Leilão encerrado ou desativado!");
		}

		UsuarioComprador comprador = (UsuarioComprador) compradorRepository.findById(usuarioLogado.getId()).orElseThrow(
				() -> new UsuarioNaoEncontradoException("Usuário não encontrado com ID " + usuarioLogado.getId()));

		// Converte DTO para entidade
		Oferta ofertaNova = MyMaper.parseObject(ofertaDTO, Oferta.class);
		ofertaNova.setComprador(comprador);
		ofertaNova.setStatusOferta(StatusOferta.ATIVA);
		ofertaNova.setVendor(leilaoExistente.getVendedor());

		if (ofertaNova.getMomentoOferta() == null) 
			ofertaNova.setMomentoOferta(LocalDateTime.now());
		
		if (ofertaNova.getMomentoOferta().isAfter(leilaoExistente.getTermino()))
			throw new LeilaoException("A oferta so deve ser feita quando leilão estiver aberto!");
		
		if (ofertaNova.getMomentoOferta().isBefore(leilaoExistente.getInicio())) 
			throw new LeilaoException("A oferta não deve ser feita antes do leilão ter iniciado");

		// Calcula valor mínimo permitido
		Double valorMinimo = calcularNovoLance(leilaoId);

		// Valida valor da oferta
		if (ofertaNova.getValorOferta() <= valorMinimo) 
			throw new LeilaoException("O valor da oferta deve ser igual ou maior que: " + valorMinimo);
		
		// Adiciona oferta ao leilão e vice versa
		leilaoExistente.getOfertas().add(ofertaNova);
		ofertaNova.setLeilao(leilaoExistente);

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
	 * @param compradorId comprador logado que é responsavel pelo PUT
	 * @param leilaoId    id do leilao que recebera o PUT de novo lance
	 * @param novoValor   novoValor que vem da request
	 * @return OfertaDTO com as info no JSON da atulização do novo lance com as
	 *         ofertas atualizadas
	 * @throws Exception caso algo dê errado mas sera tratado
	 */
	public OfertaDTO fazerNovoLanceCasoOfertasSubam(Double novoValor, Long leilaoId, Long compradorId) { // Usar o
																											// AuthenticationPrincipal

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
				.max(Comparator.comparingDouble(Oferta::getValorOferta)).get();

		Double lanceMinimoPermitido = calcularNovoLance(leilaoId);

		if (novoValor < lanceMinimoPermitido) {
			throw new IllegalArgumentException("O lance mínimo permitido é de R$ " + lanceMinimoPermitido);
		}

		if (ofertaMaisAlta != null && ofertaMaisAlta.getStatusOferta() != StatusOferta.ATIVA)
			throw new IllegalArgumentException("Oferta de leilão não é mais válida");
		
		if(novoValor <= ofertaMaisAlta.getValorOferta())
			throw new IllegalArgumentException("Valor inserido ainda não compete com o valor mais alto!");

		Oferta novaOferta = new Oferta();

		novaOferta.setValorOferta(novoValor);
		novaOferta.setMomentoOferta(LocalDateTime.now());
		novaOferta.setStatusOferta(StatusOferta.ATIVA);
		novaOferta.setLeilao(leilao);

		leilao.setComprador(usuarioComprador); // Commprador no contexto atual do novo lance
		leilao.getOfertas().add(novaOferta);

		// Persistir alterações
		leilaoRepository.save(leilao);
		ofertaRepository.save(novaOferta);
		return MyMaper.parseObject(novaOferta, OfertaDTO.class);
	}

	@Override
	public Double calcularNovoLance(Long leilaoId) {
		Leilao leilao = leilaoRepository.findById(leilaoId)
				.orElseThrow(() -> new LeilaoException("Leilao não encontrado com ID" + leilaoId));

		return leilao.getOfertas()
				.stream()
				.filter(o -> o.getStatusOferta() == StatusOferta.ATIVA || o.getStatusOferta() == StatusOferta.ACEITA)
				.map(Oferta::getValorOferta)
				.max(Double::compareTo)
				.orElseThrow(() -> new LeilaoException("Nenhuma oferta encontrada!"));

	}

	@Override
	@Transactional
	public OfertaDTO aceitarOfertaDeLeilao(Usuario usuarioLogado, Long leilaoId, Long ofertaId) {
		Leilao leilao = leilaoRepository.findById(leilaoId)
				.orElseThrow(() -> new LeilaoException("Leilão não encontrado para o ID " + leilaoId));

		if (!leilao.getVendedor().getId().equals(usuarioLogado.getId())) {
			throw new SecurityException("Você não é o dono deste leilão!");
		}

		Oferta ofertaExistente = ofertaRepository.findById(ofertaId)
				.orElseThrow(() -> new IllegalArgumentException("Oferta não existe para o ID " + ofertaId));

		if (!ofertaExistente.getLeilao().getId().equals(leilaoId)) {
			throw new IllegalArgumentException("Esta oferta não pertence a este leilão!");
		}

		boolean ofertaAceita = false;
		Oferta ofertaAceitaFinal = null;

		Double valorMinimoPermitidoo = calcularNovoLance(leilaoId);

		if (ofertaExistente.getValorOferta() < valorMinimoPermitidoo)
			throw new LeilaoException("Valor da oferta deve ser equivalente aominimo oferecido!");

		double valorAnterior = leilao.getLanceInicial();
		double incrementeReal = ofertaExistente.getValorOferta() - valorAnterior;

		for (Oferta oferta : leilao.getOfertas()) {
			if (oferta.getId().equals(ofertaExistente.getId()) && oferta.getStatusOferta() == StatusOferta.ATIVA) {

				oferta.setStatusOferta(StatusOferta.ACEITA);
				leilao.setComprador(oferta.getComprador());
				leilao.setValorDeIncremento(incrementeReal);

				ofertaAceita = true;
				ofertaAceitaFinal = oferta;
				break;
			} 
		}

		if (!ofertaAceita) {
			throw new IllegalArgumentException("Nenhuma oferta ativa encontrada para aceitar!");
		}

		ofertaRepository.saveAll(leilao.getOfertas());
		leilaoRepository.save(leilao);
		return MyMaper.parseObject(ofertaAceitaFinal, OfertaDTO.class);
	}

	@Override
	public OfertaDTO negarOfertaDeLeilao(Usuario usuarioLogado, Long LeilaoId, Long ofertaId) {
		UsuarioVendedor usuarioVendedor = (UsuarioVendedor) vendedorRepository.findById(usuarioLogado.getId())
				.orElseThrow(() -> new UsuarioNaoEncontradoException("Vendedor não encontradro!"));

		Leilao leilaoVendedor = usuarioVendedor.getLeilaoCadastrado().stream()
				.filter(l -> l.getVendedor().getId() == usuarioLogado.getId()).findFirst().orElse(null);

		Oferta ofertaNegada = null;
		for (Oferta o : leilaoVendedor.getOfertas()) {
			if (o.getId().equals(ofertaId) && o.getStatusOferta() == StatusOferta.ATIVA) {
				o.setStatusOferta(StatusOferta.NEGADA);
				ofertaNegada = ofertaRepository.save(o);
				break;
			}
		}

		return MyMaper.parseObject(ofertaNegada, OfertaDTO.class);
	}

	@Override
	public Set<OfertaDTO> findOfertasMaisCarasDeComprador(Usuario usuarioLogado, String cpfComprador,
			Double valorMinimo) {

		String cpfLogado = ((UsuarioComprador) usuarioLogado).getCpf();
		Set<Oferta> oferta = ofertaRepository.findOfertasMaisCarasDeComprador(cpfLogado, valorMinimo);
		if (oferta != null) {
			return MyMaper.parseSetObjects(oferta, OfertaDTO.class);
		} else {
			throw new IllegalArgumentException(
					"Não foi possivel achar nenhuma oferta sob esse valor pro comprador" + cpfComprador);
		}

	}

	@Override
	public Set<OfertaDTO> findOfertasMaisCarasRecebidasDeVendedor(Usuario usuarioLogado, String cnpjVendedor,
			Double valorMinimo) {

		String cnpjLogado = ((UsuarioVendedor) usuarioLogado).getCnpj(); // Pega o cnpj logado no contexto de Security
																			// com Cast pra UsuarioVendedor
		Set<Oferta> oferta = ofertaRepository.findOfertasMaisCarasDeVendedor(cnpjLogado, valorMinimo);
		if (!oferta.isEmpty()) {
			return MyMaper.parseSetObjects(oferta, OfertaDTO.class);
		} else {
			throw new IllegalArgumentException("Ofertas mais caras vazias!");
		}

	}

}
