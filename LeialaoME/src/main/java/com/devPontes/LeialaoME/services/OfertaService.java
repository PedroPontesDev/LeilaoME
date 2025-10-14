package com.devPontes.LeialaoME.services;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.model.DTO.OfertaDTO;
import com.devPontes.LeialaoME.model.entities.Usuario;

@Service
public interface OfertaService {

	OfertaDTO fazerPropostaParaLeilao(OfertaDTO oferta, Long leilaoId, Usuario usuarioLogado);

	OfertaDTO fazerNovoLanceCasoOfertasSubam(Double novoValor, Long leilaoId, Long compradorId);

	Double calcularNovoLanceMinimo(Long leilaoId);

	OfertaDTO aceitarOfertaDeLeilao(Usuario usuarioLogado, Long leilaoId, Long ofertaId);

	OfertaDTO negarOfertaDeLeilao(Usuario usuarioLogado, Long LeilaoId, Long ofertaId);
	
	Set<OfertaDTO> findOfertasMaisCarasDeComprador(Usuario usuarioLogado, String cpfComprador, Double valorMinimo);
	
	Set<OfertaDTO> findOfertasMaisCarasRecebidasDeVendedor(Usuario usuarioLogado, String cnpjVendedor, Double valorMinimo);

}
