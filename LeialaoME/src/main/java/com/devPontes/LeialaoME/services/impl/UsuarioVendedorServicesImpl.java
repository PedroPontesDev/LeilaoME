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
import com.devPontes.LeialaoME.integrations.CnpjCpfValidadorClient;
import com.devPontes.LeialaoME.model.DTO.v1.LeilaoDTO;
import com.devPontes.LeialaoME.model.DTO.v1.OfertaDTO;
import com.devPontes.LeialaoME.model.DTO.v1.PermissaoDTO;
import com.devPontes.LeialaoME.model.DTO.v1.UsuarioDTO;
import com.devPontes.LeialaoME.model.DTO.v1.UsuarioUpdateDTO;
import com.devPontes.LeialaoME.model.DTO.v1.UsuarioVendedorDTO;
import com.devPontes.LeialaoME.model.entities.Permissao;
import com.devPontes.LeialaoME.model.entities.Usuario;
import com.devPontes.LeialaoME.model.entities.UsuarioVendedor;
import com.devPontes.LeialaoME.model.entities.enums.UsuarioRole;
import com.devPontes.LeialaoME.model.entities.mapper.MyMaper;
import com.devPontes.LeialaoME.repositories.PermissaoRepositories;
import com.devPontes.LeialaoME.repositories.UsuarioRepositories;
import com.devPontes.LeialaoME.repositories.UsuarioVendedorRepositories;
import com.devPontes.LeialaoME.services.UsuarioVendedorService;

import jakarta.transaction.Transactional;

@Service
public class UsuarioVendedorServicesImpl implements UsuarioVendedorService{

	@Autowired
	UsuarioVendedorRepositories vendedorRepository;
	
	@Autowired
	PermissaoRepositories permissaoRepository;
	
	@Autowired
	UsuarioRepositories usuarioRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private OfertaServicesImpl ofertaServices;
	
	@Autowired
	private	CnpjCpfValidadorClient cnpjValidator;
	
	private static final Logger log = LoggerFactory.getLogger(UsuarioVendedorServicesImpl.class);
	
	// Define a pasta de upload (pode ser qualquer pasta que o usuário atual tenha permissão)
	private final String uploadDir = System.getProperty("user.home") + File.separator + "uploads";
	
	public UsuarioVendedorDTO findById(Long compradorId) {
		UsuarioVendedor entidade = vendedorRepository.findById(compradorId)
												     .orElseThrow(() -> 
												     	new UsuarioNaoEncontradoException("Usuario não encontrado com ID" + compradorId));
		
		var entidadeDto = MyMaper.parseObject(entidade, UsuarioVendedorDTO.class);
		
		return entidadeDto;
	}


	@Transactional
	public UsuarioDTO cadastrarUsuarioVendedor(UsuarioDTO novoUsuario) throws Exception {
		UsuarioVendedor user = (UsuarioVendedor) MyMaper.parseObject(novoUsuario, UsuarioVendedor.class);
		
		if (!cnpjValidator.validarCnpj(user.getCnpj())) 
			throw new Exception("CNPJ Não Pode Ser Validado como CNPJ REAL");
		
		if(vendedorRepository.existsByUsername(user.getUsername()))
			throw new RuntimeException("Usuário já existe com esse username");
		
		Permissao roleComprador = permissaoRepository.findByUsuarioRole(UsuarioRole.ROLE_VENDEDOR);
		
		if (roleComprador == null) throw new RuntimeException("Permissão ROLE_VENDEDOR não encontrada no banco!");
	
		user.getPermissoes().add(roleComprador);
		user.setPassword(encoder.encode(user.getPassword()));

		UsuarioVendedor salvo = usuarioRepository.save(user);

		UsuarioDTO dto = MyMaper.parseObject(salvo, UsuarioDTO.class);
		dto.setPermissoes(
			        salvo.getPermissoes()
			             .stream()
			             .map(p -> new PermissaoDTO(p.getUsuarioRole()))
			             .collect(Collectors.toSet()));
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
				output.write(buffer, 0, bytesLidos); // escreve no disco passando da posição zero até o final qu terem o total de buffer lidos
						
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

		usuarioRepository.save(entidade);

		return response;
	}


	@Override
	@Transactional
	public UsuarioDTO atualizarUsuarioVendedor(UsuarioUpdateDTO update, Long usuarioId) throws Exception {
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

		
		UsuarioVendedor salvo = (UsuarioVendedor) usuarioRepository.save(entidade);
		return MyMaper.parseObject(salvo, UsuarioDTO.class);
	}


	@Override
	@Transactional
	public String escreverBiografia(Long usuarioId, String beografia) {
		var entidade = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não enontrado com ID" + usuarioId));

		if (entidade.getBiografia() != null)
			entidade.setBiografia(beografia);

		// Salva as alterações
		UsuarioVendedor salvo = (UsuarioVendedor) usuarioRepository.save(entidade);

		return entidade.getBiografia();
	}


	@Override
	public String atualizarUsername(Long usuarioId, String usernameNovo) {
		var entidade = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não enontrado com ID" + usuarioId));

		if (entidade.getUsername() != null)
			entidade.setUsername(usernameNovo);

		// Salva as alterações
		UsuarioVendedor salvo = (UsuarioVendedor) usuarioRepository.save(entidade);

		return entidade.getUsername();
	}


	@Override
	public String atualizarPassword(Long usuarioId, String passwordNova) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Set<OfertaDTO> findOfertaMaisBaixaRecebida(Usuario usuarioLogado, String cnpjComprador, Double maximumValue) {
		var ofertaRecebidas = ofertaServices.findOfertasMaisCarasRecebidasDeVendedor(usuarioLogado, cnpjComprador, maximumValue);
		return ofertaRecebidas;
	}


	@Override
	public Set<LeilaoDTO> findLeiloesParticipados(Long usuarioVendedorId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Set<OfertaDTO> findOfertasMaisCarasRecebidas(String cnpjVendedor, Double minimumValue) {
		// TODO Auto-generated method stub
		return null;
	}


	private <R> R PermissaoDTO(PermissaoDTO permissaodto1) {
		return null;
	}


	
	
	
}
