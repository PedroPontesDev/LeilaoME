package com.devPontes.LeialaoME.services;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.devPontes.LeialaoME.model.DTO.LeilaoDTO;
import com.devPontes.LeialaoME.model.DTO.OfertaDTO;
import com.devPontes.LeialaoME.model.DTO.UsuarioDTO;
import com.devPontes.LeialaoME.model.entities.Usuario;

@Service
public interface UsuarioCompradorService {

	 UsuarioDTO cadastrarUsuarioComprador(UsuarioDTO novoUsuario) throws Exception;
	 UsuarioDTO atualizarUsuarioComprador(UsuarioDTO update, Long usuarioId);
	 Map<String, Object> fazerUploadDeImamgemDePerfil(Long id, MultipartFile file) throws Exception;
	 String escreverBiografia(Long usuarioId, String beografia);
	 String atualizarUsername(Long usuarioId, String usernameNovo);
	 String atualizarPassword(Long usuarioId, String passwordNova);
	 Set<LeilaoDTO>findLeiloesAdquiridosDeUsuario(Long usuarioCompradorId);
	 Set<OfertaDTO> findOfertaMaisBaixa(String cpfComprador, Double maximumValue);
	 Set<OfertaDTO> findOfertasMaisCaras(Usuario usuarioLogado, String cpfComprador, Double minimumValue);

}
