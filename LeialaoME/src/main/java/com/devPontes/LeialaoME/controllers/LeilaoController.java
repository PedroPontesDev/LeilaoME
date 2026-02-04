package com.devPontes.LeialaoME.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devPontes.LeialaoME.model.DTO.v1.LeilaoDTO;
import com.devPontes.LeialaoME.model.DTO.v1.OfertaDTO;
import com.devPontes.LeialaoME.model.entities.Usuario;
import com.devPontes.LeialaoME.services.impl.LeilaoServicesImpl;
import com.devPontes.LeialaoME.services.impl.UsuarioVendedorServicesImpl;

@CrossOrigin(origins = {"http://localhost:5173"})
@RestController
@RequestMapping("/v1/leilao")
public class LeilaoController {

	private static final Logger log = LoggerFactory.getLogger(LeilaoController.class);

	@Autowired
	LeilaoServicesImpl leilaoServices;
	
	@Autowired
	UsuarioVendedorServicesImpl vendedorServices;
	
	@PreAuthorize("hasRole('VENDEDOR')")
	@PostMapping(path = "/criar-leilao")
	public ResponseEntity<LeilaoDTO> criarLeilao(@RequestBody LeilaoDTO leilaoNovo, @AuthenticationPrincipal Usuario usuarioLogado) {
		log.info("🧱 Iniciando criação de leilão pelo vendedor ID {}" + usuarioLogado.getId());
		LeilaoDTO novoLeilao = leilaoServices.criarLeilao(leilaoNovo, usuarioLogado);
		if(novoLeilao != null) log.info("Leilão sendo criado com sucesso: {}" + leilaoNovo.getDescricao());
		return new ResponseEntity<>(novoLeilao, HttpStatus.OK);		
	}
	
	@PreAuthorize("hasRole('VENDEDOR')")
	@PostMapping(path = "/criar-leilao-futuro")
	public ResponseEntity<LeilaoDTO> criarLeilaoFuturo(
	        @RequestBody LeilaoDTO novoLeilao,
	        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime tempoInicio,
	        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime tempoFim,
	        @AuthenticationPrincipal Usuario usuarioLogado) {

	    log.info("Criando leilão futuro para vendedor ID {} com início {} e fim {}",
	            usuarioLogado.getId(), tempoInicio, tempoFim);

	    LeilaoDTO leilaoCriado = leilaoServices.criarLeilaoFuturo(novoLeilao, tempoInicio, tempoFim, usuarioLogado);
	    return new ResponseEntity<>(leilaoCriado, HttpStatus.CREATED);
	}

	
	@PreAuthorize("hasRole('VENDEDOR')")
	@GetMapping(path = "/visualizar-ofertas/{leilaoId}")
 	public ResponseEntity<Set<OfertaDTO>> visualizarOfertasDeLeilao(@AuthenticationPrincipal Usuario usuarioLogado, @PathVariable Long leilaoId) {
 		var ofertas = leilaoServices.visualizarOfertasDeLeilao(usuarioLogado, leilaoId);
 		return new ResponseEntity<>(ofertas, HttpStatus.OK);
 	}
	
	
	@GetMapping(path = "/find-all")
	public ResponseEntity<List<LeilaoDTO>> findAll() {
		var all = leilaoServices.findAll();
		return new ResponseEntity<>(all, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(path = "/definir-ganhador/{leilaoId}")
	public ResponseEntity<LeilaoDTO> definirGanhador(@PathVariable Long leilaoId,
			@AuthenticationPrincipal Usuario usuarioLogado) throws Exception {
		LeilaoDTO ganhadorLeilao = leilaoServices.definirGanhador(leilaoId, usuarioLogado);
		return new ResponseEntity<LeilaoDTO>(ganhadorLeilao, HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasRole('VENDEDOR')")
	@PostMapping(path = "/criar-leilao-reduzido")
	public ResponseEntity<LeilaoDTO> criarLeilaoReduzidoEmTempo(@RequestBody LeilaoDTO leilaoReduzido,
			@RequestParam("reducao") Long reducaoHoras, @AuthenticationPrincipal Usuario usuarioLogado) {
				LeilaoDTO leilaoFlash =  leilaoServices.criarLeilaoReduzido(leilaoReduzido, reducaoHoras, usuarioLogado);
				return new ResponseEntity<LeilaoDTO>(leilaoFlash, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('VENDEDOR')")
	@GetMapping(path = "/visualizar-por-status")
	public ResponseEntity<List<LeilaoDTO>> findLeilaoPorStatus(@RequestParam("status") String status) {
		var finded = leilaoServices.findLeilaoPorStatus(status);
		return new ResponseEntity<List<LeilaoDTO>>(finded,HttpStatus.OK);
		
	}


}
