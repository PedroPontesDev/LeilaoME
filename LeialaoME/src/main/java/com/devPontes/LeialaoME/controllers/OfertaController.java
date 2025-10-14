package com.devPontes.LeialaoME.controllers;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.devPontes.LeialaoME.model.DTO.OfertaDTO;
import com.devPontes.LeialaoME.model.entities.Usuario;
import com.devPontes.LeialaoME.services.impl.OfertaServicesImpl;

@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:8080" })
@RestController
@RequestMapping("/v1/oferta")
public class OfertaController {
	private static final Logger log = LoggerFactory.getLogger(OfertaController.class);

	@Autowired
	OfertaServicesImpl ofertaServices;

	@PreAuthorize("hasRole('COMPRADOR')")
	@PostMapping(path = "/oferecer-oferta/{leilaoId}")
	public ResponseEntity<OfertaDTO> fazerPropostaPraLeilaoExistente(@RequestBody OfertaDTO oferta,
			@PathVariable Long leilaoId, @AuthenticationPrincipal Usuario usuarioLogado) {
		var ofertaOferecida = ofertaServices.fazerPropostaParaLeilao(oferta, leilaoId, usuarioLogado);

		return new ResponseEntity<>(ofertaOferecida, HttpStatus.OK);

	}

	@PreAuthorize("hasRole('VENDEDOR')")
	@PutMapping("/aceitar/{leilaoId}/{ofertaId}")
    public ResponseEntity<OfertaDTO> aceitarOferta(
            @AuthenticationPrincipal Usuario usuarioLogado,
            @PathVariable Long leilaoId,
            @PathVariable Long ofertaId) {

        OfertaDTO ofertaAceita = ofertaServices.aceitarOfertaDeLeilao(usuarioLogado, leilaoId, ofertaId);
        return ResponseEntity.ok(ofertaAceita);
    }
	
	
	 	@PreAuthorize("hasRole('VENDEDOR')")
	    @GetMapping("/mais-caras")
	    public ResponseEntity<Set<OfertaDTO>> findOfertasMaisCaras(
	            @AuthenticationPrincipal Usuario usuarioLogado,
	            @RequestParam String cnpjVendedor,
	            @RequestParam Double valorMinimo) {

	        Set<OfertaDTO> ofertas = ofertaServices.findOfertasMaisCarasRecebidasDeVendedor(
	                usuarioLogado, cnpjVendedor, valorMinimo
	        );
	        return ResponseEntity.ok(ofertas);
	    }
	
}
