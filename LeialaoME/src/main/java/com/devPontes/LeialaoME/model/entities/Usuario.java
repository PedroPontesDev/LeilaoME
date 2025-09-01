package com.devPontes.LeialaoME.model.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
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
	private List<Permissao> permisssoes = new ArrayList<>();
	
	@OneToMany
	private Set<Leilao> leiloes;

}
