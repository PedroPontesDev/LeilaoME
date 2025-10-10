package com.devPontes.LeialaoME.services;

import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.model.dto.OfertaDTO;

@Service
public interface OfertaService {

	OfertaDTO fazerPropostaParaLeilao(OfertaDTO oferta, Long leilaoId, Long compradorId);

	OfertaDTO fazerNovoLanceCasoOfertasSubam(Double novoValor, Long leilaoId, Long compradorId);

	Double calcularNovoLanceMinimo(Long leilaoId);

	OfertaDTO aceitarOfertaDeLeilao(Long usarioVendedorId, Long leilaoId, Long ofertaId);

	OfertaDTO negarOfertaDeLeilao(Long usarioVendedorId, Long LeilaoId, Long ofertaId);
	
	OfertaDTO findOfertasMaisCarasDeComprador(String cpfComprador, Double valorMinimo);
	
	OfertaDTO findOfertasMaisCarasRecebidasDeVendedor(String cnpjVendedor, Double valorMinimo);

}
