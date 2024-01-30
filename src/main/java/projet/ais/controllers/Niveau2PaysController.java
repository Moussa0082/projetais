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
import projet.ais.repository.Niveau2PaysRepository;
import projet.ais.services.Niveau2PaysService;

@RestController
@RequestMapping("/niveau2Pays")
public class Niveau2PaysController {

  @Autowired
  private Niveau2PaysService niveau2PaysService;

  @Autowired
  private Niveau2PaysRepository niveau2PaysRepository;


    @PostMapping("/create")
      @Operation(summary = "créer niveau 2 pays")
     public ResponseEntity<String> createNiveau2Pays(@RequestBody Niveau2Pays niveau2Pays) {

        // Vérifier si le niveau 2 pays existe déjà
        Niveau2Pays niveau2PaysExistant = niveau2PaysRepository.findByNomN2(niveau2Pays.getNomN2());
        if (niveau2PaysExistant == null) {
            niveau2PaysService.createNiveau2Pays(niveau2Pays);
            return new ResponseEntity<>("Niveau 2 créer avec succès" , HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Le niveau 2 pays " + niveau2PaysExistant.getNomN2() + " existe déjà", HttpStatus.BAD_REQUEST);
        }
    }
    


    @PutMapping("/update/{id}")
    @Operation(summary = "Modifier niveau 2 pays")
public ResponseEntity<String> updateNiveau2Pays(@RequestBody Niveau1Pays niveau1Pays, @PathVariable String id) {
    Niveau2Pays niveau2PaysExistant = niveau2PaysRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("type d'acteur introuvable avec id :" +id));;

    if (niveau2PaysExistant != null) {
        niveau2PaysService.updateNiveau2Pays(niveau2PaysExistant, id);
        return new ResponseEntity<>("Niveau 2 Pays mis à jour avec succès", HttpStatus.OK);
    } else {
        return new ResponseEntity<>("Niveau 2 Pays non existant  " , HttpStatus.BAD_REQUEST);
    }
}

    //liste 
    @GetMapping("/listeNiveau2PaysByIdNiveau1Pays/{id}")
    @Operation(summary = "affichage de la liste des niveau 2 pays par pays")
    public ResponseEntity<List<Niveau2Pays>> listeNiveau2PaysByIdPays(@PathVariable String id){
        return  new ResponseEntity<>(niveau2PaysService.getAllNiveau2PaysByIdNiveau1Pays(id), HttpStatus.OK);
    }

           // Get Liste des  niveau 2 pays
      @GetMapping("/read")
      @Operation(summary = "Liste globale des niveau 2 pays")
    public ResponseEntity<List<Niveau2Pays>> getAllTypeNiveau2Pays() throws Exception {
        return new ResponseEntity<>(niveau2PaysService.getAllNiveau2Pays(), HttpStatus.OK);
    }

    //Supprimer Niveau 1 Pays
           @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression d'un niveau 2 pays")
    public ResponseEntity<String> deleteNiveau2Pays(@PathVariable String id){
        return new ResponseEntity<>(niveau2PaysService.deleteByIdNiveau2Pays(id), HttpStatus.OK);
    }




    
}
