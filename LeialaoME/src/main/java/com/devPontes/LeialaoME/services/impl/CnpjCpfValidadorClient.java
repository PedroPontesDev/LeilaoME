package com.devPontes.LeialaoME.services.impl;

import org.springframework.http.*;
import java.util.regex.Pattern;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CnpjCpfValidadorClient {

	@Value("${JWT_SECRET_AWS}")
	String secretReceitaWS;
	
	private static final Logger log = LoggerFactory.getLogger(CnpjCpfValidadorClient.class);

	public boolean validarCnpj(String cnpj) throws Exception {

		// Limpar mascara de como CNPJ VEM

		String cnpjRegex = "^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$";
		String cnpjSemMascaraRegex = "^\\d{14}$";
	
		log.info("Trazendo o " + cnpjSemMascaraRegex + "Cnpj com regex" + cnpjRegex);
		
		if (cnpj == null || (!Pattern.matches(cnpjRegex, cnpj) && !Pattern.matches(cnpjSemMascaraRegex, cnpj))) {
			throw new Exception("CNPJ inválido! O CNPJ deve estar no formato correto.");
		}

		// Remover a máscara do CNPJ para enviar na API
		String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");
		System.out.println("CNPJ limpo: " + cnpjLimpo); // Log do CNPJ limpo para depuração
		
		log.info("Cnpj Limpo" + cnpjLimpo);
		
		// Instancia o RestTemplate monta a entity

		RestTemplate restTemplate = new RestTemplate();

		// Define url de chamada
		String url = "https://receitaws.com.br/v1/cnpj/" + cnpjLimpo;

		// Define Headers
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + secretReceitaWS);

		log.info(headers.toString());
		
		// Cria a HTTP entity e usa o ResTemplate pra fazer chamadas com ResponseEntity

		HttpEntity<String> entidade = new HttpEntity<>(headers);

		ResponseEntity<String> resposta = restTemplate.exchange(url, HttpMethod.GET, entidade, String.class);

		// Deserializando

		ObjectMapper mapper = new ObjectMapper();

		JsonNode node = mapper.readTree(resposta.getBody());

		Boolean cpfReal = node.get("status").asText().equals("OK");
		
		log.info(cpfReal.toString());

		if (!cpfReal)
			throw new Exception("CPF NÃO É REAL");
		return cpfReal;

	}
}
