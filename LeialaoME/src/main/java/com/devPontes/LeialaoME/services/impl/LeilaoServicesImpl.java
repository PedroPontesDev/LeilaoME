package com.devPontes.LeialaoME.services.impl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.exceptions.LeilaoEncerradoException;
import com.devPontes.LeialaoME.exceptions.LeilaoException;
import com.devPontes.LeialaoME.model.DTO.LeilaoDTO;
import com.devPontes.LeialaoME.model.entities.Leilao;
import com.devPontes.LeialaoME.model.entities.Oferta;
import com.devPontes.LeialaoME.model.entities.Usuario;
import com.devPontes.LeialaoME.model.entities.UsuarioComprador;
import com.devPontes.LeialaoME.model.entities.UsuarioVendedor;
import com.devPontes.LeialaoME.model.entities.enums.StatusOferta;
import com.devPontes.LeialaoME.model.entities.enums.UsuarioRole;
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
			
		//Validações
	    LocalDateTime agora = LocalDateTime.now();
	    LocalDateTime limiteFuturo = agora.plusDays(1); // exemplo: só permite criar para hoje ou amanhã

	    if (leilao.getInicio().isBefore(agora))
	        throw new IllegalArgumentException("Leilão não pode iniciar no passado!");

	    if (leilao.getInicio().isAfter(limiteFuturo))
	        throw new IllegalArgumentException("Leilão não pode iniciar tão no futuro!");

	    if (leilao.getTermino().isBefore(leilao.getInicio()))
	        throw new IllegalArgumentException("A data de término deve ser posterior à data de início.");

	    if (leilao.getLanceInicial() == null || leilao.getLanceInicial() <= 0) 
	        throw new IllegalArgumentException("Leilão deve começar com um valor de lance inicial válido.");

		
		//Configurações
		leilao.setVendedor(vendedor);
		leilao.setAindaAtivo(true);
		vendedor.getLeilaoCadastrado().add(leilao);
		
		//Persistencia
		leilaoRepository.save(leilao);
		
		
		return MyMaper.parseObject(leilao, LeilaoDTO.class);
	}

	@Override
	public LeilaoDTO criarLeilaoFuturo(LeilaoDTO novoLeilao, LocalDateTime tempoInicio, LocalDateTime tempoFim, Usuario usuarioLogado) {
		Leilao leilao = MyMaper.parseObject(novoLeilao, Leilao.class);

		if (leilao == null)
			throw new IllegalArgumentException("Não contem nenhum dado no leilão.");

		UsuarioVendedor vendedor = (UsuarioVendedor) usuarioRepository.findById(usuarioLogado.getId())
				.orElseThrow(() -> new RuntimeException("Você não pode criar leilão para outro vendedor!"));
				
		
		if (!leilao.isAindaAtivo())
			throw new LeilaoEncerradoException("Leilão já se encontra encerrado!");

		if (tempoInicio == null || tempoFim == null)
			throw new IllegalArgumentException("As datas de início e término devem ser informadas.");

		if (leilao.getLanceInicial() == null)
			throw new IllegalArgumentException("Leilão deve começar com algum valor numérico!");

		if (tempoInicio.isBefore(LocalDateTime.now()))
			throw new IllegalArgumentException("Leilão eve começar num momento futuro!");

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
				.orElseThrow(() -> new LeilaoException("Vendedor não encontrado com o ID" + leilaoId));

		//SOMENTE ADMINS PODEM EXECUTAR ESSE METODO
		boolean isAdmin = usuarioLogado
							.getPermissoes()
							.stream()
							.anyMatch(p -> p.getUsuarioRole().name().equals("ROLE_ADMIN"));
		
		if(!isAdmin) {
			throw new SecurityException("Somente Admins podem definir ganhador");
		}
		
		// Fazer novas validações como verificar se ganhou no tempo final
	
		if(LocalDateTime.now().isBefore(leilaoExistente.getTermino())) 
			throw new LeilaoException("O horario leilão não foi encerrado!");
		

		// Garantir que existem ofertas
		if (leilaoExistente.getOfertas() == null && leilaoExistente.getOfertas().isEmpty()) {
			throw new LeilaoException("Nenhuma oferta encontrada neste leilão.");
		}
		
		//Verificar se leilao esta encerrador pra defirnir ganhador

		Oferta maiorOferta = leilaoExistente.getOfertas().stream()
				.filter(o -> o.getStatusOferta() == StatusOferta.ATIVA)
				.filter(o -> o.getValorOferta() != null)
				.max(Comparator.comparing(Oferta::getValorOferta).thenComparing(o -> o.getComprador().getId())).get(); // Tratar
																														// erros
																														// depos

		UsuarioComprador vencedor = maiorOferta.getComprador();
		maiorOferta.setComprador(vencedor);
		leilaoExistente.setComprador(vencedor);

		leilaoRepository.save(leilaoExistente);

		// Verfica a maior oferta dentro do leilao, verificar se esta ativo
		// e setar o ganhador verificando qual é a oferta mais cara baseado
		// Nas ofertas dadas pelos compradores

		var vencedorDTO = MyMaper.parseObject(leilaoExistente, LeilaoDTO.class);
		vencedorDTO.setVencedorId(vencedor.getId());
		return vencedorDTO;
	}
	
	@Override 
	public LeilaoDTO abrirLeilaoComPoucaMargemDeTempo(LeilaoDTO leilao, Long tempoMinimoHoras, Usuario usuarioLogado) {

		Leilao entity = MyMaper.parseObject(leilao, Leilao.class);

		if (!entity.isAindaAtivo())
			throw new LeilaoException("Leilão está inativo!");

		// Calcula duração atual com objeto Duration
		Duration duracaoAtual = Duration.between(entity.getInicio(), entity.getTermino());

		// Se o leilão já é menor que o mínimo, lança exceção
		if (duracaoAtual.toHours() < tempoMinimoHoras)
			throw new LeilaoException("O leilão não possui margem/tempo para abrir.");

		// Se duracaoAtual do leilao for maior que tempo minimo esperado em horas eu
		// seto o tempo
		// calculado
		LocalDateTime tempoCalculado = entity.getTermino().minusHours(tempoMinimoHoras);
		if (duracaoAtual.toHours() > tempoMinimoHoras) {
			entity.setTermino(tempoCalculado);
		}

		leilaoRepository.save(entity);

		return MyMaper.parseObject(entity, LeilaoDTO.class);
	}

	
	@Override
	public LeilaoDTO verificarEstadoDeLeilao(Long leilaoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<LeilaoDTO> findLeiloesFuturos(LocalDate proximoMes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fecharLeilao(Long leilaoId, String statusOferta) {

	}

	@Override
	public List<LeilaoDTO> findAll() {
		var all = leilaoRepository.findAll()
					.stream()
					.limit(50)
					.collect(Collectors.toList());
		
		return MyMaper.parseListObjects(all, LeilaoDTO.class);
	}

	@Override
	public List<LeilaoDTO> findLeiloesDeUsuarioLogado(Usuario usuarioLogado, String cnpj, Long leilãoId) {
		// TODO Auto-generated method stub
		return null;
	}


}
