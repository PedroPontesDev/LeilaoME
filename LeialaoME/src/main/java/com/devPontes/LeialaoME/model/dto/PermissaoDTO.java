package com.devPontes.LeialaoME.model.dto;

import com.devPontes.LeialaoME.model.entities.enums.StatusOferta;

public class PermissaoDTO {
    
	private String nome;
    private StatusOferta status; 
    
    // Construtor padrão necessário
    public PermissaoDTO() {}

    // Construtor opcional
    public PermissaoDTO(String nome) {
        this.nome = nome;
    }
  
    // getters e setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
