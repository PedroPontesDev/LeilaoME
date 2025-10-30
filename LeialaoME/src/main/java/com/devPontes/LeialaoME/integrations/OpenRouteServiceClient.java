package com.devPontes.LeialaoME.integrations;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OpenRouteServiceClient {

	
	private String urlORS = "https://api.openrouteservice.org/v2/matrix/driving-car";
	
	private String token = "5b3ce3597851110001cf62483438fbd348be4dc88b5e76a4c981108c";
	
	
	public Map<String, Object> verDistanciaCidadeds(Double[][] cordenadas) {
		RestTemplate restTemplate = new RestTemplate();
		ObjectMapper objMapper = new ObjectMapper();
		
		// Monta o body da requisição
        Map<String, Object> body = new HashMap<>();
        body.put("locations", cordenadas); // coordenadas em [lon, lat]
        body.put("metrics", new String[] { "distance", "duration" });
        body.put("units", "km");

        // Cabeçalhos
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            // Chamada à API ORS
            ResponseEntity<JsonNode> response = restTemplate.postForEntity(urlORS, request, JsonNode.class);
           
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {  
            
            	var obj = objMapper.readTree(response.getBody().toString());
        
               Double durations = obj.path("durations").get(0).get(1).asDouble() / 3600.0;
             
               var distances = obj.path("distances").get(0).get(1).asDouble() / 1000;
                
                Map<String, Object> result = new HashMap<>();
                result.put("durations", durations);
                result.put("distances", distances);
                
             
                return result;
            } else {
                System.out.println("Erro na requisição: " + response.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
