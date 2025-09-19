package com.devPontes.LeialaoME.model.entities;

import java.io.Serializable;
import java.util.Objects;

import com.devPontes.LeialaoME.model.entities.enums.UsuarioRole;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_permissao")
public class Permissao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private UsuarioRole usuarioRole;

	public Permissao() {
	}

	public Permissao(Long id, UsuarioRole usuarioRole) {
		this.id = id;
		this.usuarioRole = usuarioRole;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UsuarioRole getUsuarioRole() {
		return usuarioRole;
	}

	public void setUsuarioRole(UsuarioRole usuarioRole) {
		this.usuarioRole = usuarioRole;
	}

	@Override
	public String toString() {
		return "Permissao [id=" + id + ", usuarioRole=" + usuarioRole + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Permissao other = (Permissao) obj;
		return Objects.equals(id, other.id);
	}

}
