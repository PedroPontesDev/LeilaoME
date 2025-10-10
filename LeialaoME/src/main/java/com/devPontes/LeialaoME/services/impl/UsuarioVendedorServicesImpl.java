package com.devPontes.LeialaoME.services.impl;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.exceptions.UsuarioNaoEncontradoException;
import com.devPontes.LeialaoME.model.dto.PermissaoDTO;
import com.devPontes.LeialaoME.model.dto.UsuarioDTO;
import com.devPontes.LeialaoME.model.dto.UsuarioVendedorDTO;
import com.devPontes.LeialaoME.model.entities.Permissao;
import com.devPontes.LeialaoME.model.entities.UsuarioComprador;
import com.devPontes.LeialaoME.model.entities.UsuarioVendedor;
import com.devPontes.LeialaoME.model.entities.enums.UsuarioRole;
import com.devPontes.LeialaoME.model.entities.mapper.MyMaper;
import com.devPontes.LeialaoME.repositories.PermissaoRepositories;
import com.devPontes.LeialaoME.repositories.UsuarioRepositories;
import com.devPontes.LeialaoME.repositories.UsuarioVendedorRepositories;
import com.devPontes.LeialaoME.utils.CnpjCpfValidadorClient;

@Service
public class UsuarioVendedorServicesImpl {

	@Autowired
	UsuarioVendedorRepositories vendedorRepository;
	
	@Autowired
	PermissaoRepositories permissaoRepository;
	
	@Autowired
	UsuarioRepositories usuarioRepository;
	
	public UsuarioVendedorDTO findById(Long compradorId) {
		UsuarioVendedor entidade = vendedorRepository.findById(compradorId)
												     .orElseThrow(() -> 
												     	new UsuarioNaoEncontradoException("Usuario não encontrado com ID" + compradorId));
		return MyMaper.parseObject(entidade, UsuarioVendedorDTO.class);
	}


	public UsuarioDTO cadastrarUsuarioVendedor(UsuarioDTO novoUsuario) throws Exception {

		UsuarioComprador user = MyMaper.parseObject(novoUsuario, UsuarioComprador.class);

		if (!CnpjCpfValidadorClient.validarCnpj(novoUsuario.getCpf())) {
			throw new Exception("CPF Não Pode Ser Validado como CPF");
		}

		Permissao roleComprador = permissaoRepository.findByUsuarioRole(UsuarioRole.ROLE_COMPRADOR);
		if (roleComprador == null)
			throw new RuntimeException("Permissão ROLE_COMPRADOR não encontrada no banco!");

		user.getPermissoes().add(roleComprador);
		user.setPassword(encoder.encode(user.getPassword()));

		UsuarioVendedor salvo = usuarioRepository.save(user);

		UsuarioDTO dto = MyMaper.parseObject(salvo, UsuarioDTO.class);
		dto.setPermissoes(user.getPermissoes().stream().map(p -> new PermissaoDTO(UsuarioRole.ROLE_COMPRADOR))
				.collect(Collectors.toSet()));

		log.info("Usuário cadastrado com sucesso: {}", user.getUsername());
		return dto;
	}

	
	

	}
