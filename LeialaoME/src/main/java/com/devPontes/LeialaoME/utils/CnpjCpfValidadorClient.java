package com.devPontes.LeialaoME.utils;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CnpjCpfValidadorClient {

	@Value("${JWT_SECRET_AWS}")
	String secretReceitaWS;

	private static final Logger log = LoggerFactory.getLogger(CnpjCpfValidadorClient.class);

	public boolean validarCnpj(String cnpj) throws Exception {
		// Limpar como CNPJ VEM
		String cnpjRegex = "^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$";
		String cnpjSemMascaraRegex = "^\\d{14}$";
		log.info("Trazendo o " + cnpjSemMascaraRegex + "Cnpj com regex" + cnpjRegex);

		if (cnpj == null || (!Pattern.matches(cnpjRegex, cnpj) && !Pattern.matches(cnpjSemMascaraRegex, cnpj))) throw new Exception("CNPJ inválido! O CNPJ deve estar no formato correto.");
		
		String cnpjLimpo = cnpj.replaceAll("[^0-9]", ""); // Remover a máscara do CNPJ para enviar na API
		log.info("Cnpj Limpo" + cnpjLimpo);
		
		RestTemplate restTemplate = new RestTemplate(); // Instancia o RestTemplate monta a entity
		String url = "https://receitaws.com.br/v1/cnpj/" + cnpjLimpo; // Define url de chamada
		HttpHeaders headers = new HttpHeaders(); // Define Headers
		headers.set("Authorization", "Bearer " + secretReceitaWS);
		HttpEntity<String> entidade = new HttpEntity<>(headers); // Cria a HTTP entity e usa o ResTemplate pra fazer
																	// chamadas com ResponseEntity
		ResponseEntity<String> resposta = restTemplate.exchange(url, HttpMethod.GET, entidade, String.class);
		// Deserializando
		String situacao = null;
		if (resposta.getStatusCode().is2xxSuccessful()) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(resposta.getBody());
			situacao = root.path("situacao").asText();
		}
		if ("ATIVA".equalsIgnoreCase(situacao)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean validarCPf(String cpf) {

		if (cpf == null || cpf.length() != 11 || !cpf.matches("\\d+")) {
			return false;
		}

		if (cpf.chars().distinct().count() == 1) {
			return false;
		}

		try {

			int digito1 = calcularDigito(cpf.substring(0, 9));
			int digito2 = calcularDigito(cpf.substring(0, 9) + digito1);
			return cpf.equals(cpf.substring(0, 9) + digito1 + digito2);

		} catch (Exception e) {
			return false;
		}

	}

	private static int calcularDigito(String str) {
		int soma = 0;
		int peso = str.length() + 1;

		for (char c : str.toCharArray()) {
			soma += (c - '0') * peso--;
		}

		int resto = soma % 11;

		return (resto < 2) ? 0 : 11 - resto;

	}

}
