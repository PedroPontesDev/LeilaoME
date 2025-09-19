package com.devPontes.LeialaoME.services;

import java.util.Map;

public interface PagamentoService {

	Map<Object, String> gerarCobrancaPix(String chavePix, double valor);
	
}
