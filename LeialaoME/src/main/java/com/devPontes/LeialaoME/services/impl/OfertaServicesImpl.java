package com.devPontes.LeialaoME.services.impl;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devPontes.LeialaoME.exceptions.LeilaoEncerradoException;
import com.devPontes.LeialaoME.exceptions.LeilaoException;
import com.devPontes.LeialaoME.exceptions.UsuarioNaoEncontradoException;
import com.devPontes.LeialaoME.model.DTO.v1.OfertaDTO;
import com.devPontes.LeialaoME.model.entities.Leilao;
import com.devPontes.LeialaoME.model.entities.Oferta;
import com.devPontes.LeialaoME.model.entities.Usuario;
import com.devPontes.LeialaoME.model.entities.UsuarioComprador;
import com.devPontes.LeialaoME.model.entities.enums.StatusOferta;
import com.devPontes.LeialaoME.model.entities.mapper.MyMaper;
import com.devPontes.LeialaoME.repositories.LeilaoRepositories;
import com.devPontes.LeialaoME.repositories.OfertaRepositories;
import com.devPontes.LeialaoME.repositories.UsuarioCompradorRepositories;
import com.devPontes.LeialaoME.services.OfertaService;

import jakarta.transaction.Transactional;

@Service
public class OfertaServicesImpl implements OfertaService {

    @Autowired
    private LeilaoRepositories leilaoRepository;

    @Autowired
    private OfertaRepositories ofertaRepository;

    @Autowired
    private UsuarioCompradorRepositories compradorRepository;

