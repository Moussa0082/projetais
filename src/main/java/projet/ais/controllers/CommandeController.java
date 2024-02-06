package projet.ais.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;

import org.springframework.web.bind.annotation.GetMapping;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.type.TypeReference;


import projet.ais.models.Commande;
import projet.ais.models.Stock;
import projet.ais.services.CommandeService;

@RestController
@RequestMapping("/commande")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;


    @PostMapping("/add")
    public ResponseEntity<String> ajouterStocksACommande( @RequestBody Commande commande, @RequestBody List<Stock> stocks) {
        // Commande commande = objectMapper.convertValue(requestBody.get("commande"), Commande.class);
        // List<Stock> stocks = objectMapper.convertValue(requestBody.get("stocks"), new TypeReference<List<Stock>>() {});
        
        try {
            // Appel de la méthode ajouterStocksACommande avec la commande et la liste des stocks
            commandeService.ajouterStocksACommande(commande, stocks);
            
            // Si la méthode ajouterStocksACommande réussit sans exception, retournez une réponse OK
            return ResponseEntity.ok("Stocks ajoutés à la commande avec succès.");
        } catch (Exception e) {
            // Si une exception est levée lors de l'exécution de la méthode ajouterStocksACommande, retournez une réponse d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur est survenue lors de l'ajout des stocks à la commande : " + e.getMessage());
        }
    }
    

    
}
