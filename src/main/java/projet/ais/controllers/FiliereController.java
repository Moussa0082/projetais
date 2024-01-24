package projet.ais.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projet.ais.models.Filiere;
import projet.ais.services.FiliereService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.*;


@RestController
@CrossOrigin
@RequestMapping("/Filiere")
public class FiliereController {

    @Autowired
    FiliereService filiereService;

    @PostMapping("/AddFiliere")
    public ResponseEntity<Filiere> createFilieres(@RequestBody Filiere filiere) {
        return new ResponseEntity<>(filiereService.createFiliere(filiere) , HttpStatus.CREATED);
    }
    
    @PutMapping("/updateFilieres/{id}")
    public ResponseEntity<Filiere> updateFilieres(@PathVariable Integer id, @RequestBody Filiere filiere) {
        return new ResponseEntity<>(filiereService.updateFiliere(filiere, id), HttpStatus.OK);
    }
    
    @GetMapping("/getAllFiliere")
    public ResponseEntity<List<Filiere>> getFiliere() {
        return new ResponseEntity<>(filiereService.getAllf(), HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/{id}")
    public String deleteFilieres(Integer id) {
        return filiereService.DeleteFiliere(id);
    }
}
