package com.devPontes.LeialaoME.model.DTO.v1;

import java.time.LocalDateTime;

import com.devPontes.LeialaoME.model.entities.enums.StatusOferta;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OfertaDTO {
	
	@JsonIgnore
    private Long id;
	
	@JsonProperty("valor")
    private Double valorOferta;
	
	@JsonProperty("momento")
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime momentoOferta = LocalDateTime.now();
	
	@JsonProperty("status")
    private StatusOferta statusOferta;
	
    private Long compradorId; 
    private Long leilaoId;    

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getValorOferta() { return valorOferta; }
    public void setValorOferta(Double valorOferta) { this.valorOferta = valorOferta; }

    public LocalDateTime getMomentoOferta() { return momentoOferta; }
    public void setMomentoOferta(LocalDateTime momentoOferta) { this.momentoOferta = momentoOferta; }

    public StatusOferta getStatusOferta() { return statusOferta; }
    public void setStatusOferta(StatusOferta statusOferta) { this.statusOferta = statusOferta; }

    public Long getCompradorId() { return compradorId; }
    public void setCompradorId(Long compradorId) { this.compradorId = compradorId; }

    public Long getLeilaoId() { return leilaoId; }
    public void setLeilaoId(Long leilaoId) { this.leilaoId = leilaoId; }
}
