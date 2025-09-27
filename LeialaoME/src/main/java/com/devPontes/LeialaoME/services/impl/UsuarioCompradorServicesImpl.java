package com.devPontes.LeialaoME.services.impl;

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

	@Autowired
	private UsuarioRepositories usuarioRepository;
  
	@Autowired
	private PermissaoRepositories permissaoRepository;

	@Autowired
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	
	public UsuarioDTO cadastrarUsuarioComprador(UsuarioDTO novoUsuario) throws Exception {
		// Mapear DTO para entidade
		UsuarioComprador user = MyMaper.parseObject(novoUsuario, UsuarioComprador.class);
		user.setUrlFotoPerfil(novoUsuario.getFotoPerfil());

		if (!CnpjCpfValidadorClient.validarCPf(user.getCpf()))
			throw new Exception("CPF Não Pode Ser Validado como CPF");
		
		// Garantir que o usuário sempre receba ROLE_COMPRADOR
		Permissao roleComprador = permissaoRepository.findByUsuarioRole(UsuarioRole.ROLE_COMPRADOR);
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
		
		///Manipular o dto pra tentar resolve ro bug do json nao retornal o campo foto de urlFotoPerifl
		///AH Q ODIO
		///
		return dto;

	} 

	@Override
	public UsuarioDTO atualizarUsuarioComprador(UsuarioDTO update, Long usuarioId) {
		// TODO Auto-generated method stub
		return null;
		
		
	}

	@Override
	public String fazerUploadDeImamgemDePerfil(MultipartFile file) {
		// TODO Auto-generated method stub
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
