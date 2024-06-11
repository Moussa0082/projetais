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
import projet.ais.models.Monnaie;
import projet.ais.models.Unite;
import projet.ais.services.MonnaieService;
import projet.ais.services.UniteService;

@RestController
@RequestMapping("api-koumi/Monnaie")
public class MonnaieController {
    
    @Autowired
    MonnaieService monnaieService;

    @PostMapping("/create")
    @Operation(summary = "Création de la monnaie ")
    public ResponseEntity<Monnaie> saveMonnaie(@RequestBody Monnaie monnaie) {
        return new ResponseEntity<>(monnaieService.createMonnaie(monnaie), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Modification de la monnaie ")
    public ResponseEntity<Monnaie> updateMonnaie(@RequestBody Monnaie monnaie, @PathVariable String id) {
        return new ResponseEntity<>(monnaieService.updateMonnaie(monnaie,id), HttpStatus.CREATED);
    }

    @PutMapping("/desactiver/{id}")
    @Operation(summary = "Desactivation")
    public ResponseEntity<Monnaie> desactiveMonnaie(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(monnaieService.desactive(id), HttpStatus.CREATED);
    }

    @PutMapping("/activer/{id}")
    @Operation(summary = "Activation")
    public ResponseEntity<Monnaie> activeMonnaie(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(monnaieService.active(id), HttpStatus.CREATED);
    }

    @GetMapping("/getAllMonnaie")
    public ResponseEntity<List<Monnaie>> listeMonnaie() {
        return new ResponseEntity<>(monnaieService.getAllMonnaies(), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression")
    public String deleteMonnaie(@PathVariable String id){
        return monnaieService.deleteMonnaie(id);
    }
}
