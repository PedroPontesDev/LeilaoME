package com.devPontes.LeialaoME.services.impl;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.exceptions.LeilaoEncerradoException;
import com.devPontes.LeialaoME.exceptions.LeilaoException;
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
import com.devPontes.LeialaoME.repositories.UsuarioRepositories;
import com.devPontes.LeialaoME.services.LeilaoServices;

import jakarta.transaction.Transactional;

@Service
public class LeilaoServicesImpl implements LeilaoServices {

	@Autowired
	private LeilaoRepositories leilaoRepository;
	
	@Autowired
	private UsuarioRepositories usuarioRepository;


	@Override
	public LeilaoDTO criarLeilao(LeilaoDTO novoLeilao, Usuario usuarioLogado) {

		Leilao leilao = MyMaper.parseObject(novoLeilao, Leilao.class);

		UsuarioVendedor vendedor = (UsuarioVendedor) usuarioRepository.findByUsername(usuarioLogado.getUsername())
				.orElseThrow(() -> new RuntimeException("Você não pode criar leilão para outro vendedor!"));

		LocalDateTime agora = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
		
		leilao.setValorDeIncremento(novoLeilao.getValorDeIncremento());
		leilao.setLanceInicial(novoLeilao.getLanceInicial()) ;
		
		//  Validação de datas
		if (leilao.getInicio() == null || leilao.getTermino() == null) {
			throw new IllegalArgumentException("Datas inválidas ou formato incorreto");
		}

		if (leilao.getInicio().isBefore(agora)) {
			throw new IllegalArgumentException("Leilão não pode começar no passado");
		}

		if (leilao.getInicio().isAfter(agora.plusDays(4))) {
			throw new IllegalArgumentException("Leilão só pode ser criado com até 4 dias de antecedência");
		}

		if (leilao.getTermino().isBefore(leilao.getInicio())) {
			throw new IllegalArgumentException("Data de término deve ser antes do início");
		}

		//  Validação de valores
		if (leilao.getLanceInicial() == null || leilao.getLanceInicial() <= 0) {
			throw new IllegalArgumentException("Lance inicial inválido");
		}

		if (leilao.getValorDeIncremento() == null || leilao.getValorDeIncremento() <= 0) {
			throw new IllegalArgumentException("Valor de incremento deve ser maior que zero");
		}

		// Configuração correta
		leilao.setVendedor(vendedor);
		leilao.setAindaAtivo(true);

		vendedor.getLeilaoCadastrado().add(leilao);

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

		if (leilao.getLanceInicial() == null)
			throw new IllegalArgumentException("Leilão deve começar com algum valor numérico!");
		
		if (tempoInicio == null || tempoFim == null)
			throw new IllegalArgumentException("As datas de início e término devem ser informadas.");

		if (tempoInicio.isBefore(LocalDateTime.now()))
			throw new IllegalArgumentException("Leilão deve começar num momento futuro!");

		if (tempoFim.isBefore(tempoInicio))
			throw new IllegalArgumentException("A data de término deve ser posterior ao início do leilão!");

		if (tempoInicio.isBefore(tempoFim))
			throw new IllegalArgumentException("A data de inicio deve ser posterior ao termino do leilão!");
		
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

	    boolean isAdmin = usuarioLogado.getAuthorities()
	            .stream()
	            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

	    if (!isAdmin && !leilaoExistente.getVendedor().getId().equals(usuarioLogado.getId()))  throw new SecurityException("Você não é um administrador ou dono do leilão");
	    if (leilaoExistente.isAindaAtivo())  throw new LeilaoException("Leilão ainda em andamento horario de termino:" + leilaoExistente.getTermino());
	
	    Double max = 0D;
	    Oferta ofertaGanhadora = null;

	    for(Oferta oferta : leilaoExistente.getOfertas()) {  	    //Priozar a aceita para ser justo??
	    	if(oferta.getStatusOferta() == StatusOferta.ACEITA) {
	    		ofertaGanhadora = oferta;
	    		break;
	    	}
	    }
	    
	    if(ofertaGanhadora == null)
	    for(Oferta oferta : leilaoExistente.getOfertas()) {
	    	if(oferta.getStatusOferta() == StatusOferta.ATIVA || oferta.getStatusOferta() == StatusOferta.ACEITA) {
	    		if(oferta.getValorOferta() > max) {
	    			max = oferta.getValorOferta();
	    			ofertaGanhadora = oferta;
	    		}
	    	}
	    }
	    
	    
	    //Desativar outras ofertas
	    for(Oferta oferta : leilaoExistente.getOfertas()) {
	    	if(oferta.equals(ofertaGanhadora)) {
	    		oferta.setStatusOferta(StatusOferta.GANHADORA);
	    	} else if(oferta.getStatusOferta() == StatusOferta.ATIVA
	                || oferta.getStatusOferta() == StatusOferta.ACEITA) {
	    		oferta.setStatusOferta(StatusOferta.PERDEDORA);
	    	}
	    }
	    					
	    UsuarioComprador ganhador = ofertaGanhadora.getComprador();
	    
	    leilaoExistente.setAindaAtivo(false);
	    leilaoExistente.setComprador(ganhador);
	    leilaoRepository.save(leilaoExistente);

	    LeilaoDTO dto = MyMaper.parseObject(leilaoExistente, LeilaoDTO.class);
	    dto.setVencedorId(ganhador.getId());

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

		if(vendedor.getId() != leilao.getVendedor().getId()) {
			throw new LeilaoException("Esse leilão não pertence a este vendedor");
		}
		
		var ofertas = leilao.getOfertas().stream().collect(Collectors.toSet());

		return MyMaper.parseSetObjects(ofertas, OfertaDTO.class);

	}

	@Override
	public List<LeilaoDTO> findLeilaoPorStatus(String status) {
		StatusOferta statusEnum;
		try {
			statusEnum = StatusOferta.valueOf(status.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(
					"Status inválido! Use: ATIVO,  INATIVA, PENDENTE, GANHADORA, PERDEDORA, ACEITA, NEGADA");
		} // 1. Validar status
 
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

		Set<Leilao> leiloes = leilaoRepository.buscarLeiloesFuturosAteData(agora, tempoLimite);
		
		if (leiloes.isEmpty())
			throw new LeilaoException("Nenhum leilão futuro encontrado.");
		
		return MyMaper.parseSetObjects(leiloes, LeilaoDTO.class);

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
		leilaoExistente.setAindaAtivo(false);
		leilaoRepository.save(leilaoExistente);

	}

	@Override
	public List<LeilaoDTO> findAll() {
		var all = leilaoRepository
				.findAll()
				.stream()
				.limit(50).collect(Collectors.toList());

		return MyMaper.parseListObjects(all, LeilaoDTO.class);
	}

	// Carregar todos os leilos de compradoresres e vendroresres

	@Override
	public Set<LeilaoDTO> findLeiloesDeUsuarioComprador(Usuario usuarioLogado, String cpf, Long leilãoId) {
		UsuarioComprador usuario = ((UsuarioComprador) usuarioLogado);
		Set<Leilao> leiloesDoUsuario = usuario.getLeiloesPaticipados();
		return MyMaper.parseSetObjects(leiloesDoUsuario, LeilaoDTO.class);
	}

	@Override
	public List<LeilaoDTO> findLeiloesDeUsuarioVendedor(Usuario usuarioLogado, String cnpj, Long leilãoId) {
		// TODO Auto-generated method stub
		return null;
	}

}
