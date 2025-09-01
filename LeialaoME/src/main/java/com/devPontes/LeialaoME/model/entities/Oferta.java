package com.devPontes.LeialaoME.model.entities;

import java.time.LocalDateTime;
import java.util.List;

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
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long id;
	
	@Column
	private Double valorOferta;

	@Enumerated(EnumType.STRING)
	private StatusOferta statusOferta;

	@ManyToOne
	@JoinColumn(name = "comprador_id")
	private UsuarioComprador comprador;

	@ManyToOne
	@JoinColumn(name = "leilao_id")
	private List<Leilao> leiloesOfertados;
	
	private LocalDateTime tempoDuravel;
	
	private boolean aceita;
	
	

}
