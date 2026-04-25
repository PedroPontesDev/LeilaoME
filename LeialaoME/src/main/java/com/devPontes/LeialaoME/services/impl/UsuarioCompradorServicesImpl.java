package com.devPontes.LeialaoME.services.impl;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.devPontes.LeialaoME.exceptions.UsuarioNaoEncontradoException;
import com.devPontes.LeialaoME.integrations.CnpjCpfValidadorClient;
import com.devPontes.LeialaoME.model.DTO.v1.PermissaoDTO;
import com.devPontes.LeialaoME.model.DTO.v1.UsuarioDTO;
import com.devPontes.LeialaoME.model.DTO.v1.UsuarioUpdateDTO;
import com.devPontes.LeialaoME.model.entities.Permissao;
import com.devPontes.LeialaoME.model.entities.Usuario;
import com.devPontes.LeialaoME.model.entities.UsuarioComprador;

import com.devPontes.LeialaoME.model.entities.enums.UsuarioRole;
import com.devPontes.LeialaoME.model.entities.mapper.MyMaper;
import com.devPontes.LeialaoME.repositories.PermissaoRepositories;
import com.devPontes.LeialaoME.repositories.UsuarioCompradorRepositories;
import com.devPontes.LeialaoME.repositories.UsuarioRepositories;
import com.devPontes.LeialaoME.services.UsuarioCompradorService;

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
	private CnpjCpfValidadorClient cpfValidator;

	private static final Logger log = LoggerFactory.getLogger(UsuarioCompradorServicesImpl.class);

	// Define a pasta de upload (pode ser qualquer pasta que o usuário atual tenha permissão)
	private final String uploadDir = System.getProperty("user.home") + File.separator + "uploads";
	
	@Override
	public UsuarioDTO cadastrarUsuarioComprador(UsuarioDTO novoUsuario) throws Exception {
		UsuarioComprador user = MyMaper.parseObject(novoUsuario, UsuarioComprador.class);

		if (!cpfValidator.validarCPf(user.getCpf())) {
			throw new Exception("CPF Não Pode Ser Validado como CPF");
		}

		Permissao roleComprador = permissaoRepository.findByUsuarioRole(UsuarioRole.ROLE_COMPRADOR);
		if (roleComprador == null)
			throw new RuntimeException("Permissão ROLE_COMPRADOR não encontrada no banco!");

		user.getPermissoes().add(roleComprador);
		user.setPassword(encoder.encode(user.getPassword()));

		UsuarioComprador salvo = usuarioRepository.save(user);

		UsuarioDTO dto = MyMaper.parseObject(salvo, UsuarioDTO.class);
		
		dto.setPermissoes(salvo.getPermissoes()
											.stream()
											.map(p -> new PermissaoDTO(p.getUsuarioRole()))
											.collect(Collectors.toSet()));

		log.info("Usuário cadastrado com sucesso: {}", user.getCpf());
		return dto;
	}

	/**
	 * Faz upload de imagem de perfil do usuário.
	 * @param file   MultipartFile enviado pelo frontend
	 * @return Map com informações do upload
	 * @throws Exception caso algo dê errado
	 */
	@Transactional
	public Map<String, Object> fazerUploadDeImamgemDePerfil(Usuario usuarioLogado, MultipartFile file) throws Exception {
		var entidade = usuarioRepository.findById(usuarioLogado.getId())
				.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não enontrado com ID" + usuarioLogado.getId()));
		
		if (file.isEmpty()) {
			throw new RuntimeException("Arquivo vazio!");
		}

		String contentType = file.getContentType();
		if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
			throw new Exception("Servidor só pode consumir imagens JPG ou PNG!");
		}
		
		String nomeOriginal = file.getOriginalFilename();
	    if (nomeOriginal == null || 
	        !(nomeOriginal.toLowerCase().endsWith(".jpg") || 
	          nomeOriginal.toLowerCase().endsWith(".jpeg") || 
	          nomeOriginal.toLowerCase().endsWith(".png"))) {
	        throw new RuntimeException("Extensão inválida! Apenas .jpg, .jpeg ou .png"); //
	    }

		String nomeArquivo = UUID.randomUUID() + "_"
				+ file.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-]", "_"); // remove

		File pasta = new File(uploadDir); //Passar diretorio que sera criado o arquivo
		
		if (!pasta.exists()) {
			pasta.mkdir();
		}

		Path caminhoArquivo = Paths.get(uploadDir, nomeArquivo);

		// Abre InputStream do MultipartFile e OutputStream do Path com file vindo do frontend e Files.outPut do objeto java
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

		log.info("Upload feito para usuário {} -> {}", usuarioLogado.getId(), urlRelativa + "Para Objeto/Entidade: " + entidade);

		usuarioRepositories.save(entidade);

		return response;
		

		// 1️ Verifica se o arquivo está vazio
		// 2️ Valida o tipo MIME do arquivo
		// 3️ Gera um nome único para o arquivo, evitando sobrescrita
		// 4️ Cria a pasta de upload se não existir COM OBJETO FILE e COM DIRERTORIO DO ARQUIVO
		// 5️ Cria o Path completo do arquivo com objeto Path e Paths!!
	}

	@Override
	@Transactional
	public UsuarioDTO atualizarUsuarioComprador(UsuarioUpdateDTO update, Long usuarioId) throws Exception {
		var entidade = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não enontrado com ID" + usuarioId));

		if (update.biografia() != null)
			entidade.setBiografia(update.biografia());

		// Atualização de senha
		if (update.newPassword() != null && update.oldPassword() != null) {
			if (update.newPassword().length() < 6) {
				throw new Exception("A nova senha deve conter pelo menos 6 carcters!");
			}

			if (!encoder.matches(update.oldPassword(), entidade.getPassword())) {
				throw new Exception("A senha antiga é incorreta !");
			}

			entidade.setPassword(encoder.encode(update.newPassword()));
		}

		if (update.username() != null)
			entidade.setUsername(update.username());

		UsuarioComprador salvo = (UsuarioComprador) usuarioRepository.save(entidade);

		// Retorna o DTO atualizado
		return MyMaper.parseObject(salvo, UsuarioDTO.class);

	}

	@Override
	@Transactional
	public String escreverBiografia(Long usuarioId, String biografia) {
		var entidade = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não enontrado com ID" + usuarioId));

		if (entidade.getBiografia() == null)
			entidade.setBiografia(biografia);

		// Salva as alterações
		usuarioRepository.save(entidade);

		return entidade.getBiografia();
	}

	@Override
	@Transactional
	public String atualizarUsername(Long usuarioId, String usernameNovo) {
		var entidade = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não enontrado com ID" + usuarioId));
		
		validarUsername(usernameNovo);
		
		entidade.setUsername(usernameNovo);

		usuarioRepository.save(entidade);

		return entidade.getUsername();
	}

	@Override
	@Transactional
	public String atualizarPassword(Long usuarioId, String passwordNova, String oldPassword) throws Exception {
		UsuarioComprador usuario =  (UsuarioComprador) usuarioRepositories.findById(usuarioId)
											           .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado com ID" + usuarioId));
		
		if (passwordNova != null && oldPassword != null) {
			if (passwordNova.length() < 6) {
				throw new Exception("A nova senha deve conter pelo menos 6 carcters!");
			}

			if (!encoder.matches(oldPassword, usuario.getPassword())) {
				throw new Exception("A senha antiga é incorreta !");
			}

			usuario.setPassword(encoder.encode(passwordNova));
		}
		
		return passwordNova;

	}
	
	public void validarUsername(String username) {
		if(username == null || username.isBlank()) {
			throw new IllegalArgumentException("Username não pode ser vazio");
		}
		
		if(username.length() < 5 || username.length() > 20) {
			throw new IllegalArgumentException("Username deve ter entre 5 e 20 caracteres");
		}
		
		String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).+$";
		
		if(!username.matches(regex)) {
		   throw new IllegalArgumentException("Username deve conter letra maiúscula, minúscula e caractere especial");
		}
		
	}

}
