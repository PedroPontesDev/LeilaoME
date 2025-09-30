package com.devPontes.LeialaoME.services;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.devPontes.LeialaoME.model.dto.LeilaoDTO;
import com.devPontes.LeialaoME.model.dto.OfertaDTO;
import com.devPontes.LeialaoME.model.dto.UsuarioDTO;

@Service
public interface UsuarioCompradorService {

	 UsuarioDTO cadastrarUsuarioComprador(UsuarioDTO novoUsuario) throws Exception;
	 UsuarioDTO atualizarUsuarioComprador(UsuarioDTO update, Long usuarioId);
	 String fazerUploadDeImamgemDePerfil(Long id, MultipartFile file) throws Exception;
	 String escreverBiografia(Long usuarioId, String beografia);
	 String atualizarUsername(Long usuarioId, String usernameNovo);
	 String atualizarPassword(Long usuarioId, String passwordNova);
	 Set<LeilaoDTO>findLeiloesAdquiridosDeUsuario(Long usuarioCompradorId);
	 Set<OfertaDTO> findOfertasMaisCaras(Long userId, Double minimumValue);
	 OfertaDTO findOfertaMaisBaixa(Long userId, Double maximumValue);
}
