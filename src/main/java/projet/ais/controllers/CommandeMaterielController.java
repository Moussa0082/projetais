package projet.ais.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import projet.ais.models.CommandeMateriel;
import projet.ais.models.Materiel;
import projet.ais.services.CommandeMaterielService;

@RestController
@RequestMapping("/CommandeMateriels")
public class CommandeMaterielController {
    
    @Autowired
    CommandeMaterielService commandeMaterielService;

    

    @PostMapping("/confirmer")
    public ResponseEntity<String> ConfirmerCommande(@RequestParam String idActeur) {
        try {
            commandeMaterielService.confirmerPanier(idActeur);
            return ResponseEntity.ok("Article ajouté au panier avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'ajout au panier : " + e.getMessage());
        }
    }

    @GetMapping("/getAllCommande/{id}")
    public ResponseEntity<List<CommandeMateriel>> list(@PathVariable String idActeur) {
        return new ResponseEntity<>(commandeMaterielService.getAllCommandeByActeur(idActeur), HttpStatus.OK);
    }

    @GetMapping("/readByActeur/{id}")
    public ResponseEntity<List<CommandeMateriel>> getAllByActeur(@PathVariable String id){
        return new ResponseEntity<>(commandeMaterielService.getCommandeByActeur(id), HttpStatus.OK);
    }

    @PostMapping("/addCommande/{idMateriel}/{idActeur}")
    public ResponseEntity<String> saveCommande(@PathVariable String idMateriel , @PathVariable String idActeur ) {
        try {
            commandeMaterielService.commande(idMateriel, idActeur);
            return ResponseEntity.ok("Ajouté panier avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'ajout au panier : " + e.getMessage());
        }
    }

    @PostMapping("/confirmerLivraison/{id}")
    public ResponseEntity<String> livraison(@PathVariable String idCommande) {
        try {
            commandeMaterielService.confirmerLivraison(idCommande);
            return ResponseEntity.ok("Livraison Confirmer panier avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'ajout au panier : " + e.getMessage());
        }
    }

    @PostMapping("/confirmerCommande/{idCommande}")
    public ResponseEntity<String> confirmer(@PathVariable String idCommande) {
        try {
            commandeMaterielService.confirmerCommande(idCommande);
            return ResponseEntity.ok("Livraison Confirmer panier avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'ajout au panier : " + e.getMessage());
        }
    }

    @PostMapping("/vider")
    public ResponseEntity<String> viderPanier(@RequestParam String idActeur) {
        try {
            commandeMaterielService.viderPanier(idActeur);
            return ResponseEntity.ok("Panier vidé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la suppression du panier : " + e.getMessage());
        }
    }

    @PostMapping("/supprimer")
    public ResponseEntity<String> supprimerDuPanier(@RequestParam String idActeur, @RequestParam String idMateriel) {
        try {
            commandeMaterielService.supprimerMateriel(idActeur, idMateriel);
            return ResponseEntity.ok("Article supprimé du panier avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la suppression de l'article du panier : " + e.getMessage());
        }
    }
}
