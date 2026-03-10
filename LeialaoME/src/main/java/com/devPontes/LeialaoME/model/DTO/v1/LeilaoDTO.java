package com.devPontes.LeialaoME.model.DTO.v1;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFilter("LeilaoFilters")
public class LeilaoDTO {

	@JsonIgnore
    private Long id;
    
    @JsonProperty("titulo")
    private String descricao;
    
    @JsonFormat(pattern = "dd/MM/yyy HH:mm:ss")
    private LocalDateTime inicio;
    
    @JsonFormat(pattern = "dd/MM/yyy HH:mm:ss") //JSONFORMAT PARA DESCERIALIZAR DATAS 
    private LocalDateTime termino;
    
    @JsonProperty("primeiro_lance")
    private Double lanceInicial;
    
    @JsonProperty("incremento_lance")
    private Double valorIncrementadoAposLance;
    
    @JsonProperty("ativo")
    private Boolean aindaAtivo;

    private Long vendedorId;
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

    public Double getValorIncrementadoAposLance() { return valorIncrementadoAposLance; }
    public void setValorIncrementadoAposLance(Double valorIncrementadoAposLance) { this.valorIncrementadoAposLance = valorIncrementadoAposLance; }

    public Boolean getAindaAtivo() { return aindaAtivo; }
    public void setAindaAtivo(Boolean aindaAtivo) { this.aindaAtivo = aindaAtivo; }

    public Long getVendedorId() { return vendedorId; }
    public void setVendedorId(Long vendedorId) { this.vendedorId = vendedorId; }

    public Long getVencedorId() { return vencedorId; }
    public void setVencedorId(Long vencedorId) { this.vencedorId = vencedorId; }

}
