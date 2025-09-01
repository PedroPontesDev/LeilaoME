package com.devPontes.LeialaoME.model.entities;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_usuario")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_usuario")
public abstract class Usuario implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;
	private String password;

	@Lob
	private String biografia;

	@Lob
	private byte[] fotoPerfil;

	@ManyToMany
	private Set<Permissao> permissoes = new HashSet<>();

	@OneToMany(fetch = FetchType.EAGER)
	private Set<Leilao> leiloesVendidos = new HashSet<>();;

	@OneToMany(fetch = FetchType.EAGER)
	private Set<Leilao> leiloesComprados = new HashSet<>();

	public Usuario() {
	}

	public Usuario(Long id, String username, String password, String biografia, byte[] fotoPerfil,
			Set<Permissao> permissoes, Set<Leilao> leiloesVendidos, Set<Leilao> leiloesComprados) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.biografia = biografia;
		this.fotoPerfil = fotoPerfil;
		this.permissoes = permissoes;
		this.leiloesVendidos = leiloesVendidos;
		this.leiloesComprados = leiloesComprados;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return permissoes.stream()
                .map(p -> (GrantedAuthority) () -> p.getUsuarioRole().name())
                .toList();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return UserDetails.super.isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return UserDetails.super.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return UserDetails.super.isCredentialsNonExpired();
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return UserDetails.super.isEnabled();
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

	public byte[] getFotoPerfil(MultipartFile file) throws IOException {
		return file.getBytes();
	}

	public void setFotoPerfil(byte[] fotoPerfil) {
		this.fotoPerfil = fotoPerfil;
	}

	public Set<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(Set<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	public Set<Leilao> getLeiloesVendidos() {
		return leiloesVendidos;
	}

	public void setLeiloesVendidos(Set<Leilao> leiloesVendidos) {
		this.leiloesVendidos = leiloesVendidos;
	}

	public Set<Leilao> getLeiloesComprados() {
		return leiloesComprados;
	}

	public void setLeiloesComprados(Set<Leilao> leiloesComprados) {
		this.leiloesComprados = leiloesComprados;
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
		Usuario other = (Usuario) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", username=" + username + ", password=" + password + ", biografia=" + biografia
				+ ", fotoPerfil=" + Arrays.toString(fotoPerfil) + ", permissoes=" + permissoes + ", leiloesVendidos="
				+ leiloesVendidos + ", leiloesComprados=" + leiloesComprados + "]";
	}

	
	
}
