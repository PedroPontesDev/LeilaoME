package com.devPontes.LeialaoME.services.impl;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
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
import com.devPontes.LeialaoME.repositories.UsuarioRepositories;
import com.devPontes.LeialaoME.services.UsuarioCompradorService;
import com.devPontes.LeialaoME.utils.CnpjCpfValidadorClient;

import jakarta.transaction.Transactional;

@Service
public class UsuarioCompradorServicesImpl implements UsuarioCompradorService {

	@Autowired
	private UsuarioRepositories usuarioRepositories;

	@Autowired
	private UsuarioCompradorRepositories usuarioRepository;

	@Autowired
	private PermissaoRepositories permissaoRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	OfertaServicesImpl ofertaServices;

	private static final Logger log = LoggerFactory.getLogger(UsuarioCompradorServicesImpl.class);

	// Define a pasta de upload (pode ser qualquer pasta que o usuário atual tenha
	// permissão)
	private final String uploadDir = System.getProperty("user.home") + File.separator + "uploads";

	@Override
	@Transactional
	public UsuarioDTO cadastrarUsuarioComprador(UsuarioDTO novoUsuario) throws Exception {

		UsuarioComprador user = MyMaper.parseObject(novoUsuario, UsuarioComprador.class);

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

	/**
	 * Faz upload de imagem de perfil do usuário.
	 * 
	 * @param userId id do usuário (pode ser usado para associar a imagem)
	 * @param file   MultipartFile enviado pelo frontend
	 * @return Map com informações do upload
	 * @throws Exception caso algo dê errado
	 */
	@Transactional
	public Map<String, Object> fazerUploadDeImamgemDePerfil(Long userId, MultipartFile file) throws Exception {
		var entidade = usuarioRepository.findById(userId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não enontrado com ID" + userId));

		// 1️ Verifica se o arquivo está vazio
		if (file.isEmpty()) {
			throw new RuntimeException("Arquivo vazio!");
		}

		// 2️ Valida o tipo do arquivo
		String contentType = file.getContentType();
		if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
			throw new Exception("Servidor só pode consumir imagens JPG ou PNG!");
		}

		// 3️ Gera um nome único para o arquivo, evitando sobrescrita
		String nomeArquivo = UUID.randomUUID() + "_FRONTEND_  " + "_"
				+ file.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-]", "_"); // remove

		// 4️ Cria a pasta de upload se não existir COM OBJETO fiLE
		File pasta = new File(uploadDir);
		if (!pasta.exists()) {
			pasta.mkdirs(); // cria a pasta e subpastas
		}

		// 5️ Cria o Path completo do arquivo com objeto Path e Paths!!
		Path caminhoArquivo = Paths.get(pasta.getAbsolutePath(), nomeArquivo);

		// 6️ Abre InputStream do MultipartFile e OutputStream do Path com file vindo do
		// frontend e Files.outPut do objeto java
		try (InputStream input = file.getInputStream(); OutputStream output = Files.newOutputStream(caminhoArquivo)) {

			byte[] buffer = new byte[8192]; // lê 8KB por vez
			int bytesLidos;
			while ((bytesLidos = input.read(buffer)) != -1) {
				output.write(buffer, 0, bytesLidos); // escreve no disco passando da posição zero até o final qu ter bufferlidos
						
			}
		}

		// cria URL relativa para o frontend acessar a imagem
		String urlRelativa = "/uploads/" + nomeArquivo;
		entidade.setUrlFotoPerfil(urlRelativa);

		// Retorna informações do arquivo para o frontend
		Map<String, Object> response = new HashMap<>();
		response.put("message", "Upload concluído com sucesso!");
		response.put("url", urlRelativa);
		response.put("fileName", file.getOriginalFilename());
		response.put("size", file.getSize()); // tamanho em bytes

		log.info("Upload feito para usuário {} -> {}", userId, urlRelativa + "Para Onjeto/Entidade: " + entidade);

		usuarioRepositories.save(entidade);

		return response;
	}

	@Override
	@Transactional
	public UsuarioDTO atualizarUsuarioComprador(UsuarioDTO update, Long usuarioId) {
		var entidade = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não enontrado com ID" + usuarioId));

		if (update.getBiografia() != null)
			entidade.setBiografia(update.getBiografia());

		if (update.getPassword() != null)
			entidade.setPassword(encoder.encode(update.getPassword()));

		if (update.getUsername() != null)
			entidade.setUsername(update.getUsername());

		if (update.getFotoPerfil() != null)
			entidade.setUrlFotoPerfil(update.getFotoPerfil());

		// Salva as alterações
		UsuarioComprador salvo = (UsuarioComprador) usuarioRepository.save(entidade);

		// Retorna o DTO atualizado
		return MyMaper.parseObject(salvo, UsuarioDTO.class);

	}

	@Override
	@Transactional
	public String escreverBiografia(Long usuarioId, String biografia) {
		var entidade = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não enontrado com ID" + usuarioId));

		if (entidade.getBiografia() != null)
			entidade.setBiografia(biografia);

		// Salva as alterações
		UsuarioComprador salvo = (UsuarioComprador) usuarioRepository.save(entidade);

		return entidade.getBiografia();
	}

	@Override
	@Transactional
	public String atualizarUsername(Long usuarioId, String usernameNovo) {
		var entidade = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não enontrado com ID" + usuarioId));

		if (entidade.getUsername() != null)
			entidade.setUsername(usernameNovo);

		// Salva as alterações
		UsuarioComprador salvo = (UsuarioComprador) usuarioRepository.save(entidade);

		return entidade.getUsername();
	}

	@Override
	@Transactional
	public String atualizarPassword(Long usuarioId, String passwordNova) {
		throw new UnsupportedOperationException("Não implementado ainda");
	}

	@Override
	public Set<OfertaDTO> findOfertasMaisCaras(String cpfComprador, Double minimumValue) {
		var ofertaMaisCaras = ofertaServices.findOfertasMaisCarasDeComprador(cpfComprador, minimumValue);
		return ofertaMaisCaras;

	}

	@Override
	public Set<LeilaoDTO> findLeiloesAdquiridosDeUsuario(Long usuarioCompradorId) {
		// TODO: mostrar historico de leiloes adquiridos por id de usuario
		throw new UnsupportedOperationException("Não implementado ainda");
	}

	@Override
	public Set<OfertaDTO> findOfertaMaisBaixa(String cpfComprador, Double maximumValue) {
		// TODO Auto-generated method stub
		return null;
	}

}
