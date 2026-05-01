package com.devPontes.LeialaoME.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.devPontes.LeialaoME.model.DTO.v1.LeilaoDTO;
import com.devPontes.LeialaoME.model.entities.Usuario;
import com.devPontes.LeialaoME.services.impl.LeilaoServicesImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/leilao/v1")
@Tag(name = "", description = "Gerenciamento de todo o fluxo de leilão")
public class LeilaoController {

    private static final Logger log = LoggerFactory.getLogger(LeilaoController.class);

    @Autowired
    private LeilaoServicesImpl leilaoServices;

    @GetMapping(path = "/find-all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Procurar todos os leilões", responses = {
        @ApiResponse(description = "Sucesso", responseCode = "200", content = @Content(schema = @Schema(implementation = LeilaoDTO.class))),
        @ApiResponse(description = "Sem Conteúdo", responseCode = "204", content = @Content)
    })
    public ResponseEntity<List<LeilaoDTO>> findAll() {
        var all = leilaoServices.findAll();
        return ResponseEntity.ok(all);
    }

    @PreAuthorize("hasRole('VENDEDOR')")
    @GetMapping(path = "/visualizar-por-status", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Filtrar leilões por status", responses = {
        @ApiResponse(description = "Sucesso", responseCode = "200", content = @Content)
    })
    public ResponseEntity<List<LeilaoDTO>> findLeilaoPorStatus(
            @Parameter(description = "Status do leilão (ex: ATIVO, ENCERRADO)") @RequestParam String status) {
        var finded = leilaoServices.findLeilaoPorStatus(status);
        return ResponseEntity.ok(finded);
    }

    @PreAuthorize("hasRole('VENDEDOR')")
    @PostMapping(path = "/criar-leilao", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Cria um novo leilão comum", responses = {
        @ApiResponse(description = "Criado", responseCode = "201", content = @Content),
        @ApiResponse(description = "Erro na requisição", responseCode = "400", content = @Content)
    })
    public ResponseEntity<LeilaoDTO> criarLeilao(@RequestBody LeilaoDTO leilaoNovo, 
                                                @AuthenticationPrincipal Usuario usuarioLogado) {
        log.info("Iniciando criação de leilão pelo vendedor ID {}", usuarioLogado.getId());
        LeilaoDTO novoLeilao = leilaoServices.criarLeilao(leilaoNovo, usuarioLogado);
        return new ResponseEntity<>(novoLeilao, HttpStatus.CREATED);     
    }

    @PreAuthorize("hasRole('VENDEDOR')")
    @PostMapping(path = "/criar-leilao-futuro", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Agenda um leilão para data futura", responses = {
        @ApiResponse(description = "Agendado", responseCode = "201", content = @Content)
    })
    public ResponseEntity<LeilaoDTO> criarLeilaoFuturo(
            @RequestBody LeilaoDTO novoLeilao,
            @Parameter(description = "Data de início (ISO)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime tempoInicio,
            @Parameter(description = "Data de fim (ISO)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime tempoFim,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        log.info("Criando leilão futuro para vendedor ID {} entre {} e {}", usuarioLogado.getId(), tempoInicio, tempoFim);
        LeilaoDTO leilaoCriado = leilaoServices.criarLeilaoFuturo(novoLeilao, tempoInicio, tempoFim, usuarioLogado);
        return new ResponseEntity<>(leilaoCriado, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('VENDEDOR')")
    @PostMapping(path = "/criar-leilao-reduzido", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Cria um leilão com tempo reduzido (Flash)", responses = {
        @ApiResponse(description = "Criado", responseCode = "201", content = @Content)
    })
    public ResponseEntity<LeilaoDTO> criarLeilaoReduzidoEmTempo(
            @RequestBody LeilaoDTO leilaoReduzido,
            @Parameter(description = "Quantidade de horas para reduzir") @RequestParam("reducao") Long reducaoHoras, 
            @AuthenticationPrincipal Usuario usuarioLogado) {
        
        LeilaoDTO leilaoFlash = leilaoServices.criarLeilaoReduzido(leilaoReduzido, reducaoHoras, usuarioLogado);
        return new ResponseEntity<>(leilaoFlash, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping(path = "/definir-ganhador/{leilaoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Finaliza o leilão e define o vencedor", responses = {
        @ApiResponse(description = "Sucesso", responseCode = "200", content = @Content),
        @ApiResponse(description = "Erro interno", responseCode = "500", content = @Content)
    })
    public ResponseEntity<LeilaoDTO> definirGanhador(
            @PathVariable Long leilaoId,
            @AuthenticationPrincipal Usuario usuarioLogado) throws Exception {
        
        LeilaoDTO ganhadorLeilao = leilaoServices.definirGanhador(leilaoId, usuarioLogado);
        return ResponseEntity.ok(ganhadorLeilao);
    }   
    
    
}