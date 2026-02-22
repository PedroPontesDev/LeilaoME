package com.devPontes.LeialaoME.services.impl;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.exceptions.LeilaoEncerradoException;
import com.devPontes.LeialaoME.exceptions.LeilaoException;
import com.devPontes.LeialaoME.exceptions.UsuarioNaoEncontradoException;
import com.devPontes.LeialaoME.model.DTO.v1.LeilaoDTO;
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
import com.devPontes.LeialaoME.repositories.UsuarioRepositories;
import com.devPontes.LeialaoME.repositories.UsuarioVendedorRepositories;
import com.devPontes.LeialaoME.services.LeilaoServices;

import jakarta.transaction.Transactional;

@Service
public class LeilaoServicesImpl implements LeilaoServices {

	@Autowired
	private LeilaoRepositories leilaoRepository;

	@Autowired
	private UsuarioCompradorRepositories compradorRepository;

	@Autowired
	private UsuarioRepositories usuarioRepository;

	@Autowired
	private UsuarioVendedorRepositories vendedorRepositories;

	@Autowired
	private OfertaRepositories ofertaRepository;

	@Override
	public LeilaoDTO criarLeilao(LeilaoDTO novoLeilao, Usuario usuarioLogado) {
		Leilao leilao = MyMaper.parseObject(novoLeilao, Leilao.class);

		UsuarioVendedor vendedor = (UsuarioVendedor) usuarioRepository.findByUsername(usuarioLogado.getUsername())
				.orElseThrow(() -> new RuntimeException("Você não pode criar leilão para outro vendedor!"));

		// Validações\
		LocalDateTime agora = LocalDateTime.now();
		LocalDateTime limiteFuturo = agora.plusDays(2); // Exemplo: permite criar leilões para daqui a no máximo 7 dias

		// Se o início do leilão for depois do meu limite permitido (ex: 2 dias)

		if (leilao.getInicio().isBefore(agora)) {
			throw new IllegalArgumentException("Leilão não pode começar no passado");
		}
		if (leilao.getInicio().isAfter(limiteFuturo))
			throw new IllegalArgumentException("Leilão não pode ser agendado para mais de 2 dias no futuro");

		if (leilao.getLanceInicial() == null || leilao.getLanceInicial() <= 0)
			throw new IllegalArgumentException("Leilão deve começar com um valor de lance inicial válido.");

		if (leilao.getTermino().isBefore(leilao.getInicio()))
			throw new IllegalArgumentException("Data de término deve ser posterior à data de início");
		// Configurações
		leilao.setVendedor(vendedor);
		leilao.setAindaAtivo(true);
		vendedor.getLeilaoCadastrado().add(leilao);

		// Persistencia
		leilaoRepository.save(leilao);

		return MyMaper.parseObject(leilao, LeilaoDTO.class);
	}

	@Override
	public LeilaoDTO criarLeilaoFuturo(LeilaoDTO novoLeilao, LocalDateTime tempoInicio, LocalDateTime tempoFim,
			Usuario usuarioLogado) {
		Leilao leilao = MyMaper.parseObject(novoLeilao, Leilao.class);

		if (leilao == null)
			throw new IllegalArgumentException("Não contem nenhum dado no leilão.");

		UsuarioVendedor vendedor = (UsuarioVendedor) usuarioRepository.findById(usuarioLogado.getId())
				.orElseThrow(() -> new RuntimeException("Você não pode criar leilão para outro vendedor!"));

		if (tempoInicio == null || tempoFim == null)
			throw new IllegalArgumentException("As datas de início e término devem ser informadas.");

		if (leilao.getLanceInicial() == null)
			throw new IllegalArgumentException("Leilão deve começar com algum valor numérico!");

		if (tempoInicio.isBefore(LocalDateTime.now()))
			throw new IllegalArgumentException("Leilão deve começar num momento futuro!");

		if (tempoFim.isBefore(tempoInicio))
			throw new IllegalArgumentException("A data de término deve ser posterior ao início do leilão!");

		if (leilao.getLanceInicial() == null || leilao.getLanceInicial() <= 0)
			throw new IllegalArgumentException("Leilão deve começar com um valor de lance inicial válido.");

		leilao.setInicio(tempoInicio);
		leilao.setTermino(tempoFim);

		leilao.setVendedor(vendedor);
		vendedor.getLeilaoCadastrado().add(leilao);

		Leilao salvo = leilaoRepository.save(leilao);
		return MyMaper.parseObject(salvo, LeilaoDTO.class);
	}

