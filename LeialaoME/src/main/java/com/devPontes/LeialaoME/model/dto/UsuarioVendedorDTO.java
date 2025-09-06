package com.devPontes.LeialaoME.model.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UsuarioVendedorDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String biografia;
    private byte[] fotoPerfil;
    private String cnpj;

    private Set<LeilaoDTO> leiloesCadastrados = new HashSet<>();

    public UsuarioVendedorDTO() {}

    public UsuarioVendedorDTO(Long id, String username, String biografia, byte[] fotoPerfil, String cnpj) {
        this.id = id;
        this.username = username;
        this.biografia = biografia;
        this.fotoPerfil = fotoPerfil;
        this.cnpj = cnpj;
    }

    // Getters e Setters
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

    public String getBiografia() {
        return biografia;
    }
    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }
    public void setFotoPerfil(byte[] fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getCnpj() {
        return cnpj;
    }
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Set<LeilaoDTO> getLeiloesCadastrados() {
        return leiloesCadastrados;
    }
    public void setLeiloesCadastrados(Set<LeilaoDTO> leiloesCadastrados) {
        this.leiloesCadastrados = leiloesCadastrados;
    }

    // Equals e HashCode (baseados no id)
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UsuarioVendedorDTO other = (UsuarioVendedorDTO) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "UsuarioVendedorDTO [id=" + id + ", username=" + username 
                + ", cnpj=" + cnpj 
                + ", leiloesCadastrados=" + (leiloesCadastrados != null ? leiloesCadastrados.size() : 0) + "]";
    }
}
