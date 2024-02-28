package projet.ais.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import projet.ais.models.Acteur;
import projet.ais.models.CategorieProduit;
import projet.ais.models.MessageWa;
import projet.ais.services.MessageService;

import org.springframework.web.bind.annotation.PostMapping;


@RestController
@CrossOrigin
@RequestMapping("api-koumi/send")

public class MessageController {

    @Autowired
    MessageService messageService;

    @PostMapping("/sendAndSaveMessages")
    public ResponseEntity<String> sendAndSaveMessagess(
            @RequestParam String whatsAppActeur,
            @RequestParam String message) {
        
        try {
            // Appeler la méthode du service pour envoyer et enregistrer le message
            MessageWa savedMessage = messageService.sendMessagePersonnalAndSave(whatsAppActeur, message);
            return ResponseEntity.ok("Message envoyé et enregistré avec succès. ID du message : " + savedMessage.getIdMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/readAllMessage")
    @Operation(summary="Récuperation de tout les messages")
    public ResponseEntity<List<MessageWa>> getAllMessages() {
        return new ResponseEntity<>(messageService.getAllMessage(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary="Suppression des messages")
    public String deleteMessage(@PathVariable String id) {
        return messageService.deleteMessage(id);
    }

    @DeleteMapping("/deleteAllMessage")
    @Operation(summary="Suppression de tout les message")
    public ResponseEntity<Void> deleteAllMessages() {
        try {
            messageService.deleteAllMessages();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


