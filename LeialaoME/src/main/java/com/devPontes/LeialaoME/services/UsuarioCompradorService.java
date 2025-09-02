package com.devPontes.LeialaoME.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.devPontes.LeialaoME.model.dto.LeilaoDTO;
import com.devPontes.LeialaoME.model.dto.UsuarioDTO;

@Service
public interface UsuarioCompradorService {

	 UsuarioDTO cadastrarUsuarioComprador(UsuarioDTO novoUsuario) throws Exception;
	 UsuarioDTO cadastrarUsuarioVendedor(UsuarioDTO novoUsuario) throws Exception;
	 UsuarioDTO atualizarUsuarioComprador(UsuarioDTO update, Long usuarioId);
	 byte[] fazerUploadDeImamgemDePerfil(MultipartFile file);
	 String escreverBiografia(Long usuarioId, String beografia);
	 String atualizarUsername(Long usuarioId, String usernameNovo);
	 String atualizarPassword(Long usuarioId, String passwordNova);
	 UsuarioDTO findOfertasMaisCaras(Long ofertaId, Long userId, String username);
	 LeilaoDTO mostrarLeiloesAdquiridos(Long leiloesId, Long usuaroCompradorId, String username);
}
