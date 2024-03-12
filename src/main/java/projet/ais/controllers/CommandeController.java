package projet.ais.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;


import java.util.logging.Logger;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import projet.ais.models.Commande;
import projet.ais.models.CommandeAvecStocks;
import projet.ais.models.Stock;
import projet.ais.services.CommandeService;


@RestController
@CrossOrigin
@RequestMapping("api-koumi/commande")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;


    @PostMapping("/add")
    public ResponseEntity<String> ajouterCommandeAvecStocks(@RequestBody CommandeAvecStocks commandeAvecStocks) {
        try {
            Commande commande = commandeAvecStocks.getCommande();
            List<Stock> stocks = commandeAvecStocks.getStocks();
            List<Double> quantitesDemandees = commandeAvecStocks.getQuantitesDemandees();
            commandeService.ajouterStocksACommande(commande, stocks, quantitesDemandees);
            return ResponseEntity.ok("Commande ajoutée avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Une erreur est survenue lors de l'ajout de la commande : " + e.getMessage());
        }
    }
    
    
          //Valider commande
            @PutMapping("/{id}/enable")
            public ResponseEntity<String> enableCommande(@PathVariable("id") String id) {
                try {
                    return commandeService.enableCommande(id);
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Une erreur est survenue lors de la validation de la commande : " + e.getMessage());
                }
            }

        //Annuler commande
        @PutMapping("/{id}/disable")
        public ResponseEntity<String> disableCommande(@PathVariable("id") String id) {
            try {
                return commandeService.disableCommande(id);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur est survenue lors de la validation de la commande : " + e.getMessage());
            }
        }

    

    @PostMapping("/addCommandeMateriel")
    @Operation(summary="Ajout de commande materiel")
    public ResponseEntity<String> saveCommande(@RequestParam String idMateriel , @RequestParam String idActeur ) {
        try {
            commandeService.commandeMateriel(idMateriel, idActeur);
            return ResponseEntity.ok("Ajouté panier avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'ajout au panier : " + e.getMessage());
        }
    }

    @PostMapping("/confirmerLivraison/{id}")
    @Operation(summary="Confirmation de livraison  materiel")
    public ResponseEntity<String> livraison(@PathVariable String idCommande) {
        try {
            commandeService.confirmerLivraison(idCommande);
            return ResponseEntity.ok("Livraison Confirmer panier avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'ajout au panier : " + e.getMessage());
        }
    }

    @PostMapping("/confirmerCommande/{idCommande}")
    @Operation(summary="Confirmer commande materiel")
    public ResponseEntity<String> confirmer(@PathVariable String idCommande) {
        try {
            commandeService.confirmerCommande(idCommande);
            return ResponseEntity.ok("Livraison Confirmer panier avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'ajout au panier : " + e.getMessage());
        }
    }

    @GetMapping("/getAllCommande/{id}")
    @Operation(summary="Liste des commandes d'un acteur")
    public ResponseEntity<List<Commande>> list(@PathVariable String idActeur) {
        return new ResponseEntity<>(commandeService.getAllCommandeByActeur(idActeur), HttpStatus.OK);
    }

    @GetMapping("/readByActeur/{id}")
    @Operation(summary="Liste des commandes d'un acteur")
    public ResponseEntity<List<Commande>> getAllByActeur(@PathVariable String id){
        return new ResponseEntity<>(commandeService.getCommandeByActeur(id), HttpStatus.OK);
    }
    
}
