package com.devPontes.LeialaoME.services;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.devPontes.LeialaoME.model.dto.LeilaoDTO;
import com.devPontes.LeialaoME.model.dto.OfertaDTO;
import com.devPontes.LeialaoME.model.dto.UsuarioDTO;

@Service
public interface UsuarioVendedorService {

	 UsuarioDTO atualizarUsuarioVendedor(UsuarioDTO update, Long usuarioId);
	 Map<String, Object> fazerUploadDeImamgemDePerfil(Long id, MultipartFile file) throws Exception;
	 String escreverBiografia(Long usuarioId, String beografia);
	 String atualizarUsername(Long usuarioId, String usernameNovo);
	 String atualizarPassword(Long usuarioId, String passwordNova);
	 Set<LeilaoDTO>findLeiloesParticipados(Long usuarioCompradorId);
	 Set<OfertaDTO> findOfertasMaisCarasRecebidas(String cnpjComprador, Double minimumValue);
	 Set<OfertaDTO> findOfertaMaisBaixaRecebida(String cnpjComprador, Double maximumValue);

}
