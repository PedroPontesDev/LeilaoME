package com.devPontes.LeialaoME.model.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UsuarioDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	private String username;
	private String password;

	private String biografia;

	private String fotoPerfil;

	private Set<PermissaoDTO> permissoes = new HashSet<>();

	private String cpf;

	private String cnpj;

	public UsuarioDTO() {
	}

	public UsuarioDTO(Long id, String username, String password, String biografia, String fotoPerfil,
			Set<PermissaoDTO> permissoes, String cpf, String cnpj) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.biografia = biografia;
		this.fotoPerfil = fotoPerfil;
		this.permissoes = permissoes;
		this.cpf = cpf;
		this.cnpj = cnpj;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBiografia() {
		return biografia;
	}

	public void setBiografia(String biografia) {
		this.biografia = biografia;
	}

	public void setPermissoes(Set<PermissaoDTO> permissoes) {
		this.permissoes = permissoes;
	}

	public String getFotoPerfil() {
		return fotoPerfil;
	}

	public void setFotoPerfil(String fotoPerfil) {
		this.fotoPerfil = fotoPerfil;
	}

	public Set<PermissaoDTO> getPermissoes() {
		return permissoes;
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
		UsuarioDTO other = (UsuarioDTO) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "UsuarioDTO [id=" + id + ", username=" + username + ", password=" + password + ", biografia=" + biografia
				+ ", fotoPerfil=" + fotoPerfil + ", permissoes=" + permissoes + ", cpf=" + cpf + "]";
	}

}
