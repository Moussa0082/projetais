package projet.ais.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import jakarta.persistence.EntityNotFoundException;
import projet.ais.models.CategorieProduit;
import projet.ais.models.Continent;
import projet.ais.repository.ContinentRepository;
import projet.ais.services.ContinentService;

@RestController
@RequestMapping("/continent")
public class ContinentController {
    
    @Autowired
    private ContinentService continentService;

    @Autowired
    private ContinentRepository continentRepository;


       //Ajouter un continent
           @PostMapping("/create")
     @Operation(summary = "Ajouté un continent")
     public ResponseEntity<String> createContinent(@RequestBody Continent continent) {

        // Vérifier si le continent existe déjà
        Continent continentExistant = continentRepository.findByNomContinent(continent.getNomContinent());
        if (continentExistant == null) {
            continentService.createContinent(continent);
            return new ResponseEntity<>("Continent ajouté avec succès" , HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Le continent " + continentExistant.getNomContinent() + " existe déjà", HttpStatus.BAD_REQUEST);
        }
    }
    

    //Modifier un continent
    @PutMapping("/update/{id}")
    @Operation(summary = "Modifier un continent")
   public ResponseEntity<String> updateContinent(@RequestBody Continent continent, @PathVariable String id) {
    Continent continentExistant = continentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Continent introuvable "));;

    if (continentExistant != null) {
        continentService.updateContinent(continentExistant, id);
        return new ResponseEntity<>("Continent mis à jour avec succès", HttpStatus.OK);
    } else {
        return new ResponseEntity<>("Continent non existant ", HttpStatus.BAD_REQUEST);
    }
}


      //Activer continent
       @PutMapping("/activer/{id}")
    @Operation(summary="Activation d'un continent à travers son id")
    public ResponseEntity<Continent> activeContinent(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(continentService.active(id), HttpStatus.OK);
    }

    //Desativer continent
    @PutMapping("/desactiver/{id}")
    @Operation(summary="Desactivation d'un continent produit à travers son id")
    public ResponseEntity<Continent> desactiveContinent(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(continentService.desactive(id), HttpStatus.OK);
    }



           // Get Liste des  continents
      @GetMapping("/read")
      @Operation(summary = "affichage de la liste des continents ")
    public ResponseEntity<List<Continent>> getAllContinent() throws Exception {
        return new ResponseEntity<>(continentService.getAllContinent(), HttpStatus.OK);
    }

    //Suppression d'un continent
           @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression d'un continent")
    public ResponseEntity<String> deleteContinent(@PathVariable String id){
        return new ResponseEntity<>(continentService.deleteByIdContinent(id), HttpStatus.OK);
    }




}
