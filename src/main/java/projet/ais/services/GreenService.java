package projet.ais.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// import com.greenapi.MessageSender;

import projet.ais.config.GreenApiConfig;
import projet.ais.models.GreenApi;
import projet.ais.repository.GreenRepository;

@Service
public class GreenService {
    
    @Autowired
    GreenApiConfig greenApiConfig;
    @Autowired
    GreenRepository greenRepository;
    

    // https://api.greenapi.com/waInstance{{idInstance}}/sendMessage/{{apiTokenInstance}}
    public void sendMessage(){
//        String apiKey = greenApiConfig.getApiId();
//        String apiToken = greenApiConfig.getApiToken();

//         var restTemplate = new RestTemplate();
//         var requestUrl = new StringBuilder();
//         requestUrl
//         .append("https://api.greenapi.com")
//         .append("/waInstance")
//         .append(apiKey)
//         .append("/sendMessage/")
//         .append(apiToken);
        
//        var headers = new HttpHeaders();
// headers.setContentType(MediaType.APPLICATION_JSON);

// var jsonBody = "{\r\n\t\"chatId\": \"11001234567@c.us\",\r\n\t\"message\": \"I use Green-API to send this message to you!\"\r\n}";

// var requestEntity = new HttpEntity<>(jsonBody, headers);

// var response = restTemplate.exchange(requestUrl.toString(), HttpMethod.POST, requestEntity, String.class);
// System.out.println(response);
    }
}
