package projet.ais.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import projet.ais.config.GreenApiConfig;
import projet.ais.services.SendMessage;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin
@RequestMapping("/send")
public class GreenController {
    

    @Autowired
    GreenApiConfig greenApiConfig;
    @Autowired
    SendMessage sendMessage;

    

    @PostMapping("/sent-message")
    public String sendMessageWa(@RequestParam String numero, @RequestParam String me) throws JsonProcessingException {
        sendMessage.sendMessages(numero, me);
        return "Message envoyé avec succèss";
    }
}