	@Override
	@Transactional
	public LeilaoDTO definirGanhador(Long leilaoId, Usuario usuarioLogado) throws Exception {

		Leilao leilaoExistente = leilaoRepository.findById(leilaoId)
				.orElseThrow(() -> new LeilaoException("Leilão não encontrado"));

		boolean isAdmin = usuarioLogado
				.getAuthorities()
				.stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

		if (!isAdmin)
			throw new SecurityException("Você não é um administrador");

		if (!leilaoExistente.isAindaAtivo())
			throw new LeilaoException("Leilão já foi encerrado");

		Oferta ofertaGanhadora = leilaoExistente.getOfertas().stream()
				.filter(o -> o.getStatusOferta() == StatusOferta.ACEITA || o.getStatusOferta() == StatusOferta.ATIVA)
				.max(Comparator.comparing(Oferta::getValorOferta)).orElseThrow(null);

		UsuarioComprador vencedor = ofertaGanhadora.getComprador();

		for (Oferta ofertaAceita : leilaoExistente.getOfertas()) {
			if (ofertaAceita.equals(ofertaGanhadora)) {
				ofertaAceita.setStatusOferta(StatusOferta.GANHADORA);
			} else {
				ofertaAceita.setStatusOferta(StatusOferta.PERDEDORA);
			}
		}

		leilaoExistente.setAindaAtivo(false);
		leilaoExistente.setComprador(vencedor);
		leilaoRepository.save(leilaoExistente);

		var dto = MyMaper.parseObject(leilaoExistente, LeilaoDTO.class);
		dto.setVencedorId(vencedor.getId());
		return dto;
	}

	@Transactional
	public LeilaoDTO criarLeilaoReduzido(LeilaoDTO leilao, Long reducaoHoras, Usuario usuarioLogado) {
		Leilao entity = MyMaper.parseObject(leilao, Leilao.class);

		UsuarioVendedor vendedor = (UsuarioVendedor) usuarioRepository.findById(usuarioLogado.getId())
				.orElseThrow(() -> new SecurityException("Usuário inválido"));

		if (reducaoHoras == null || reducaoHoras <= 0) {
			throw new LeilaoException("Horas de redução inválidas");
		}

		if (entity.getTermino() == null)
			throw new DateTimeException("Leilão precisa ter data final base");

		// Calcula duração atual com objeto Duration
		LocalDateTime inicio = LocalDateTime.now();
		Duration duracaoAtual = Duration.between(inicio, entity.getTermino());
		LocalDateTime tempoCalculado = entity.getTermino().minusHours(reducaoHoras);

		if (reducaoHoras >= duracaoAtual.toHours()) {
			throw new LeilaoException("Redução maior ou igual à duração do leilão");
		}
		entity.setInicio(inicio);
		entity.setAindaAtivo(true);
		entity.setTermino(tempoCalculado);
		entity.setVendedor(vendedor);
		leilaoRepository.save(entity);
		return MyMaper.parseObject(entity, LeilaoDTO.class);
	}

	@Override
	public Set<OfertaDTO> visualizarOfertasDeLeilao(Usuario usuarioLogado, Long leilaoId) {
		Leilao leilao = leilaoRepository.findById(leilaoId)
				.orElseThrow(() -> new LeilaoException("Leilão não encontrado com ID" + leilaoId));

		UsuarioVendedor vendedor = (UsuarioVendedor) usuarioRepository.findById(usuarioLogado.getId())
				.orElseThrow(() -> new SecurityException("Usuário inválido"));

		var ofertas = leilao.getOfertas().stream().collect(Collectors.toSet());

		return MyMaper.parseSetObjects(ofertas, OfertaDTO.class);

	}

