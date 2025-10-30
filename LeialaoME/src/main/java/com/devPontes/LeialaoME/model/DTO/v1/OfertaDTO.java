package com.devPontes.LeialaoME.model.DTO.v1;

import java.time.LocalDateTime;
import com.devPontes.LeialaoME.model.entities.enums.StatusOferta;

public class OfertaDTO {
    private Long id;
    private Double valorOferta;
    private LocalDateTime momentoOferta;
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
