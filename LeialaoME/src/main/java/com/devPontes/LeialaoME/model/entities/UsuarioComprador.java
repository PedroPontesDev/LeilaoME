package com.devPontes.LeialaoME.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_usuario_comprador")
@DiscriminatorValue("COMPRADOR")
public class UsuarioComprador extends Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "cpf_comprador")
	private String cpf;

	@OneToMany(mappedBy = "comprador")
	List<Oferta> ofertas = new ArrayList<>();
	
	@OneToMany(mappedBy = "comprador")
	private Set<Leilao> leiloesPaticipados = new HashSet<>();

	public UsuarioComprador(Long id, String username, String password, String biografia, byte[] fotoPerfil,
			Set<Permissao> permissoes, String cpf, List<Oferta> ofertas) {
		super(id, username, password, biografia, fotoPerfil, permissoes);
		this.cpf = cpf;
		this.ofertas = ofertas;
	}

	public UsuarioComprador() {
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public List<Oferta> getOfertas() {
		return ofertas;
	}

	@Override
	public String toString() {
		return "UsuarioComprador [cpf=" + cpf + ", ofertas=" + ofertas + "]";
	}

}