	@Override
	public List<LeilaoDTO> findLeilaoPorStatus(String status) {

		// 1. Validar status
		StatusOferta statusEnum;
		try {
			statusEnum = StatusOferta.valueOf(status.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(
					"Status inválido! Use: ATIVO,  INATIVA, PENDENTE, GANHADORA, PERDEDORA, ACEITA, NEGADA");
		}

		// 2. Buscar no banco
		List<Leilao> leiloes = leilaoRepository.findLeilaoPorStatus(statusEnum);

		if (leiloes.isEmpty())
			throw new LeilaoException("Nenhum leilão encontrado com status: " + statusEnum);

		// 3. Converter para DTO
		return MyMaper.parseListObjects(leiloes, LeilaoDTO.class);
	}

	@Override
	public Set<LeilaoDTO> findLeiloesFuturos(LocalDate proximoMes) throws Exception {

		LocalDateTime agora = LocalDateTime.now();
		LocalDateTime tempoLimite = proximoMes.atTime(23, 59, 29);

		Set<Leilao> leiloes = (Set<Leilao>) leilaoRepository.findAll();

		Set<Leilao> leilosFuturos = new HashSet<>();

		for (Leilao proximos : leiloes) {
			if (proximos.getInicio().isAfter(agora) && proximos.getInicio().isBefore(tempoLimite))
				leilosFuturos.add(proximos);
		}

		if (leilosFuturos.isEmpty())
			throw new LeilaoException("Nenhum leilão futuro encontrado.");
		return MyMaper.parseSetObjects(leilosFuturos, LeilaoDTO.class);

	}

	@Override
	@Transactional
	public void fecharLeilao(Long leilaoId) {

		Leilao leilaoExistente = leilaoRepository.findById(leilaoId)
				.orElseThrow(() -> new LeilaoException("Leilao não encontrado para o ID " + leilaoId));

		if (leilaoExistente.isAindaAtivo() == false)
			throw new LeilaoEncerradoException("Leilão já está fechado!");

		if (leilaoExistente.getTermino().isAfter(LocalDateTime.now())) {
			throw new LeilaoException("Leilao ainda não terminou! ");
		} 
		boolean existeOfertaAtiva = leilaoExistente.getOfertas().stream()
																.anyMatch(o -> o.getStatusOferta() == StatusOferta.ATIVA);
		
		if (!existeOfertaAtiva) {
			throw new LeilaoException("Nenhuma oferta ativa para fechar o leilão");
		}
		
		Oferta ofertaMaisAlta = leilaoExistente.getOfertas()
											   .stream()
											   .filter(o -> o.getStatusOferta() == StatusOferta.ATIVA)
											   .max(Comparator.comparingDouble(o -> o.getValorOferta()))
											   .orElseThrow(() -> new LeilaoException("Não foram encontradas ofertas ATIVAS ou com valores máximos"));
		ofertaMaisAlta.setStatusOferta(StatusOferta.ACEITA);
		
		for(Oferta oferta : leilaoExistente.getOfertas()) {
			if(oferta != ofertaMaisAlta) {
				oferta.setStatusOferta(StatusOferta.INATIVA);
			}
		}
		
		leilaoExistente.setAindaAtivo(false);

		leilaoRepository.save(leilaoExistente);

	}

	@Override
	public List<LeilaoDTO> findAll() {
		var all = leilaoRepository.findAll().stream().limit(50).collect(Collectors.toList());

		return MyMaper.parseListObjects(all, LeilaoDTO.class);
	}

	// Carregar todos os leilos de compradoresres e vendroresres

	@Override
	public List<LeilaoDTO> findLeiloesDeUsuarioComprador(Usuario usuarioLogado, String cpf, Long leilãoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LeilaoDTO> findLeiloesDeUsuarioVendedor(Usuario usuarioLogado, String cnpj, Long leilãoId) {
		// TODO Auto-generated method stub
		return null;
	}

}
