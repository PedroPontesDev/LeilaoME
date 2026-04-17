package com.devPontes.LeialaoME.controllers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.devPontes.LeialaoME.model.entities.Usuario;
import com.devPontes.LeialaoME.services.impl.UsuarioVendedorServicesImpl;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/v1/vendedor")
public class UsuarioVendedorController {

	private static final Logger log = LoggerFactory.getLogger(UsuarioVendedorController.class);

	@Autowired
	UsuarioVendedorServicesImpl vendedorServices;
	
	@PostMapping("/{id}/upload-foto")  //Utilizar anotaiton @AuthenticationPrincipal para passar o id logado certo
	public ResponseEntity<Map<String, Object>> uploadFoto(@AuthenticationPrincipal Usuario usuarioLogado, @RequestParam("file") MultipartFile file) throws Exception {
		Map<String, Object> caminho =  vendedorServices.fazerUploadDeImamgemDePerfil(usuarioLogado, file);
		return new ResponseEntity<>(caminho, HttpStatus.ACCEPTED);
	}

	
	
}
