package com.devPontes.LeialaoME.model.DTO.v1;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LeilaoDTO extends RepresentationModel<LeilaoDTO> {

    private Long id;
    
    @JsonProperty("titulo")
    private String descricao;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime inicio;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime termino;
    
    @JsonProperty("primeiro_lance")
    private Double lanceInicial;
    
    @JsonProperty("incremento_lance")
    private Double valorDeIncremento;
    
    @JsonProperty("ativo")
    private Boolean aindaAtivo;

    private Long vendedorId;
    
    private String urlFotoProduto;
    
    private Long compradorId;
    private Long vencedorId;

    public Long getId() { return id;}
    public Long getCompradorId() {return compradorId;}
	public void setCompradorId(Long compradorId) {
		this.compradorId = compradorId;
	}
	public void setId(Long id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getInicio() { return inicio; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }

    public LocalDateTime getTermino() { return termino; }
    public void setTermino(LocalDateTime termino) { this.termino = termino; }

    public Double getLanceInicial() { return lanceInicial; }
    public void setLanceInicial(Double lanceInicial) { this.lanceInicial = lanceInicial; }
    
    
    public Double getValorDeIncremento() {
		return valorDeIncremento;
	}
	public void setValorDeIncremento(Double valorDeIncremento) {
		this.valorDeIncremento = valorDeIncremento;
	}
	public Boolean getAindaAtivo() { return aindaAtivo; }
    public void setAindaAtivo(Boolean aindaAtivo) { this.aindaAtivo = aindaAtivo; }

    public Long getVendedorId() { return vendedorId; }
    public void setVendedorId(Long vendedorId) { this.vendedorId = vendedorId; }

    public Long getVencedorId() { return vencedorId; }
    public void setVencedorId(Long vencedorId) { this.vencedorId = vencedorId; }
	
    public String getUrlFotoProduto() {return urlFotoProduto;}
	
    public void setUrlFotoProduto(String urlFotoProduto) {
		this.urlFotoProduto = urlFotoProduto;
	}
    
    

}
