package com.devPontes.LeialaoME.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_usuario_comprador")
@DiscriminatorValue("COMPRADOR")
public class UsuarioComprador extends Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "cpf_comprador")
	private String cpf;

	@OneToMany(mappedBy = "comprador", fetch = FetchType.EAGER)
	List<Oferta> ofertas = new ArrayList<>();
	
	@OneToMany(mappedBy = "comprador", fetch = FetchType.EAGER)
	private Set<Leilao> leiloesPaticipados = new HashSet<>();

	public UsuarioComprador(Long id, String username, String password, String biografia, String urlFotoPerfil,
			Set<Permissao> permissoes, String cpf, List<Oferta> ofertas) {
		super(id, username, password, biografia, urlFotoPerfil, permissoes);
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
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(cpf, leiloesPaticipados, ofertas);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioComprador other = (UsuarioComprador) obj;
		return Objects.equals(cpf, other.cpf) && Objects.equals(leiloesPaticipados, other.leiloesPaticipados)
				&& Objects.equals(ofertas, other.ofertas);
	}

	

	
}
