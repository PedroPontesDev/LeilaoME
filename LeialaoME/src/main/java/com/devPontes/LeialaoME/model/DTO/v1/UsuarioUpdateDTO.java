package com.devPontes.LeialaoME.model.DTO.v1;

public record UsuarioUpdateDTO (
	  String username,
      String biografia,
      String oldPassword,
      String newPassword
) {}
