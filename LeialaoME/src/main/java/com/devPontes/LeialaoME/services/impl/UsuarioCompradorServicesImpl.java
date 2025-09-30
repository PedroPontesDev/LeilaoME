package com.devPontes.LeialaoME.services.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.devPontes.LeialaoME.exceptions.UsuarioNaoEncontradoException;
import com.devPontes.LeialaoME.model.dto.LeilaoDTO;
import com.devPontes.LeialaoME.model.dto.OfertaDTO;
import com.devPontes.LeialaoME.model.dto.PermissaoDTO;
import com.devPontes.LeialaoME.model.dto.UsuarioDTO;
import com.devPontes.LeialaoME.model.entities.Permissao;
import com.devPontes.LeialaoME.model.entities.UsuarioComprador;
import com.devPontes.LeialaoME.model.entities.enums.UsuarioRole;
import com.devPontes.LeialaoME.model.entities.mapper.MyMaper;
import com.devPontes.LeialaoME.repositories.PermissaoRepositories;
import com.devPontes.LeialaoME.repositories.UsuarioCompradorRepositories;
import com.devPontes.LeialaoME.services.UsuarioCompradorService;
import com.devPontes.LeialaoME.utils.CnpjCpfValidadorClient;

@Service
public class UsuarioCompradorServicesImpl implements UsuarioCompradorService {

	@Autowired
	private UsuarioCompradorRepositories usuarioRepository;

	@Autowired
	private PermissaoRepositories permissaoRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	private static final Logger log = LoggerFactory.getLogger(UsuarioCompradorServicesImpl.class);

	private final String nameDir = "C:\\Users\\user\\Documents\\dumps\\uploads"; // pasta relativa dentro do projeto

	@Override
	public UsuarioDTO cadastrarUsuarioComprador(UsuarioDTO novoUsuario) throws Exception {
		UsuarioComprador user = MyMaper.parseObject(novoUsuario, UsuarioComprador.class);
		user.setUrlFotoPerfil(novoUsuario.getFotoPerfil());

		if (!CnpjCpfValidadorClient.validarCPf(user.getCpf())) {
			throw new Exception("CPF Não Pode Ser Validado como CPF");
		}

		Permissao roleComprador = permissaoRepository.findByUsuarioRole(UsuarioRole.ROLE_COMPRADOR);
		if (roleComprador == null)
			throw new RuntimeException("Permissão ROLE_COMPRADOR não encontrada no banco!");

		user.getPermissoes().add(roleComprador);
		user.setPassword(encoder.encode(user.getPassword()));

		UsuarioComprador salvo = usuarioRepository.save(user);

		UsuarioDTO dto = MyMaper.parseObject(salvo, UsuarioDTO.class);
		dto.setPermissoes(user.getPermissoes().stream().map(p -> new PermissaoDTO(UsuarioRole.ROLE_COMPRADOR))
				.collect(Collectors.toSet()));

		log.info("Usuário cadastrado com sucesso: {}", user.getUsername());
		return dto;
	}

	@Override
	public String fazerUploadDeImamgemDePerfil(Long userId, MultipartFile file) throws Exception {
		UsuarioComprador usuarioExistente = (UsuarioComprador) usuarioRepository.findById(userId).orElseThrow(
				() -> new UsuarioNaoEncontradoException("Não foi possível carregar a foto deste usuário!"));

		if (file.isEmpty()) {
			throw new RuntimeException("Arquivo vazio!");
		}

		// UUID = Gera unicidade de elemntos no aso nome único do arquivo
		String nomeArquivo = UUID.randomUUID() + "_" + file.getOriginalFilename();

		// Cria diretório se não existir
		File pasta = new File(nameDir);
		if (!pasta.exists())
			pasta.mkdirs();

		// Objeto Path = Path definitivo para a pasta criada
		Path destino = Paths.get(pasta.getAbsolutePath(), nomeArquivo);

		// Salva o arquivo
		try (InputStream is = file.getInputStream()) {
			Files.copy(is, destino);
			log.info("Upload concluído. Arquivo copiado: {}", Files.copy(is, destino));
		}
		log.info("Arquivo salvo em: {}", destino.toString());
		
		URI url = URI.create("localhost:8080/".concat(nomeArquivo));

		usuarioExistente.setUrlFotoPerfil(url.getPath());   // Salva apenas o nome do arquivo no banco mas vamos tentar salvar url
		usuarioRepository.save(usuarioExistente);
		return nomeArquivo;
	}

	@Override
	public UsuarioDTO atualizarUsuarioComprador(UsuarioDTO update, Long usuarioId) {
		throw new UnsupportedOperationException("Não implementado ainda");
	}

	@Override
	public String escreverBiografia(Long usuarioId, String biografia) {
		throw new UnsupportedOperationException("Não implementado ainda");
	}

	@Override
	public String atualizarUsername(Long usuarioId, String usernameNovo) {
		throw new UnsupportedOperationException("Não implementado ainda");
	}

	@Override
	public String atualizarPassword(Long usuarioId, String passwordNova) {
		throw new UnsupportedOperationException("Não implementado ainda");
	}

	@Override
	public Set<OfertaDTO> findOfertasMaisCaras(Long userId, Double minimumValue) {
		//TODO: Achar as ofertas mais caras dadas por um comprador por SQL
		
		throw new UnsupportedOperationException("Não implementado ainda");
	}
	
	public OfertaDTO findOfertaMaisBaixa(Long userId, Double maximumValue) {
		//TODO: Achar as ofertas mais caras dadas por um comprador por SQL
		
		throw new UnsupportedOperationException("Não implementado ainda");
	}
	

	@Override
	public Set<LeilaoDTO> findLeiloesAdquiridosDeUsuario(Long usuarioCompradorId) {
		//TODO: mostrar historico de leiloes adquiridos por id de usuario 
		throw new UnsupportedOperationException("Não implementado ainda");
	}


	
}
