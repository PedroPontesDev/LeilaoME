package com.devPontes.LeialaoME.model.DTO.v1;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class UsuarioVendedorDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String biografia;
    private String urlProfilePic;
    private String cnpj;

    private Set<LeilaoDTO> leiloesCadastrados = new HashSet<>();

    public UsuarioVendedorDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }

    public String getUrlProfilePic() { return urlProfilePic; }
    public void setUrlProfilePic(String urlProfilePic) { this.urlProfilePic = urlProfilePic; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public Set<LeilaoDTO> getLeiloesCadastrados() { return leiloesCadastrados; }
    public void setLeiloesCadastrados(Set<LeilaoDTO> leiloesCadastrados) { this.leiloesCadastrados = leiloesCadastrados; }
}
