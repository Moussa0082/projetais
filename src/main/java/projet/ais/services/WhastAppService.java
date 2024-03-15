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

import projet.ais.config.GreenApiConfig;
import projet.ais.repository.MessageWaRepository;
import projet.ais.repository.WhatsAppRepository;

@Service
public class WhastAppService {

 @Autowired
    GreenApiConfig greenApiConfig;

    @Autowired
    WhatsAppRepository whatsAppRepository;

    public String sendRapideMessage(String chatId, String message) {
        String apiKey = greenApiConfig.getApiId();
        String apiToken = greenApiConfig.getApiToken();

        var restTemplate = new RestTemplate();
        var requestUrl = new StringBuilder();
        requestUrl
                .append("https://api.greenapi.com/")
                .append("waInstance/")
                .append(apiKey)
                .append("/sendMessage/")
                .append(apiToken);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var jsonBody = "{\r\n\t\"chatId\": \"" + chatId + "@c.us\",\r\n\t\"message\": \"" + message + "\"\r\n}";

        var requestEntity = new HttpEntity<>(jsonBody, headers);

        var response = restTemplate.exchange(requestUrl.toString(), HttpMethod.POST, requestEntity, String.class);
        System.out.println(response);
        return "Message sent";
    }
}
