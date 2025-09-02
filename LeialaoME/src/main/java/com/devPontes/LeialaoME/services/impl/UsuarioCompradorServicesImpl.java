package com.devPontes.LeialaoME.services.impl;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.model.dto.PermissaoDTO;
import com.devPontes.LeialaoME.model.dto.UsuarioDTO;
import com.devPontes.LeialaoME.model.entities.Permissao;
import com.devPontes.LeialaoME.model.entities.UsuarioComprador;
import com.devPontes.LeialaoME.model.entities.enums.UsuarioRole;
import com.devPontes.LeialaoME.model.entities.mapper.MyMaper;
import com.devPontes.LeialaoME.repositories.UsuarioRepositories;
import com.devPontes.LeialaoME.services.UsuarioCompradorService;

@Service
public class UsuarioCompradorServicesImpl implements UsuarioCompradorService {

	private static final Logger log = LoggerFactory.getLogger(UsuarioCompradorServicesImpl.class);

	@Autowired
	private UsuarioRepositories usuarioRepository;

	@Autowired
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public UsuarioDTO cadastrarUsuarioComprador(UsuarioDTO novoUsuario) throws Exception {
		// Mapear DTO para entidade
		UsuarioComprador user = MyMaper.parseObject(novoUsuario, UsuarioComprador.class);

		// 2️⃣ Converter permissões do DTO para entidades (se houver)
		Set<Permissao> permissoes = novoUsuario.getPermissoes().stream().map(p -> {
			Permissao permissao = new Permissao();
			permissao.setUsuarioRole(UsuarioRole.valueOf(p.getNome()));
			return permissao;
		}).collect(Collectors.toSet());

		user.getPermissoes().addAll(permissoes);

		if(!CnpjCpfValidadorClient.validarCPf(novoUsuario.getCpf())) 
											throw new Exception("CPF Não Pode Ser Validado como CPF");
		
		// Garantir que o usuário sempre receba ROLE_COMPRADOR
		Permissao roleComprador = new Permissao();
		roleComprador.setUsuarioRole(UsuarioRole.ROLE_COMPRADOR);
		user.getPermissoes().add(roleComprador);

		// Criptografar senha
		user.setPassword(encoder.encode(user.getPassword()));

		// Salvar no banco
		UsuarioComprador salvo = usuarioRepository.save(user);

		// Mapear de volta para DTO e refletir permissões do banco
		UsuarioDTO dto = MyMaper.parseObject(salvo, UsuarioDTO.class);
		dto.setPermissoes(salvo.getPermissoes().stream().filter(p -> p.getUsuarioRole() != null)
				.map(p -> new PermissaoDTO(p.getUsuarioRole().name())).collect(Collectors.toSet()));

		return dto;

	}

}
