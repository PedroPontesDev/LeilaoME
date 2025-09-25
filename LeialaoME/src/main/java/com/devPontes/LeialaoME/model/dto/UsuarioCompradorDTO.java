package com.devPontes.LeialaoME.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class UsuarioCompradorDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String biografia;
    private String urlProfilePic;

    private List<OfertaDTO> ofertas = new ArrayList<>();
    private Set<LeilaoDTO> leiloesParticipados = new HashSet<>();

    public UsuarioCompradorDTO() {}

    public UsuarioCompradorDTO(Long id, String username, String biografia, String urlProfilePic, List<OfertaDTO> ofertas,
			Set<LeilaoDTO> leiloesParticipados) {
		super();
		this.id = id;
		this.username = username;
		this.biografia = biografia;
		this.setUrlProfilePic(urlProfilePic);
		this.ofertas = ofertas;
		this.leiloesParticipados = leiloesParticipados;
	}
    
    public String getUrlProfilePic() {
		return urlProfilePic;
	}

	public void setUrlProfilePic(String urlProfilePic) {
		this.urlProfilePic = urlProfilePic;
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

    public List<OfertaDTO> getOfertas() {
        return ofertas;
    }
    public void setOfertas(List<OfertaDTO> ofertas) {
        this.ofertas = ofertas;
    }

    public Set<LeilaoDTO> getLeiloesParticipados() {
        return leiloesParticipados;
    }
    public void setLeiloesParticipados(Set<LeilaoDTO> leiloesParticipados) {
        this.leiloesParticipados = leiloesParticipados;
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
        UsuarioCompradorDTO other = (UsuarioCompradorDTO) obj;
        return Objects.equals(id, other.id);
    }

	@Override
	public String toString() {
		return "UsuarioCompradorDTO [id=" + id + ", username=" + username + ", biografia=" + biografia
				+ ", urlProfilePic=" + urlProfilePic + ", ofertas=" + ofertas + ", leiloesParticipados="
				+ leiloesParticipados + "]";
	}

    
    

	
}