    @Override
    public OfertaDTO fazerPropostaParaLeilao(OfertaDTO ofertaDTO, Long leilaoId, Usuario usuarioLogado) {
        Leilao leilao = leilaoRepository.findById(leilaoId)
                .orElseThrow(() -> new LeilaoException("Leilão não encontrado com ID " + leilaoId));

        if (!leilao.isAindaAtivo()) {
            throw new LeilaoEncerradoException("Leilão encerrado ou desativado!");
        }

        UsuarioComprador comprador = (UsuarioComprador) compradorRepository.findById(usuarioLogado.getId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado com ID " + usuarioLogado.getId()));

        Oferta oferta = MyMaper.parseObject(ofertaDTO, Oferta.class);
        oferta.setComprador(comprador);
        oferta.setStatusOferta(StatusOferta.ATIVA);
        oferta.setMomentoOferta(ofertaDTO.getMomentoOferta());

        if (oferta.getMomentoOferta() == null) {
            oferta.setMomentoOferta(LocalDateTime.now());
        }

        if (oferta.getMomentoOferta().isAfter(leilao.getTermino())) {
            throw new LeilaoException("Oferta fora do período do leilão!");
        }

        Double lanceMinimo = calcularNovoLance(leilaoId);

        if (oferta.getValorOferta() < lanceMinimo) {
            throw new LeilaoException("O valor da oferta deve ser igual ou maior que: " + lanceMinimo);
        }

        oferta.setLeilao(leilao);
        leilao.getOfertas().add(oferta);

        ofertaRepository.save(oferta);
        leilaoRepository.save(leilao);

        return MyMaper.parseObject(oferta, OfertaDTO.class);
    }

    @Override
    @Transactional
    public OfertaDTO fazerNovoLanceCasoOfertasSubam(Double novoValor, Long leilaoId, Long compradorId) {
        Leilao leilao = leilaoRepository.findById(leilaoId)
                .orElseThrow(() -> new LeilaoException("Leilão não encontrado com ID " + leilaoId));

        if (!leilao.isAindaAtivo()) {
            throw new LeilaoEncerradoException("Leilão encerrado ou desativado!");
        }

        UsuarioComprador comprador = (UsuarioComprador) compradorRepository.findById(compradorId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado com ID " + compradorId));

        Oferta ofertaMaisAlta = leilao.getOfertas()
                .stream()
                .filter(o -> o.getStatusOferta() == StatusOferta.ATIVA)
                .max(Comparator.comparingDouble(Oferta::getValorOferta))
                .orElseThrow(() -> new IllegalArgumentException("Nenhuma oferta encontrada"));

        Double lanceMinimo = calcularNovoLance(leilaoId);

        if (novoValor < lanceMinimo) {
            throw new IllegalArgumentException("O lance mínimo permitido é de R$ " + lanceMinimo);
        }

        if (ofertaMaisAlta != null && novoValor <= ofertaMaisAlta.getValorOferta()) {
            throw new IllegalArgumentException("Valor inserido não supera o lance mais alto!");
        }
     
        Oferta novaOferta = new Oferta();
        novaOferta.setValorOferta(novoValor);
        novaOferta.setMomentoOferta(LocalDateTime.now());
        novaOferta.setStatusOferta(StatusOferta.ATIVA);
        novaOferta.setLeilao(leilao);
        novaOferta.setComprador(comprador);

        leilao.getOfertas().add(novaOferta);
        ofertaRepository.save(novaOferta);
        leilaoRepository.save(leilao);

        return MyMaper.parseObject(novaOferta, OfertaDTO.class);
    }

    @Override
    public Double calcularNovoLance(Long leilaoId) {
        Leilao leilao = leilaoRepository.findById(leilaoId)
                .orElseThrow(() -> new LeilaoException("Leilão não encontrado com ID " + leilaoId));
       
        Double maiorOferta = leilao.getOfertas()
        						   .stream()
        						   .filter(o -> o.getStatusOferta() == StatusOferta.ACEITA || o.getStatusOferta() == StatusOferta.ATIVA)
        						   .mapToDouble(o -> o.getValorOferta())
        						   .max()
        						   .orElseThrow(() -> new LeilaoException("Nenhuma oferta encontada"));
        
        return maiorOferta + leilao.getValorDeIncremento();
          
    }
    
    
    @Override
    @Transactional
    public OfertaDTO aceitarOfertaDeLeilao(Usuario usuarioLogado, Long leilaoId, Long ofertaId) {
        Leilao leilao = leilaoRepository.findById(leilaoId)
                .orElseThrow(() -> new LeilaoException("Leilão não encontrado para o ID " + leilaoId));

        if (!leilao.getVendedor().getId().equals(usuarioLogado.getId())) {
            throw new SecurityException("Você não é o dono deste leilão!");
        }

        Oferta oferta = ofertaRepository.findById(ofertaId)
                .orElseThrow(() -> new IllegalArgumentException("Oferta não encontrada para o ID " + ofertaId));

        if (!oferta.getLeilao().getId().equals(leilaoId)) {
            throw new IllegalArgumentException("Esta oferta não pertence a este leilão!");
        }

        oferta.setStatusOferta(StatusOferta.ACEITA);
        leilao.setComprador(oferta.getComprador());
        leilao.setLanceInicial(oferta.getValorOferta());
        leilao.setAindaAtivo(false);
        leilaoRepository.save(leilao);

        return MyMaper.parseObject(oferta, OfertaDTO.class);
    }
    
    @Override
	public OfertaDTO negarOfertaDeLeilao(Usuario usuarioLogado, Long ofertaId, Long leilaoId) {
		Leilao leilaoOferta = leilaoRepository.findById(leilaoId)
											   .orElseThrow(() -> new LeilaoException("Leilão não encontrado!"));
		
		Oferta ofertaLeilao = ofertaRepository.findById(ofertaId)
											.orElseThrow(() -> new LeilaoException("Oferta não encontrada!"));
		
		if(!leilaoOferta.getVendedor().getId().equals(usuarioLogado.getId())) {
			throw new IllegalArgumentException("Somente vendedores podem negr ofertas!");
		}
		
		if(!ofertaLeilao.getLeilao().getId().equals(leilaoOferta.getId())) {
			throw new IllegalArgumentException("Esta oferta não pertence a este leilão!");
		}
		
		ofertaLeilao.setLeilao(leilaoOferta);
		ofertaLeilao.setStatusOferta(StatusOferta.NEGADA);
		return MyMaper.parseObject(ofertaLeilao, OfertaDTO.class);
	
    }
   

    @Override
    public List<OfertaDTO> findOfertasMaisCarasDeComprador(Usuario usuarioLogado, Double valorMinimo) {
        String cpf = ((UsuarioComprador) usuarioLogado).getCpf();
        List<Oferta> ofertas = ofertaRepository.findOfertasMaisCarasDeComprador(cpf, valorMinimo);
        return MyMaper.parseListObjects(ofertas, OfertaDTO.class);
      
    }

	@Override
	public List<OfertaDTO> findOfertasMaisBaixasDeComprador(Usuario usuarioLogado, Double valorMaximo) {
		// TODO Auto-generated method stub
		return null;
	}

}