package projet.ais.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import projet.ais.models.Speculation;
import projet.ais.services.SpeculationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@CrossOrigin
@RequestMapping("/Speculation")
public class SpeculationController {
    
    @Autowired
    SpeculationService speculationService;

    @PostMapping("/addSpeculation")
    @Operation(summary="Création de spéculation")
    public ResponseEntity<Speculation> createSpeculations(@RequestBody Speculation speculation) {
        return new ResponseEntity<>(speculationService.createSpeculation(speculation), HttpStatus.CREATED);
    }
    
    @PutMapping("/update/{id}")
    @Operation(summary="Modification de la spéculation en fonction de l'id")
    public ResponseEntity<Speculation> updateSpeculations(@PathVariable Integer id, @RequestBody Speculation speculation) {
        return new ResponseEntity<>(speculationService.updateSpeculation(speculation, id), HttpStatus.OK);
    }
    
    @PutMapping("/activer/{id}")
    @Operation(summary="Activation de la spéculation en fonction de l'id")
    public ResponseEntity<Speculation> activeSpeculations(@PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(speculationService.active(id), HttpStatus.OK);
    }
    
    @PutMapping("/desactiver/{id}")
    @Operation(summary="Desactivation de la spéculation en fonction de l'id")
    public ResponseEntity<Speculation> desactiveSpeculations(@PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(speculationService.desactive(id), HttpStatus.OK);
    }

    @GetMapping("/getAllSpeculation")
    @Operation(summary="Récuperation des spéculations")
    public ResponseEntity<List<Speculation>> getAllSpeculations() {
        return new ResponseEntity<>(speculationService.getAllSpeculation(), HttpStatus.OK);
    }
    
    @GetMapping("/getAllSpeculationByCategorie/{id}")
    @Operation(summary="Récuperation des spéculations en fonction de l'id de categorie")
    public ResponseEntity<List<Speculation>> getAllSpeculationByCate(@PathVariable Integer id) {
        return new ResponseEntity<>(speculationService.getAllSpeculationByCategorie(id), HttpStatus.OK);
    }
    
    @DeleteMapping("/deleteSpeculation/{id}")
    @Operation(summary="Suppression d'une spéculations en fonction de l'id")
    public String supprimerSpeculation(@PathVariable Integer id) {
        return speculationService.DeleteSpeculations(id);
    }
}
