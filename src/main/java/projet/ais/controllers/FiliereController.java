package projet.ais.controllers;

import java.util.List;

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

import io.swagger.v3.oas.annotations.Operation;
import projet.ais.models.Filiere;
import projet.ais.services.FiliereService;


@RestController
@CrossOrigin
@RequestMapping("api-koumi/Filiere")
public class FiliereController {

    @Autowired
    FiliereService filiereService;

    @PostMapping("/AddFiliere")
    @Operation(summary="Création de filiere")
    public ResponseEntity<Filiere> createFilieres(@RequestBody Filiere filiere) {
        return new ResponseEntity<>(filiereService.createFiliere(filiere) , HttpStatus.CREATED);
    }
    
    @PutMapping("/updateFilieres/{id}")
    @Operation(summary="Modification de filiere fonction de l'id de filiere")
    public ResponseEntity<Filiere> updateFilieres(@PathVariable String id, @RequestBody Filiere filiere) {
        return new ResponseEntity<>(filiereService.updateFiliere(filiere, id), HttpStatus.OK);
    }

    @PutMapping("/activer/{id}")
    @Operation(summary="Activation de filiere fonction de l'id de filiere")
    public ResponseEntity<Filiere> activeFilieres(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(filiereService.active(id), HttpStatus.OK);
    }
    
    @PutMapping("/desactiver/{id}")
    @Operation(summary="Desactivation de filiere fonction de l'id de filiere")
    public ResponseEntity<Filiere> desactiveFilieres(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(filiereService.desactive(id), HttpStatus.OK);
    }

    @GetMapping("/getAllFiliere")
    @Operation(summary="Récuperation de tout les filieres")
    public ResponseEntity<List<Filiere>> getFiliere() {
        return new ResponseEntity<>(filiereService.getAllf(), HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/{id}")
    @Operation(summary="Supprimé un filiere en fonction de l'id de filiere")
    public String deleteFilieres(@PathVariable String id) {
        return filiereService.DeleteFiliere(id);
    }
}
