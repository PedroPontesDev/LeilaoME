package com.devPontes.LeialaoME.model.DTO;

import java.time.LocalDateTime;

public class LeilaoDTO {

    private Long id;
    private String descricao;
    private LocalDateTime inicio;
    private LocalDateTime termino;
    private Double lanceInicial;
    private Double valorIncrementadoAposLance;
    private Boolean aindaAtivo;

    private Long vendedorId;
    private Long vencedorId;

    // Getters/Setters
    public Long getId() { return id; }
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
