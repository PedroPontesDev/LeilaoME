package com.devPontes.LeialaoME.model.DTO.v1;

import java.util.Set;

public record UsuarioDTO (
			Long id,
	        String username,
	        String password,
	        String biografia,
	        String fotoPerfil,
	        Set<PermissaoDTO> permissoes,
	        String cpf,
	        String cnpj
	) {}

