package projet.ais.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import projet.ais.models.CategorieProduit;
import projet.ais.models.Commande;
import projet.ais.models.CommandeAvecStocks;
import projet.ais.models.DetailCommande;
import projet.ais.models.Intrant;
import projet.ais.models.Stock;
import projet.ais.services.CommandeService;


@RestController
// @CrossOrigin(origins = "*")
@RequestMapping("api-koumi/commande")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;
    
    
    // @PostMapping("/ajouterStocksACommande")
    @PostMapping("/add")
    public ResponseEntity<?> ajouterStocksACommande(@RequestBody CommandeAvecStocks commandeAvecStocks ) {
        try {
            // Envelopper les listes dans des objets Optional
    Optional<List<Stock>> optionalStocks = Optional.ofNullable(commandeAvecStocks.getStocks());
    Optional<List<Intrant>> optionalIntrants = Optional.ofNullable(commandeAvecStocks.getIntrants());
    Optional<List<Double>> optionalQuantitesDemandees = Optional.ofNullable(commandeAvecStocks.getQuantitesDemandees());
    Optional<List<Double>> optionalQuantitesIntrants = Optional.ofNullable(commandeAvecStocks.getQuantitesIntrants());

    // Appeler la méthode ajouterStocksACommande en passant les objets Optional
    commandeService.ajouterStocksACommande(
        commandeAvecStocks.getActeur(),
        optionalStocks,
        optionalIntrants,
        optionalQuantitesDemandees,
        optionalQuantitesIntrants
    );
          
return ResponseEntity.status(HttpStatus.OK).body("Commande passer avec succes");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la création des commandes : " + e.getMessage());
        }
    }
    
    
    @GetMapping("/getAllCommandesWithPagination")
    public ResponseEntity<Page<Commande>> getCommandes(@RequestParam() int page,
                                                  @RequestParam() int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Commande> commandes = commandeService.getAllCommandePageable(pageable);
        return ResponseEntity.ok().body(commandes);
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
                    .body("Une erreur est survenue lors de l'annulation de la commande : " + e.getMessage());
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

     @PutMapping("/confirmerLivraisonVendeur")
    public ResponseEntity<String> confirmerLivraisonVendeur(@RequestParam String id, @RequestParam Map<String, Double> quantitesLivre) throws Exception {
        try {
            commandeService.confirmerLivraisonVendeur(id,quantitesLivre);
            return ResponseEntity.ok("Livraison Confirmer avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la confirmation de la livraison des produits : " + e.getMessage());
        }
    }

    @PutMapping("/confirmerLivraison/{idDetailCommande}/{quantiteLivree}")
    public ResponseEntity<?> confirmerLivrasonProduit(@PathVariable String idDetailCommande, @PathVariable double quantiteLivree) {
    try {
        commandeService.confirmerCommande(idDetailCommande, quantiteLivree);
        return ResponseEntity.ok("Commande confirmée avec succès pour le produit : " + idDetailCommande);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la confirmation de la commande pour le produit : " + e.getMessage());
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

    @GetMapping("/getAllCommandeByActeur/{idActeur}")
    @Operation(summary="Liste des commandes d'un acteur celui qui a commandé")
    public ResponseEntity<List<Commande>> listByActeur(@PathVariable String idActeur) {
        return new ResponseEntity<>(commandeService.getAllCommandeByActeur(idActeur), HttpStatus.OK);
    }

    @GetMapping("/getAllCommandeByActeurProprietaire/{acteurProprietaire}")
    @Operation(summary="Liste des commandes d'un acteur celui qui a commandé")
    public ResponseEntity<List<Commande>> getAllCommandeByActeurProprietaire(@PathVariable String acteurProprietaire) {
        return new ResponseEntity<>(commandeService.getAllCommandeByActeurProprietaire(acteurProprietaire), HttpStatus.OK);
    }

    @GetMapping("/getAllCommande")
    @Operation(summary="Liste des commandes d'un acteur celui qui à qui appartient les stock comandés")
    public ResponseEntity<List<Commande>> getAllCommandes() {
        return new ResponseEntity<>(commandeService.getAllCommandes(), HttpStatus.OK);
    }

    @GetMapping("/readByActeur/{id}")
    @Operation(summary="Liste des commandes d'un acteur")
    public ResponseEntity<List<Commande>> getAllByActeur(@PathVariable String id){
        return new ResponseEntity<>(commandeService.getCommandeByActeur(id), HttpStatus.OK);
    }
    
}
