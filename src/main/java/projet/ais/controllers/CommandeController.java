package projet.ais.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;


import java.util.logging.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.type.TypeReference;


import projet.ais.models.Commande;
import projet.ais.models.CommandeAvecStocksDTO;
import projet.ais.models.DetailCommande;
import projet.ais.models.Stock;
import projet.ais.services.CommandeService;

@RestController
@RequestMapping("/commande")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;


    @PostMapping("/add")
    public ResponseEntity<String> ajouterCommandeAvecStocks(@RequestBody Commande commande) {
        try {
            commandeService.creerCommandeAvecStocks(commande);
             return ResponseEntity.ok("Commande ajoutée avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Une erreur est survenue lors de l'ajout de la commande : " + e.getMessage());
        }
    }
    

    

    
}
