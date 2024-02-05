package projet.ais.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import projet.ais.models.CommandeMateriel;
import projet.ais.services.CommandeMaterielService;

@RestController
@RequestMapping("/CommandeMateriels")
public class CommandeMaterielController {
    
    @Autowired
    CommandeMaterielService commandeMaterielService;

    @PostMapping("/addMateriel")
    public ResponseEntity<String> saveCommande(@RequestParam String idActeur, @RequestParam String idMateriel) {
        try {
            commandeMaterielService.ajouterAuPanier(idActeur, idMateriel);
            return ResponseEntity.ok("Article ajouté au panier avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'ajout au panier : " + e.getMessage());
        }
    }

    @GetMapping("/getAllCommande")
    public ResponseEntity<List<CommandeMateriel>> list(@RequestParam String idActeur) {
        return new ResponseEntity<>(commandeMaterielService.getAllCommandeByActeur(idActeur), HttpStatus.OK);
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
