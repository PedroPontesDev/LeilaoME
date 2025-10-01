package com.devPontes.LeialaoME.controllers;

import java.util.Map;

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
	public ResponseEntity<Map<String, Object>> uploadFoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws Exception {
		Map<String, Object> caminho =  compradorServices.fazerUploadDeImamgemDePerfil(id, file);
		return new ResponseEntity<>(caminho, HttpStatus.ACCEPTED);
	}


	@PostMapping(path = "/cadastrar-comprador")
	public ResponseEntity<UsuarioDTO> registrarUsuarioComprador(@RequestBody UsuarioDTO usuario) throws Exception {
		UsuarioDTO saved = compradorServices.cadastrarUsuarioComprador(usuario);
	    return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}

	
}
