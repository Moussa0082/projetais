package projet.ais.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import projet.ais.models.Commande;
import projet.ais.models.Stock;
import projet.ais.services.CommandeService;

@RestController
@RequestMapping("/commande")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;


    @GetMapping("/add")
    public ResponseEntity<String> ajouterStocksACommande(@RequestBody Commande commande, @RequestBody List<Stock> stocks) {
        try {
            commandeService.ajouterStocksACommande(commande, stocks);
            return ResponseEntity.ok("Stocks ajoutés à la commande avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue lors de l'ajout des stocks à la commande : " + e.getMessage());
        }
    }
    
}
