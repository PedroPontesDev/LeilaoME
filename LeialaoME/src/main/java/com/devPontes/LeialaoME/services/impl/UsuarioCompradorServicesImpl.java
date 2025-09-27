package com.devPontes.LeialaoME.services.impl;

import java.io.File;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.devPontes.LeialaoME.model.dto.LeilaoDTO;
import com.devPontes.LeialaoME.model.dto.PermissaoDTO;
import com.devPontes.LeialaoME.model.dto.UsuarioDTO;
import com.devPontes.LeialaoME.model.entities.Permissao;
import com.devPontes.LeialaoME.model.entities.UsuarioComprador;
import com.devPontes.LeialaoME.model.entities.enums.UsuarioRole;
import com.devPontes.LeialaoME.model.entities.mapper.MyMaper;
import com.devPontes.LeialaoME.repositories.PermissaoRepositories;
import com.devPontes.LeialaoME.repositories.UsuarioRepositories;
import com.devPontes.LeialaoME.services.UsuarioCompradorService;
import com.devPontes.LeialaoME.utils.CnpjCpfValidadorClient;

@Service
public class UsuarioCompradorServicesImpl implements UsuarioCompradorService {

	private static final Logger log = LoggerFactory.getLogger(UsuarioCompradorServicesImpl.class);

	//private final S3Client s3Client;

   // @Value("${aws.s3.bucket}")
  //  private String bucketName;
	
	
	@Autowired
	private UsuarioRepositories usuarioRepository;
  
	@Autowired
	private PermissaoRepositories permissaoRepository;

	@Autowired
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	
	public UsuarioDTO cadastrarUsuarioComprador(UsuarioDTO novoUsuario) throws Exception {
		UsuarioComprador user = MyMaper.parseObject(novoUsuario, UsuarioComprador.class); // Mapear DTO para entidade
		user.setUrlFotoPerfil(novoUsuario.getFotoPerfil());

		if (!CnpjCpfValidadorClient.validarCPf(user.getCpf()))
			throw new Exception("CPF Não Pode Ser Validado como CPF");
		
	
		Permissao roleComprador = permissaoRepository.findByUsuarioRole(UsuarioRole.ROLE_COMPRADOR); // Garantir que o usuário sempre receba ROLE_COMPRADOR
		if (roleComprador == null) 
			throw new RuntimeException("Permissão ROLE_COMPRADOR não encontrada no banco!");
		user.getPermissoes().add(roleComprador);
		
		user.setPassword(encoder.encode(user.getPassword()));
		
		UsuarioComprador salvo = usuarioRepository.save(user);

		UsuarioDTO dto = MyMaper.parseObject(salvo, UsuarioDTO.class);

		//Manipular o dto pra tentar resolver o bug do json voltando c permissao nula msm c roles no banco
		dto.setPermissoes(user.getPermissoes()
								.stream()
								.map(p -> new PermissaoDTO(UsuarioRole.ROLE_COMPRADOR))
								.collect(Collectors.toSet()));
		return dto;
	} 

	@Override
	public UsuarioDTO atualizarUsuarioComprador(UsuarioDTO update, Long usuarioId) {
		// TODO : buscar no banc usuario existente e atualizar campos vindo da request salvar e mapear de volta
		return null;
		
		
	}

	@Override
	public String fazerUploadDeImamgemDePerfil(MultipartFile file) throws Exception {
		/* TODO: Utilizar o parametro file o objeto File  e o IOstream pra fazer um uppload salvar no banco. 
		Lembrar de criar variaveis com o Objeto file criar para o diretorio/destion e nome de arquivo */
		
		try {

			 // 1. Valida se não está vazio
			if(file.isEmpty()) {
				throw new RuntimeException("Aquivo vazio!");
			}
		
			//2 .Gerar nome do arquivo UNICO E IMUTAVEL
			String nomeArquivo = UUID.randomUUID() + "_" + file.getName();
			
			// 3. Caminho local (pasta "uploads" no projeto)
			File pasta = new File("");  
			
			
		
			
			
			
		}catch (Exception e) {
			throw new RuntimeException("Upload não pôde ser concluido!");
		}
		
		
		
		
		
		return null;
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
