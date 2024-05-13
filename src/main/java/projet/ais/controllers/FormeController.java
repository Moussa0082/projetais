package projet.ais.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.http.HttpStatus;


import io.swagger.v3.oas.annotations.Operation;
import projet.ais.models.Forme;
import projet.ais.services.FormeService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("api-koumi/formeproduit")
public class FormeController {

    @Autowired
    FormeService formeService;

    @PostMapping("/AddForme")
    @Operation(summary="Création de forme de produit")
    public ResponseEntity<Forme> createFormes(@RequestBody Forme forme) {
        return new ResponseEntity<>(formeService.createForme(forme) , HttpStatus.CREATED);
    }
    
    @PutMapping("/updateForme/{id}")
    @Operation(summary="Modification de la forme")
    public ResponseEntity<Forme> updateForme(@PathVariable String id, @RequestBody Forme forme) {
        return new ResponseEntity<>(formeService.update(forme, id), HttpStatus.OK);
    }

    @PutMapping("/activer/{id}")
    @Operation(summary="Activation de forme")
    public ResponseEntity<Forme> activeFormes(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(formeService.active(id), HttpStatus.OK);
    }
    
    @PutMapping("/desactiver/{id}")
    @Operation(summary="Desactivation de forme ")
    public ResponseEntity<Forme> desactiveFilieres(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(formeService.desactive(id), HttpStatus.OK);
    }

    @GetMapping("/getAllForme/")
    @Operation(summary="Récuperation de tout les formes de produit")
    public ResponseEntity<List<Forme>> getFiliere() {
        return new ResponseEntity<>(formeService.getAllForme(), HttpStatus.OK);
    }
    

    @DeleteMapping("/delete/{id}")
    @Operation(summary="Supprimé un forme")
    public String deleteFormess(@PathVariable String id) {
        return formeService.deleteForme(id);
    }
}
