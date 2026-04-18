package com.devPontes.LeialaoME.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;

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

        LocalDateTime agora = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        
        if (!leilao.isAindaAtivo()) {
            throw new LeilaoEncerradoException("Leilão encerrado ou desativado!");
        }
        
        if(agora.isBefore(leilao.getInicio())) {
        	throw new LeilaoException("Leilão ainda nçao começou!");
        }
        
        if(agora.isAfter(leilao.getTermino())) {
        	throw new LeilaoException("Não é possivel fazer proposta para um leilão que já terminou!");
        }

        UsuarioComprador comprador = (UsuarioComprador) compradorRepository.findById(usuarioLogado.getId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado com ID " + usuarioLogado.getId()));

        Oferta oferta = MyMaper.parseObject(ofertaDTO, Oferta.class);
       
        if (oferta.getValorOferta() == null || oferta.getValorOferta() <= 0) {
            throw new LeilaoException("O valor da oferta deve ser maior que zero.");
        }
        
        oferta.setComprador(comprador);
        oferta.setStatusOferta(StatusOferta.ATIVA);
        oferta.setMomentoOferta(agora);
        
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
    public Double calcularNovoLance(Long leilaoId) {
        Leilao leilao = leilaoRepository.findById(leilaoId)
                .orElseThrow(() -> new LeilaoException("Leilão não encontrado com ID " + leilaoId));
        
        Double maiorOferta = leilao.getOfertas()
        						   .stream()
        						   .filter(o -> o.getStatusOferta() == StatusOferta.ATIVA)
        						   .map(o -> o.getValorOferta())
        						   .max(Double::compareTo)
        						   .orElse(null);
        if(maiorOferta == null) {
            return leilao.getLanceInicial();
        }
        
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

        if (oferta.getStatusOferta() != StatusOferta.ATIVA
                && oferta.getStatusOferta() != StatusOferta.PENDENTE) {
            throw new LeilaoException("Somente ofertas ativas ou pendentes podem ser aceitas.");
        }
        
        for(Oferta ofertas : leilao.getOfertas()) {
        	if(ofertas.getId().equals(oferta.getId())) {
        		ofertas.setStatusOferta(StatusOferta.ACEITA);
        	} else if(ofertas.getStatusOferta() == StatusOferta.ATIVA 
        				|| ofertas.getStatusOferta() == StatusOferta.PENDENTE) {
        		ofertas.setStatusOferta(StatusOferta.INATIVA);
        	}
        }
        
        leilao.setAindaAtivo(false);
        leilao.setComprador(oferta.getComprador());
        leilao.setLanceInicial(oferta.getValorOferta());
        
        ofertaRepository.saveAll(leilao.getOfertas());
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

	    if (ofertaLeilao.getStatusOferta() != StatusOferta.ATIVA
	            && ofertaLeilao.getStatusOferta() != StatusOferta.PENDENTE) {
	        throw new LeilaoException("Somente ofertas ativas ou pendentes podem ser negadas.");
	    }

		ofertaLeilao.setStatusOferta(StatusOferta.NEGADA);
		
		ofertaRepository.save(ofertaLeilao);
		
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
		String cpf = ((UsuarioComprador) usuarioLogado).getCpf();
		List<Oferta> ofertas = ofertaRepository.findOfertasMenoresComprador(cpf, valorMaximo);
		return MyMaper.parseListObjects(ofertas, OfertaDTO.class);
	}

}