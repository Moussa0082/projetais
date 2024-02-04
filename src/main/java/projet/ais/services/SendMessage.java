package projet.ais.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import projet.ais.config.GreenApiConfig;
import projet.ais.models.MessageWa;
import projet.ais.repository.MessageWaRepository;

@Service
public class SendMessage {
    
    @Autowired
    GreenApiConfig greenApiConfig;
    @Autowired
    MessageWaRepository messageRepository;

    
public String sendMessages(String whatsAppActeur, String msg) throws Exception {
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
    requestBody.put("message", msg);

    // Convertir la structure de données en JSON
    var jsonBody = new ObjectMapper().writeValueAsString(requestBody);

    var requestEntity = new HttpEntity<>(jsonBody, headers);

    var response = restTemplate.exchange(requestUrl.toString(), HttpMethod.POST, requestEntity, String.class);

    if (response.getStatusCode() == HttpStatus.OK) {
    try{
        // MessageWa messageWa = new MessageWa();
        // messageWa.setActeurConcerner("Ibrahim sy");
        // messageWa.setText(msg);
        // messageRepository.save(messageWa);
    }catch(Exception e){
        throw new Exception("Erreur lors de l'enregistrement");
        }
    } else {
        // Si le statut n'est pas 200, vous pouvez imprimer le statut et le corps de la réponse pour le débogage.
        System.out.println("Échec de l'envoi du message. Statut : " + response.getStatusCode());
        System.out.println("Réponse du serveur : " + response.getBody());
        // Ajoutez ici le code pour effectuer une autre opération en cas d'échec.
    }

    return "Message envoyé avec succès";
}

}

