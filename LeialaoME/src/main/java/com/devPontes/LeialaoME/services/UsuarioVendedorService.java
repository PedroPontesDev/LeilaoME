package com.devPontes.LeialaoME.services;

import java.util.List;
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
public interface UsuarioVendedorService {

	 UsuarioDTO atualizarUsuarioVendedor(UsuarioUpdateDTO update, Long usuarioId) throws Exception;
	 Map<String, Object> fazerUploadDeImamgemDePerfil(Long id, MultipartFile file) throws Exception;
	 String escreverBiografia(Long usuarioId, String beografia);
	 String atualizarUsername(Long usuarioId, String usernameNovo);
	 String atualizarPassword(Long usuarioId, String passwordNova, String passwordAntiga);
	 Set<LeilaoDTO>findLeiloesParticipados(Long usuarioCompradorId);
	 Set<OfertaDTO> findOfertasMaisCarasRecebidas(Usuario usuarioLogado, String cnpjComprador, Double minimumValue);
	 Set<OfertaDTO> findOfertaMaisBaixaRecebida(Usuario usuarioLogado, String cnpjComprador, Double maximumValue);
	 
	

}
