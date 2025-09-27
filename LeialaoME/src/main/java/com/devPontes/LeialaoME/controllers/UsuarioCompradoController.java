package com.devPontes.LeialaoME.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.devPontes.LeialaoME.services.impl.UsuarioCompradorServicesImpl;


@RestController
@RequestMapping("/comprador")
public class UsuarioCompradoController {
	
	
	@Autowired
	UsuarioCompradorServicesImpl compradorServices;
	
	@PostMapping("/usuarios/{id}/upload-foto")
	public ResponseEntity<String> uploadFoto(  @PathVariable Long id, @RequestParam("file") MultipartFile file) {
	    try {
	        String caminho = compradorServices.fazerUploadDeImamgemDePerfil(id. file);
	        return ResponseEntity.ok("Upload concluído: " + caminho);
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("Erro: " + e.getMessage());
	    }
	}

}
