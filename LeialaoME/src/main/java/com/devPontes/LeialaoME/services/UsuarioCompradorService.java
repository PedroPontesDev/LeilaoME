package com.devPontes.LeialaoME.services;

import java.util.Map;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.devPontes.LeialaoME.model.DTO.v1.UsuarioDTO;
import com.devPontes.LeialaoME.model.DTO.v1.UsuarioUpdateDTO;
import com.devPontes.LeialaoME.model.entities.Usuario;


@Service
public interface UsuarioCompradorService {

	 UsuarioDTO cadastrarUsuarioComprador(UsuarioDTO novoUsuario) throws Exception;
	 UsuarioDTO atualizarUsuarioComprador(UsuarioUpdateDTO update, Long usuarioId) throws Exception;
	 Map<String, Object> fazerUploadDeImamgemDePerfil(Usuario usuarioLogado, MultipartFile file) throws Exception;
	 String escreverBiografia(Long usuarioId, String beografia);
	 String atualizarUsername(Long usuarioId, String usernameNovo);
	 String atualizarPassword(Long usuarioId, String passwordNova, String oldPassword) throws Exception;
	
	
}
