package projet.ais.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import projet.ais.models.Niveau1Pays;
import projet.ais.models.Pays;
import projet.ais.models.SousRegion;
import projet.ais.repository.SousRegionRepository;
import projet.ais.services.SousRegionService;

@RestController
@RequestMapping("/sousRegion")
public class SousRegionController {


    @Autowired
    private SousRegionService sousRegionService;

    @Autowired
    private SousRegionRepository sousRegionRepository;


         @PostMapping("/create")
     @Operation(summary = "Ajouté une sous region")
     public ResponseEntity<String> createSousRegion(@RequestBody SousRegion sousRegion) {

        // Vérifier si la sous region existe déjà
        SousRegion sousRegionExistant = sousRegionRepository.findByNomSousRegion(sousRegion.getNomSousRegion());
        if (sousRegionExistant == null) {
            sousRegionService.createSousRegion(sousRegion);
            return new ResponseEntity<>("Sous region ajouté avec succès" , HttpStatus.OK);
        } else {
            return new ResponseEntity<>("La sous region " + sousRegionExistant.getNomSousRegion() + " existe déjà", HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/update/{id}")
    @Operation(summary = "Modifier une sous region")
   public ResponseEntity<String> updateSousRegion(@RequestBody SousRegion sousRegion, @PathVariable String id) {
    SousRegion sousRegionExistant = sousRegionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Sous region introuvable "));;

    if (sousRegionExistant != null) {
        sousRegionService.updateSousRegion(sousRegionExistant, id);
        return new ResponseEntity<>("Sous region mis à jour avec succès", HttpStatus.OK);
    } else {
        return new ResponseEntity<>("Sous region non existant ", HttpStatus.BAD_REQUEST);
    }
}

     //liste sous region par continent
    @GetMapping("/listeSousRegionByContinent/{id}")
    @Operation(summary = "affichage de la liste des  sous region par continent")
    public ResponseEntity<List<SousRegion>> listeSousRegionBycontinent(@PathVariable String id){
        return  new ResponseEntity<>(sousRegionService.getAllSousRegionByContinent(id), HttpStatus.OK);
    }


           // Get Liste des  pays
      @GetMapping("/read")
      @Operation(summary = "affichage de la liste des sous region ")
    public ResponseEntity<List<SousRegion>> getAllSousRegion() throws Exception {
        return new ResponseEntity<>(sousRegionService.getAllSousRegion(), HttpStatus.OK);
    }

    //Suppression d'un pays methode
           @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression d'une sous region")
    public ResponseEntity<String> deleteSousRegion(@PathVariable String id){
        return new ResponseEntity<>(sousRegionService.deleteByIdSousRegion(id), HttpStatus.OK);
    }


               //Activer sous region
       @PutMapping("/activer/{id}")
    @Operation(summary="Activation d'une sous region à travers son id")
    public ResponseEntity<SousRegion> activeSousRegion(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(sousRegionService.active(id), HttpStatus.OK);
    }

    //Desativer niveau 1 pays
    @PutMapping("/desactiver/{id}")
    @Operation(summary="Desactivation d'une sous region à travers son id")
    public ResponseEntity<SousRegion> desactiveSousRegion(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(sousRegionService.desactive(id), HttpStatus.OK);
    }






}
