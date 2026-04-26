package com.devPontes.LeialaoME.controllers;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devPontes.LeialaoME.integrations.OpenRouteServiceClient;
import com.devPontes.LeialaoME.model.DTO.v1.CordenadasRequestDTO;

@RestController
@RequestMapping("/v1/distance")
public class OpenRouteController {

	@Autowired
	private OpenRouteServiceClient orsClient;

	/**
	 * Recebe coordenadas via POST e retorna durations/distances da ORS
	 * 
	 * Exemplo de JSON: { "coordinates": [ [-43.4667, -23.0167], [-43.6999,
	 * -22.9833] ] }
	 * 
	 * @throws Exception
	 */

	@PostMapping("/matrix")
	public ResponseEntity<?> calcularDistancia(@RequestBody CordenadasRequestDTO cordenadas) throws Exception {
		if (cordenadas == null || cordenadas.getCordenadas().size() < 2)
			throw new Exception("Cordneadas presicam ser incluidas com LAT E LONG");

		Map<String, Object> resultado = orsClient.verDistanciaCidadeds(cordenadas);

		if (resultado == null ) {
			return ResponseEntity.internalServerError().body(Map.of("error", "Não foi possível obter dados do ORS"));
		}

		return ResponseEntity.ok(resultado);
	}

}