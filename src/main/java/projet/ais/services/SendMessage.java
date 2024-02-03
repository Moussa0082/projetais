package projet.ais.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import projet.ais.config.GreenApiConfig;

@Service
public class SendMessage {
    
    @Autowired
    GreenApiConfig greenApiConfig;

    
public String sendMessages(String whatsAppActeur, String msg) throws JsonProcessingException {
    String apiKey = greenApiConfig.getApiId();
    String apiToken = greenApiConfig.getApiToken();

    var restTemplate = new RestTemplate();
    var requestUrl = new StringBuilder();
    requestUrl
            .append("https://api.greenapi.com")
            .append("/waInstance")
            .append(apiKey)
            .append("/sendMessage/")
            .append(apiToken);

    var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Utiliser une structure de données, comme une Map, pour stocker les valeurs dynamiques
    Map<String, String> requestBody = new HashMap<>();
    requestBody.put("chatId", whatsAppActeur + "@c.us");
    requestBody.put("message",msg);

    // Convertir la structure de données en JSON
    var jsonBody = new ObjectMapper().writeValueAsString(requestBody);

    var requestEntity = new HttpEntity<>(jsonBody, headers);

    var response = restTemplate.exchange(requestUrl.toString(), HttpMethod.POST, requestEntity, String.class);
    System.out.println(response);

    return "Message envoyé avec succès";
}
}

