package com.devPontes.LeialaoME.model.dto;

public class PermissaoDTO {
    
	private String nome;
    
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
