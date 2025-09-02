package com.devPontes.LeialaoME.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.model.dto.UsuarioCompradorDTO;
import com.devPontes.LeialaoME.model.dto.UsuarioDTO;
import com.devPontes.LeialaoME.repositories.UsuarioRepositories;

@Service
public class UsuarioCompradorServicesImpl {

	@Autowired
	private UsuarioRepositories usuarioRepository;

	private static final Logger log = LoggerFactory.getLogger(UsuarioCompradorServicesImpl.class);

	
	public UsuarioCompradorDTO cadastrarUsuarioComprador(UsuarioDTO novoUsuario) {
		return new UsuarioCompradorDTO();
	}
	
	
}
