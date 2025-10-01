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
import org.springframework.http.ResponseEntity;
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	public Map<String, Object> fazerUploadDeImamgemDePerfil(Long userId, MultipartFile file) throws Exception {
		// Trabalhando com JsonNode para frontend consumir como JSON é feio mas funciona
		// bem

		// Primeiro, buscamos o usuário no banco pelo ID. Se não existir, lançamos
		// exceção.
		// Isso evita tentar salvar uma foto para um usuário que não existe.
		UsuarioComprador usuarioExistente = (UsuarioComprador) usuarioRepository.findById(userId).orElseThrow(
				() -> new UsuarioNaoEncontradoException("Não foi possível carregar a foto deste usuário!"));

		// Verifica se o arquivo enviado não está vazio.
		// Evita criar arquivos zerados ou quebrar a lógica do upload.
		if (file.isEmpty())
			throw new RuntimeException("Arquivo vazio!");

		// Valida o tipo do arquivo. Aqui estamos aceitando apenas imagens jpg, jpeg ou
		// png.
		// getContentType() retorna o MIME type, mas aqui simplificamos usando endsWith.
		// Se não for imagem, lançamos exceção. Isso previne uploads de arquivos
		// inválidos.
		String contentType = file.getContentType();

		if (contentType == null || 
		   !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
		    throw new Exception("Servidor só pode consumir imagens JPG ou PNG!");
		}

		
		// Criamos um nome único para o arquivo, usando UUID + nome original.
		// Substituímos caracteres especiais no nome para evitar problemas no sistema de
		// arquivos.
		String nomeArquivo = UUID.randomUUID() + "_" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

		// Criamos o diretório se ele não existir ainda.
		// nameDir é uma pasta que definimos como destino dos uploads.
		File pasta = new File(nameDir);
		if (!pasta.exists()) {
			pasta.mkdir(); // mkdir cria apenas a pasta especificada, não subpastas.
		}

		// Criamos o Path completo do arquivo final (pasta + nome do arquivo)
		// Path é melhor que File para manipulação de caminhos modernos no Java.
		Path diretorio = Paths.get(pasta.getAbsolutePath(), nomeArquivo);

		// Aqui abrimos um InputStream para ler os bytes do MultipartFile
		// e um OutputStream para gravar no disco.
		// Fazemos isso em um try-with-resources para garantir que os streams serão
		// fechados automaticamente.
		try (InputStream input = file.getInputStream(); OutputStream output = Files.newOutputStream(diretorio)) {

			byte[] buffer = new byte[8192]; // Buffer de 8KB, lê por blocos para não carregar tudo na memória
			int bytesLidos;

			// Enquanto houver bytes para ler do arquivo, vamos escrevendo no disco
			// output.write escreve exatamente os bytes lidos no buffer, começando do 0 até
			// bytesLidos
			while ((bytesLidos = input.read(buffer)) != -1) {
				output.write(buffer, 0, bytesLidos);
			}
		}

		// Geramos a URL relativa do arquivo, que é o que vamos devolver ao front-end
		// Isso permite que o React ou outro cliente consiga acessar a imagem depois.

		String urlRelativa = "/uploads/" + (nomeArquivo);

		// Atualizamos o usuário com a URL da foto de perfil
		usuarioExistente.setUrlFotoPerfil(urlRelativa);
		usuarioRepository.save(usuarioExistente);

		// Retornamos a URL relativa para o front-end com JSONODE
		ObjectMapper objMapper = new ObjectMapper();
		JsonNode responseBody = objMapper.createObjectNode().put("url", urlRelativa);

		// Cria uma resposta JSON simples
		Map<String, Object> response = new HashMap<>();
		response.put("message", "Upload concluído com sucesso!");
		response.put("url", urlRelativa);
		response.put("fileName", file.getOriginalFilename());
		response.put("size", file.getSize()); // tamanho em bytes
		log.info("Upload feito para usuário {} -> {}", userId, urlRelativa);
		return response;
	}

	@Override
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
	public String atualizarUsername(Long usuarioId, String usernameNovo) {
		var entidade = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não enontrado com ID" + usuarioId));

		if (entidade.getUsername() != null)
			entidade.setUsername(usernameNovo);

		// Salva as alterações
		UsuarioComprador salvo = (UsuarioComprador) usuarioRepository.save(entidade);

		return entidade.getBiografia();
	}

	@Override
	public String atualizarPassword(Long usuarioId, String passwordNova) {
		throw new UnsupportedOperationException("Não implementado ainda");
	}

	@Override
	public Set<OfertaDTO> findOfertasMaisCaras(Long userId, Double minimumValue) {
		// TODO: Achar as ofertas mais caras dadas por um comprador por SQL

		throw new UnsupportedOperationException("Não implementado ainda");
	}

	/*
	 * public OfertaDTO findOfertaMaisBaixa(Long userId, Double maximumValue) { //
	 * TODO: Achar as ofertas mais caras dadas por um comprador por SQL var entidade
	 * = usuarioRepository.findById(usuarioId) .orElseThrow(() -> new
	 * UsuarioNaoEncontradoException("Usuario não enontrado com ID" + usuarioId));
	 * 
	 * 
	 * 
	 * throw new UnsupportedOperationException("Não implementado ainda"); }
	 */

	@Override
	public Set<LeilaoDTO> findLeiloesAdquiridosDeUsuario(Long usuarioCompradorId) {
		// TODO: mostrar historico de leiloes adquiridos por id de usuario
		throw new UnsupportedOperationException("Não implementado ainda");
	}

	@Override
	public OfertaDTO findOfertaMaisBaixa(Long userId, Double maximumValue) {
		// TODO Auto-generated method stub
		return null;
	}

}
