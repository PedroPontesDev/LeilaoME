package com.devPontes.LeialaoME.model.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_leilao")
public class Leilao {

	@Id
	@GeneratedValue
	private Long id;

	@Lob
	private String descricao;

	@Lob
	private byte[] fotoProduto;

	@ManyToOne
	@JoinColumn(name = "comprador_id")
	private Usuario comprador;

	@ManyToOne
	@JoinColumn(name = "vendedor_id")
	private UsuarioVendedor vendedor;

	private BigDecimal valorIncrementoAposLance;

	private BigDecimal valorInicial;

	private LocalDateTime inicio;

}
