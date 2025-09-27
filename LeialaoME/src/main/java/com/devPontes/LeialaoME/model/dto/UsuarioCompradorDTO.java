package com.devPontes.LeialaoME.model.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.devPontes.LeialaoME.model.entities.Permissao;
import com.devPontes.LeialaoME.model.entities.Usuario;

public class UsuarioCompradorDTO extends Usuario {
	private static final long serialVersionUID = 1L;

	private String biografia;
	private String urlProfilePic;

	private List<OfertaDTO> ofertas = new ArrayList<>();
	private Set<LeilaoDTO> leiloesParticipados = new HashSet<>();

	public UsuarioCompradorDTO() {
	}

	public UsuarioCompradorDTO(Long id, String username, String password, String biografia, String urlFotoPerfil,
			Set<Permissao> permissoes, String urlProfilePic, List<OfertaDTO> ofertas,
			Set<LeilaoDTO> leiloesParticipados) {
		super(id, username, password, biografia, urlFotoPerfil, permissoes);
		this.urlProfilePic = urlProfilePic;
		this.ofertas = ofertas;
		this.leiloesParticipados = leiloesParticipados;
	}

	public String getUrlProfilePic() {
		return urlProfilePic;
	}

	public void setUrlProfilePic(String urlProfilePic) {
		this.urlProfilePic = urlProfilePic;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(biografia, leiloesParticipados, ofertas, urlProfilePic);
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
		UsuarioCompradorDTO other = (UsuarioCompradorDTO) obj;
		return Objects.equals(biografia, other.biografia)
				&& Objects.equals(leiloesParticipados, other.leiloesParticipados)
				&& Objects.equals(ofertas, other.ofertas) && Objects.equals(urlProfilePic, other.urlProfilePic);
	}

	@Override
	public String toString() {
		return "UsuarioCompradorDTO [biografia=" + biografia + ", urlProfilePic=" + urlProfilePic + ", ofertas="
				+ ofertas + ", leiloesParticipados=" + leiloesParticipados + "]";
	}

	
	
}
