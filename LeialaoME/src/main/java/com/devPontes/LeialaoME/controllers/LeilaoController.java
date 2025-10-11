package com.devPontes.LeialaoME.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devPontes.LeialaoME.model.dto.LeilaoDTO;
import com.devPontes.LeialaoME.services.impl.LeilaoServicesImpl;
import com.devPontes.LeialaoME.services.impl.UsuarioCompradorServicesImpl;
import com.devPontes.LeialaoME.services.impl.UsuarioVendedorServicesImpl;

@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8080"})
@RestController
@RequestMapping("/v1/leilao")
public class LeilaoController {

	private static final Logger log = LoggerFactory.getLogger(LeilaoController.class);

	@Autowired
	LeilaoServicesImpl leilaoServices;
	
	@Autowired
	UsuarioCompradorServicesImpl compradorServices;
	
	@Autowired
	UsuarioVendedorServicesImpl vendedorServices;
	
	@PreAuthorize("hasRole('VENDEDOR')")
	@PostMapping(path = "/criar-leilao")
	public ResponseEntity<LeilaoDTO> criarLeilao(@RequestBody LeilaoDTO leilaoNovo, @RequestParam Long vendedorId) {
		log.info("🧱 Iniciando criação de leilão pelo vendedor ID {}", vendedorId);
		LeilaoDTO novoLeilao = leilaoServices.criarLeilao(vendedorId, leilaoNovo);
		if(novoLeilao != null) log.info("Leilão sendo criado com sucesso: {}" + leilaoNovo.getDescricao());
		return new ResponseEntity<>(novoLeilao, HttpStatus.OK);		
	}
	

}
