package com.devPontes.LeialaoME.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.devPontes.LeialaoME.model.dto.UsuarioDTO;
import com.devPontes.LeialaoME.services.impl.UsuarioCompradorServicesImpl;

@RestController
@RequestMapping("/v1/comprador")
public class UsuarioCompradoController {

	private static final Logger log = LoggerFactory.getLogger(UsuarioCompradoController.class);

	@Autowired
	UsuarioCompradorServicesImpl compradorServices;

	@PostMapping("/{id}/upload-foto")
	public ResponseEntity<String> uploadFoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws Exception {
		String caminho = compradorServices.fazerUploadDeImamgemDePerfil(id, file);
		log.info("Path > " + caminho + "Arquivo > " + "Tamanho em Bytes do Arquivo: " + file.getBytes().length, "Nome Original: " + file.getOriginalFilename(), "Dados: " + file.getInputStream());
		var response = ResponseEntity.ok("Upload concluído: " + caminho);
	
		log.info("Response:" + response);
		return response;
	}


	@PostMapping(path = "/cadastrar-comprador")
	public ResponseEntity<UsuarioDTO> registrarUsuarioComprador(@RequestBody UsuarioDTO usuario) throws Exception {
		UsuarioDTO saved = compradorServices.cadastrarUsuarioComprador(usuario);
		log.info("O que rolou pra você Pedro:" + saved);
	    return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}

	
}
