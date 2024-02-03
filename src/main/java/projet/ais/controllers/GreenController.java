package projet.ais.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import projet.ais.config.GreenApiConfig;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@CrossOrigin
@RequestMapping("/send")
public class GreenController {
    

     @Autowired
    GreenApiConfig greenApiConfig;

    

    @PostMapping("/sent-message")
    public String sendMessages(){
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
        
        var jsonBody = "{\r\n\t\"chatId\": \"22383496674@c.us\",\r\n\t\"message\": \"I use Green-API to send this message to you!\"\r\n}";
        
        var requestEntity = new HttpEntity<>(jsonBody, headers);
        
        var response = restTemplate.exchange(requestUrl.toString(), HttpMethod.POST, requestEntity, String.class);
        System.out.println(response);
            
        return "Message envoyé avec succéss";
    }
}
