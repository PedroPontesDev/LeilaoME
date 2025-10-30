package com.devPontes.LeialaoME.model.DTO.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UsuarioCompradorDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String biografia;
    private String urlProfilePic;
    private String cpf;

    private List<OfertaDTO> ofertas = new ArrayList<>();
    private Set<LeilaoDTO> leiloesParticipados = new HashSet<>();

    public UsuarioCompradorDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }

    public String getUrlProfilePic() { return urlProfilePic; }
    public void setUrlProfilePic(String urlProfilePic) { this.urlProfilePic = urlProfilePic; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public List<OfertaDTO> getOfertas() { return ofertas; }
    public void setOfertas(List<OfertaDTO> ofertas) { this.ofertas = ofertas; }

    public Set<LeilaoDTO> getLeiloesParticipados() { return leiloesParticipados; }
    public void setLeiloesParticipados(Set<LeilaoDTO> leiloesParticipados) { this.leiloesParticipados = leiloesParticipados; }
}
