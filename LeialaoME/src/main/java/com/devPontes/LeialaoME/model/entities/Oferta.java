package com.devPontes.LeialaoME.model.entities;

import java.time.LocalDateTime;

import com.devPontes.LeialaoME.model.entities.enums.StatusOferta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Oferta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double valorOferta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOferta statusOferta;

    @ManyToOne
    @JoinColumn(name = "comprador_id", nullable = false)
    private UsuarioComprador comprador;

    @ManyToOne
    @JoinColumn(name = "leilao_id", nullable = false)
    private Leilao leilao;

    @Column(nullable = false)
    private LocalDateTime tempoDuravel = LocalDateTime.now();

    @Column(nullable = false)
    private boolean aceita = false;

    // Construtor padrão
    public Oferta() {}

    // Construtor com campos essenciais
    public Oferta(Double valorOferta, StatusOferta statusOferta, UsuarioComprador comprador, Leilao leilao) {
        this.valorOferta = valorOferta;
        this.statusOferta = statusOferta;
        this.comprador = comprador;
        this.leilao = leilao;
        this.tempoDuravel = LocalDateTime.now();
        this.aceita = false;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getValorOferta() { return valorOferta; }
    public void setValorOferta(Double valorOferta) { this.valorOferta = valorOferta; }

    public StatusOferta getStatusOferta() { return statusOferta; }
    public void setStatusOferta(StatusOferta statusOferta) { this.statusOferta = statusOferta; }

    public UsuarioComprador getComprador() { return comprador; }
    public void setComprador(UsuarioComprador comprador) { this.comprador = comprador; }

    public Leilao getLeilao() { return leilao; }
    public void setLeilao(Leilao leilao) { this.leilao = leilao; }

    public LocalDateTime getTempoDuravel() { return tempoDuravel; }
    public void setTempoDuravel(LocalDateTime tempoDuravel) { this.tempoDuravel = tempoDuravel; }

    public boolean isAceita() { return aceita; }
    public void setAceita(boolean aceita) { this.aceita = aceita; }

    @Override
    public String toString() {
        return "Oferta [id=" + id + ", valorOferta=" + valorOferta + ", statusOferta=" + statusOferta 
                + ", comprador=" + comprador.getUsername() + ", leilao=" + leilao.getId() 
                + ", tempoDuravel=" + tempoDuravel + ", aceita=" + aceita + "]";
    }
}
