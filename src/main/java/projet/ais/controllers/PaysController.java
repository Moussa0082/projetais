package projet.ais.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

import io.swagger.v3.oas.annotations.Operation;
import projet.ais.models.Pays;
import projet.ais.repository.PaysRepository;
import projet.ais.services.PaysService;

@RestController
@CrossOrigin
@RequestMapping("api-koumi/pays")
public class PaysController {

    @Autowired
    private PaysService  paysService;

    @Autowired
    private PaysRepository paysRepository;

     @PostMapping("/create")
     @Operation(summary = "Ajouté un pays")
     public ResponseEntity<String> createPays(@RequestBody Pays pays) {

        // Vérifier si le pays existe déjà
        Pays paysExistant = paysRepository.findByNomPays(pays.getNomPays());
        if (paysExistant == null) {
            paysService.createPays(pays);
            return new ResponseEntity<>("Pays ajouté avec succès" , HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Le pays " + paysExistant.getNomPays() + " existe déjà", HttpStatus.BAD_REQUEST);
        }
    }

    //liste pays par sous region
    @GetMapping("/listePaysBySousRegion/{id}")
    @Operation(summary = "affichage de la liste des pays par sous region")
    public ResponseEntity<List<Pays>> listePaysBysousregion(@PathVariable String id){
        return  new ResponseEntity<>(paysService.getAllPaysBySousRegion(id), HttpStatus.OK);
    }
    
    @PutMapping("/update/{id}")
    @Operation(summary = "Modifier un pays")
   public ResponseEntity<Pays> updatesPays(@RequestBody Pays pays, @PathVariable String id) {
    return new ResponseEntity<>(paysService.updatePays(pays, id), HttpStatus.OK);
}


           // Get Liste des  pays
      @GetMapping("/read")
      @Operation(summary = "affichage de la liste des pays ")
    public ResponseEntity<List<Pays>> getAllPays() throws Exception {
        return new ResponseEntity<>(paysService.getAllPays(), HttpStatus.OK);
    }

    //Suppression d'un pays methode
           @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression d'un pays")
    public ResponseEntity<String> deletePays(@PathVariable String id){
        return new ResponseEntity<>(paysService.deleteByIdPays(id), HttpStatus.OK);
    }

              //Activer  pays
       @PutMapping("/activer/{id}")
    @Operation(summary="Activation d'un pays à travers son id")
    public ResponseEntity<Pays> activePays(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(paysService.active(id), HttpStatus.OK);
    }

    //Desativer  pays
    @PutMapping("/desactiver/{id}")
    @Operation(summary="Desactivation d'un pays à travers son id")
    public ResponseEntity<Pays> desactivePays(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(paysService.desactive(id), HttpStatus.OK);
    }

    
}
