package com.devPontes.LeialaoME.services;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.devPontes.LeialaoME.model.DTO.v1.LeilaoDTO;
import com.devPontes.LeialaoME.model.DTO.v1.OfertaDTO;
import com.devPontes.LeialaoME.model.DTO.v1.UsuarioDTO;
import com.devPontes.LeialaoME.model.DTO.v1.UsuarioUpdateDTO;
import com.devPontes.LeialaoME.model.entities.Usuario;

@Service
public interface UsuarioCompradorService {

	 UsuarioDTO atualizarUsuarioComprador(UsuarioUpdateDTO update, Long usuarioId) throws Exception;
	 Map<String, Object> fazerUploadDeImamgemDePerfil(Long id, MultipartFile file) throws Exception;
	 String escreverBiografia(Long usuarioId, String beografia);
	 String atualizarUsername(Long usuarioId, String usernameNovo);
	 String atualizarPassword(Long usuarioId, String passwordNova);
	 Set<LeilaoDTO>findLeiloesAdquiridosDeUsuario(Long usuarioCompradorId);
	 Set<OfertaDTO> findOfertaMaisBaixa(String cpfComprador, Double maximumValue);
	 UsuarioDTO cadastrarUsuarioComprador(UsuarioDTO novoUsuario) throws Exception;
	 Set<OfertaDTO> findOfertasMaisCarasComprador(Usuario usuarioLogado, String cpfComprador, Double minimumValue);
	
	
}
