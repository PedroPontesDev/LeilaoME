package com.devPontes.LeialaoME.model.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_usuario_vendedor")
@DiscriminatorValue("VENDEDOR")
public class UsuarioVendedor extends Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	private String cnpj;

	@OneToMany(mappedBy = "vendedor")
	Set<Leilao> leilaoCadastrado = new HashSet<>();

	public UsuarioVendedor(String cnpj, Set<Leilao> leilaoCadastrado) {
		this.cnpj = cnpj;
		this.leilaoCadastrado = leilaoCadastrado;
	}

	public UsuarioVendedor() {

	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	
	

	public Set<Leilao> getLeilaoCadastrado() {
		return leilaoCadastrado;
	}

	public void setLeilaoCadastrado(Set<Leilao> leilaoCadastrado) {
		this.leilaoCadastrado = leilaoCadastrado;
	}

	@Override
	public String toString() {
		return "UsuarioVendedor [cnpj=" + cnpj + ", leilaoCadastrado=" + leilaoCadastrado + "]";
	}

}
