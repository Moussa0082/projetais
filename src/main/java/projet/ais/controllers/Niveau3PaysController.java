package projet.ais.controllers;

import java.util.List;

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

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import projet.ais.models.Niveau1Pays;
import projet.ais.models.Niveau2Pays;
import projet.ais.models.Niveau3Pays;
import projet.ais.repository.Niveau3PaysRepository;
import projet.ais.services.Niveau3PaysService;

@RestController
@RequestMapping("/nivveau3Pays")
public class Niveau3PaysController {


    @Autowired
    private Niveau3PaysService niveau3PaysService;

    @Autowired
    private Niveau3PaysRepository niveau3PaysRepository;


      @PostMapping("/create")
      @Operation(summary = "créer niveau 3 pays")
     public ResponseEntity<String> createNiveau3Pays(@RequestBody Niveau3Pays niveau3Pays) {

        // Vérifier si le niveau 3 pays existe déjà
        Niveau3Pays niveau3PaysExistant = niveau3PaysRepository.findByNomN3(niveau3Pays.getNomN3());
        if (niveau3PaysExistant == null) {
            niveau3PaysService.createNiveau3Pays(niveau3Pays);
            return new ResponseEntity<>("Niveau 3 créer avec succès" , HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Le niveau 3 pays " + niveau3PaysExistant.getNomN3() + " existe déjà", HttpStatus.BAD_REQUEST);
        }
    }
    


    @PutMapping("/update/{id}")
    @Operation(summary = "Modifier niveau 3 pays")
public ResponseEntity<String> updateNiveau3Pays(@RequestBody Niveau3Pays niveau3Pays, @PathVariable Integer id) {
    Niveau3Pays niveau3PaysExistant = niveau3PaysRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Niveau 3 pays introuvable " ));;

    if (niveau3PaysExistant != null) {
        niveau3PaysService.updateNiveau3Pays(niveau3PaysExistant, id);
        return new ResponseEntity<>("Niveau 3 Pays mis à jour avec succès", HttpStatus.OK);
    } else {
        return new ResponseEntity<>("Niveau 3 Pays non existant  " , HttpStatus.BAD_REQUEST);
    }
}

    //liste 
    @GetMapping("/listeNiveau3PaysByIdNiveau2Pays/{id}")
    @Operation(summary = "affichage de la liste des niveau 2 pays par pays")
    public ResponseEntity<List<Niveau3Pays>> listeNiveau3PaysByIdPays(@PathVariable Integer id){
        return  new ResponseEntity<>(niveau3PaysService.getAllNiveau3PaysByIdNiveau2Pays(id), HttpStatus.OK);
    }

           // Get Liste des  niveau 2 pays
      @GetMapping("/read")
      @Operation(summary = "Liste globale des niveau 3 pays")
    public ResponseEntity<List<Niveau3Pays>> getAllTypeNiveau3Pays() throws Exception {
        return new ResponseEntity<>(niveau3PaysService.getAllNiveau3Pays(), HttpStatus.OK);
    }

    //Supprimer Niveau 1 Pays
           @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression d'un niveau 3 pays")
    public ResponseEntity<String> deleteNiveau3Pays(@PathVariable Integer id){
        return new ResponseEntity<>(niveau3PaysService.deleteByIdNiveau3Pays(id), HttpStatus.OK);
    }



    
}
