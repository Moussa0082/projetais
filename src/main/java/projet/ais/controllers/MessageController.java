package projet.ais.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import projet.ais.config.GreenApiConfig;
import projet.ais.models.MessageWa;
import projet.ais.services.MessageService;
import projet.ais.services.SendMessage;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin
@RequestMapping("/send")

public class MessageController {

    @Autowired
    MessageService messageService;

    @PostMapping("/sendAndSaveMessages")
    public ResponseEntity<String> sendAndSaveMessage(
            @RequestParam String whatsAppActeur,
            @RequestParam String message) {
        
        try {
            // Appeler la méthode du service pour envoyer et enregistrer le message
            MessageWa savedMessage = messageService.sendMessageAndSave(whatsAppActeur, message);
            return ResponseEntity.ok("Message envoyé et enregistré avec succès. ID du message : " + savedMessage.getIdMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());
        }
    }
}


