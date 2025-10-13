package com.devPontes.LeialaoME.model.DTO;

import com.devPontes.LeialaoME.model.entities.enums.UsuarioRole;

public class PermissaoDTO {
    
	private UsuarioRole roles;
    
    public PermissaoDTO() {}

    public PermissaoDTO(UsuarioRole roles) {
 		this.roles = roles;
 	}

	public UsuarioRole getRoles() {
		return roles;
	}

	public void setRoles(UsuarioRole roles) {
		this.roles = roles;
	}

}