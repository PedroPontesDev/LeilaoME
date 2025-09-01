package com.devPontes.LeialaoME.model.entities;

import java.math.BigDecimal;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_usuario_vendedor")
public class UsuarioVendedor extends Usuario {

	private String cnpj;
	private BigDecimal valorOferecido;

	public UsuarioVendedor(Long id, String username, String password, String biografia, byte[] fotoPerfil,
			Set<Permissao> permissoes, Set<Leilao> leiloesVendidos, Set<Leilao> leiloesComprados, String cnpj,
			BigDecimal valorOferecido) {
		super(id, username, password, biografia, fotoPerfil, permissoes, leiloesVendidos, leiloesComprados);
		this.cnpj = cnpj;
		this.valorOferecido = valorOferecido;
	}

	public UsuarioVendedor(String cnpj, BigDecimal valorOferecido) {
		this.cnpj = cnpj;
		this.valorOferecido = valorOferecido;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public BigDecimal getValorOferecido() {
		return valorOferecido;
	}

	public void setValorOferecido(BigDecimal valorOferecido) {
		this.valorOferecido = valorOferecido;
	}

	@Override
	public String toString() {
		return "UsuarioVendedor [cnpj=" + cnpj + ", valorOferecido=" + valorOferecido + "]";
	}

}
