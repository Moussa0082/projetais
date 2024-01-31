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
import projet.ais.models.Acteur;
import projet.ais.models.Continent;
import projet.ais.models.Niveau1Pays;
import projet.ais.models.TypeActeur;
import projet.ais.repository.Niveau1PaysRepository;
import projet.ais.services.Niveau1PaysService;

@RestController
@RequestMapping("/niveau1Pays")
public class Niveau1PaysController {

    @Autowired
    private Niveau1PaysService niveau1PaysService;

    @Autowired
    private Niveau1PaysRepository niveau1PaysRepository;

    
      @PostMapping("/create")
      @Operation(summary = "créer niveau 1 pays")
     public ResponseEntity<String> createNiveau1Pays(@RequestBody Niveau1Pays niveau1Pays) {

        // Vérifier si le niveau 1 pays existe déjà
        Niveau1Pays niveau1PaysExistant = niveau1PaysRepository.findByNomN1(niveau1Pays.getNomN1());
        if (niveau1PaysExistant == null) {
            niveau1PaysService.createNiveau1Pays(niveau1Pays);
            return new ResponseEntity<>("Type d'acteur créer avec succès" , HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Le niveau 1 pays " + niveau1PaysExistant.getNomN1() + " existe déjà", HttpStatus.BAD_REQUEST);
        }
    }
    


    @PutMapping("/update/{id}")
    @Operation(summary = "Modifier niveau 1 pays")
public ResponseEntity<String> updateNiveau1Pays(@RequestBody Niveau1Pays niveau1Pays, @PathVariable String id) {
    Niveau1Pays niveau1PaysExistant = niveau1PaysRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("type d'acteur introuvable avec id :" +id));;

    if (niveau1PaysExistant != null) {
        niveau1PaysService.updateNiveau1Pays(niveau1PaysExistant, id);
        return new ResponseEntity<>("Niveau 1 Pays mis à jour avec succès", HttpStatus.OK);
    } else {
        return new ResponseEntity<>("Niveau 1 Pays non existant  " , HttpStatus.BAD_REQUEST);
    }
}

    //liste 
    @GetMapping("/listeNiveau1PaysByIdPays/{id}")
    @Operation(summary = "affichage de la liste des niveau 1 pays par pays")
    public ResponseEntity<List<Niveau1Pays>> listeNiveau1PaysByIdPays(@PathVariable String id){
        return  new ResponseEntity<>(niveau1PaysService.getAllNiveau1PaysByPays(id), HttpStatus.OK);
    }

           // Get Liste des  niveau 1 pays
      @GetMapping("/read")
      @Operation(summary = "Liste globale des niveau 1 pays")
    public ResponseEntity<List<Niveau1Pays>> getAllTypeNiveau1Pays() throws Exception {
        return new ResponseEntity<>(niveau1PaysService.getAllNiveau1Pays(), HttpStatus.OK);
    }

           //Activer niveau 1 pays
       @PutMapping("/activer/{id}")
    @Operation(summary="Activation de niveau 1 pays à travers son id")
    public ResponseEntity<Niveau1Pays> activeNiveau1Pays(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(niveau1PaysService.active(id), HttpStatus.OK);
    }

    //Desativer niveau 1 pays
    @PutMapping("/desactiver/{id}")
    @Operation(summary="Desactivation de niveau 1 pays à travers son id")
    public ResponseEntity<Niveau1Pays> desactiveNiveau1Pays(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(niveau1PaysService.desactive(id), HttpStatus.OK);
    }

    //Supprimer Niveau 1 Pays
           @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression d'un niveau 1 pays")
    public ResponseEntity<String> deleteNiveau1Pays(@PathVariable String id){
        return new ResponseEntity<>(niveau1PaysService.deleteByIdNiveau1Pays(id), HttpStatus.OK);
    }

    
}
