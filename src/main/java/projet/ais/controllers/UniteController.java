package projet.ais.controllers;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import projet.ais.models.Unite;
import projet.ais.services.UniteService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin
@RequestMapping("api-koumi/Unite")
public class UniteController {
    
    @Autowired
    UniteService uniteService;

    @PostMapping("/addUnite")
    @Operation(summary = "Création de unite")
    public ResponseEntity<Unite> saveUnite(@RequestBody Unite unite) {
        return new ResponseEntity<>(uniteService.createUnite(unite), HttpStatus.CREATED);
    }
    
    @PutMapping("/updateUnite/{id}")
    @Operation(summary = "Modification d'unité")
    public ResponseEntity<Unite> updatedUnite(@RequestBody Unite unite, @PathVariable String id) {
        return new ResponseEntity<>(uniteService.updateUnite(unite,id), HttpStatus.CREATED);
    }

    @PutMapping("/activer/{id}")
    @Operation(summary = "activation d'unité")
    public ResponseEntity<Unite> activeUnite(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(uniteService.active(id), HttpStatus.CREATED);
    }

    @PutMapping("/desactiver/{id}")
    @Operation(summary = "Modification d'unité")
    public ResponseEntity<Unite> desactiveUnite(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(uniteService.desactive(id), HttpStatus.CREATED);
    }

    @GetMapping("/getAllUnite")
    public ResponseEntity<List<Unite>> listeUnite() {
        return new ResponseEntity<>(uniteService.getAllUnites(), HttpStatus.CREATED);
    }

    @GetMapping("/getAllUniteByActeurs/{id}")
    public ResponseEntity<List<Unite>> listeUniteByActeur(@PathVariable String id) {
        return new ResponseEntity<>(uniteService.getAllUnitesByActeur(id), HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression des unites")
    public String deleteUnites(@PathVariable String id){
        return uniteService.deleteUnite(id);
    }
}
