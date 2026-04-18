package com.devPontes.LeialaoME.services;


import java.util.List;

import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.model.DTO.v1.OfertaDTO;
import com.devPontes.LeialaoME.model.entities.Usuario;

@Service
public interface OfertaService {

	OfertaDTO fazerPropostaParaLeilao(OfertaDTO oferta, Long leilaoId, Usuario usuarioLogado);

	OfertaDTO aceitarOfertaDeLeilao(Usuario usuarioLogado, Long leilaoId, Long ofertaId);
	
	OfertaDTO negarOfertaDeLeilao(Usuario usuarioLogado, Long ofertaId, Long leilaoId);
	
	List<OfertaDTO> findOfertasMaisCarasDeComprador(Usuario usuarioLogado, Double valorMinimo);
	
	List<OfertaDTO> findOfertasMaisBaixasDeComprador(Usuario usuarioLogado, Double valorMaximo);

	Double calcularNovoLance(Long leilaoId);


	



}
