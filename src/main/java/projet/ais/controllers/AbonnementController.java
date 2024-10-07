package projet.ais.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import projet.ais.config.ResponseHandler;
import projet.ais.models.Abonnement;
import projet.ais.services.AbonnementService;

@RestController
@RequestMapping("api-koumi/abonnement")
public class AbonnementController {
 
    @Autowired
    AbonnementService aService;

    @PostMapping("/AddAbonnement")
    @Operation(summary="Création de Abonnement")
    public ResponseEntity<Abonnement> createAbonnements(@RequestBody Abonnement abonnement) {
        return new ResponseEntity<>(aService.creerAbonnement(abonnement) , HttpStatus.CREATED);
    }
    
    @PutMapping("/activer/{id}")
    @Operation(summary="Activation de Abonnement")
    public ResponseEntity<Abonnement> activeAbonnements(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(aService.active(id), HttpStatus.OK);
    }
    
    @PutMapping("/desactiver/{id}")
    @Operation(summary="Desactivation de Abonnement ")
    public ResponseEntity<Abonnement> desactiveFilieres(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(aService.desactive(id), HttpStatus.OK);
    }

    @GetMapping("/getAllAbonnement/")
    @Operation(summary="Récuperation de tout les Abonnements de produit")
    public ResponseEntity<List<Abonnement>> getAbonnement() {
        return new ResponseEntity<>(aService.getAll(), HttpStatus.OK);
    }
    
    // Récupérer le dernier abonnement d'un acteur par son id
    @GetMapping("/dernier/{idActeur}")
    public ResponseEntity<Abonnement> getDernierAbonnement(@PathVariable String idActeur) {
        Abonnement dernierAbonnement = aService.getLastAbonnementByActeur(idActeur);
        
        if (dernierAbonnement != null) {
            return ResponseEntity.ok(dernierAbonnement);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getAllByActeur/{id}")
    @Operation(summary="Récuperation de tout les Abonnements d'un acteur")
    public ResponseEntity<List<Abonnement>> getAbonnementByActeur(@PathVariable String id) {
        return new ResponseEntity<>(aService.getAllByActeur(id), HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/{id}")
    @Operation(summary="Supprimé un Abonnement")
    public String deleteAbonnementss(@PathVariable String id) {
        return aService.deleteAbonnement(id);
    }
}
