package com.devPontes.LeialaoME.services.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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

@Service
public class UsuarioCompradorServicesImpl implements UsuarioCompradorService {

	@Autowired
    private UsuarioCompradorRepositories usuarioRepository;

	@Autowired
	private PermissaoRepositories permissaoRepository;

	@Autowired
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	private static final Logger log = LoggerFactory.getLogger(UsuarioCompradorServicesImpl.class);

	private final String nameDir = "/uploads";


	public UsuarioDTO cadastrarUsuarioComprador(UsuarioDTO novoUsuario) throws Exception {
		UsuarioComprador user = MyMaper.parseObject(novoUsuario, UsuarioComprador.class); // Mapear DTO para entidade
		user.setUrlFotoPerfil(novoUsuario.getFotoPerfil());

		if (!CnpjCpfValidadorClient.validarCPf(user.getCpf()))
			throw new Exception("CPF Não Pode Ser Validado como CPF");

		Permissao roleComprador = permissaoRepository.findByUsuarioRole(UsuarioRole.ROLE_COMPRADOR); // Garantir que o
																										// usuário
																										// sempre receba
																										// ROLE_COMPRADOR
		if (roleComprador == null)
			throw new RuntimeException("Permissão ROLE_COMPRADOR não encontrada no banco!");
		user.getPermissoes().add(roleComprador);

		user.setPassword(encoder.encode(user.getPassword()));

		UsuarioComprador salvo = usuarioRepository.save(user);

		UsuarioDTO dto = MyMaper.parseObject(salvo, UsuarioDTO.class);

		// Manipular o dto pra tentar resolver o bug do json voltando c permissao nula
		// msm c roles no banco
		dto.setPermissoes(user.getPermissoes().stream().map(p -> new PermissaoDTO(UsuarioRole.ROLE_COMPRADOR))
				.collect(Collectors.toSet()));
		return dto;
	}

	@Override
	public UsuarioDTO atualizarUsuarioComprador(UsuarioDTO update, Long usuarioId) {
		// TODO : buscar no banc usuario existente e atualizar campos vindo da request
		// salvar e mapear de volta
		return null;

	}

	@Override
	public String fazerUploadDeImamgemDePerfil(Long userId, MultipartFile file) throws Exception {
		UsuarioComprador usuarioExistente = (UsuarioComprador) usuarioRepository.findById(userId)		
				.orElseThrow(() -> new UsuarioNaoEncontradoException("Não foi possivel carregarr a foto deste usuario!"));
		try {
			// 1. Valida se não está vazio
			if (file.isEmpty())throw new RuntimeException("Aquivo vazio!");

			String nomeArquivo = UUID.randomUUID() + "_" + file.getName();    // 2 .Gerar nome do arquivo UNICO E IMUTAVEL
			File pasta = new File(nameDir);         // 2. Caminho local (pasta "uploads" no projeto)
			if (!pasta.exists())
				pasta.mkdir();     //3. Comando para criar pasta

			File destino = new File(pasta, nomeArquivo); // 4. Pasta de Destino

			try (InputStream is = file.getInputStream()) { // InputStream para "fazer o put" das streams do arquivo dos// frontends/clients
				OutputStream os = new FileOutputStream(destino); // Objeto para "fazer o put" em storage ou na S3/GCP/etc... Gravação
				byte[] buffer = new byte[1024]; // Vetor
				int tamanho;
				while ((tamanho = is.read(buffer)) > 0) { //ler o tamnho do arquivo que veio do input e dps percorrelo a partir da poscao zero e escrever ele na saida de estiono linha por linha por conta do vetor de byes
					os.write(buffer, 0, tamanho);
				}
			}
			usuarioExistente.setUrlFotoPerfil(nomeArquivo);
			usuarioRepository.save(usuarioExistente);
			return nomeArquivo;
		} catch (Exception e) {
			throw new RuntimeException("Upload não pôde ser concluido!");
		}

	}

	@Override
	public String escreverBiografia(Long usuarioId, String beografia) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String atualizarUsername(Long usuarioId, String usernameNovo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String atualizarPassword(Long usuarioId, String passwordNova) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LeilaoDTO findOfertasMaisCaras(Long ofertaId, Long userId, String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LeilaoDTO mostrarLeiloesAdquiridosDeUsuario(Long usuaroCompradorId) {
		// TODO Auto-generated method stub
		return null;
	}

}
