package projet.ais.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import projet.ais.models.ParametreFiche;
import projet.ais.services.ParametreFicheService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@CrossOrigin
@RequestMapping("api-koumi/parametreFiche")
public class ParametreFicheController {
    
    @Autowired
    ParametreFicheService parametreFicheService;

    @PostMapping("/addParametreFiche")
    @Operation(summary = "Création du parametre")
    public ResponseEntity<ParametreFiche> saveParam(@RequestBody ParametreFiche parametreFiche){
        return new ResponseEntity<>(parametreFicheService.createParametreFiche(parametreFiche), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ParametreFiche> updateParametre(@PathVariable String id, @RequestBody ParametreFiche parametreFiche) {        
        return new ResponseEntity<>(parametreFicheService.updateParametreFiche(parametreFiche, id), HttpStatus.OK);
    }
    
    @PutMapping("/activer/{id}")
    public ResponseEntity<ParametreFiche> activeParametre(@PathVariable String id) throws Exception {        
        return new ResponseEntity<>(parametreFicheService.active(id), HttpStatus.OK);
    }
  
    @PutMapping("/desactiver/{id}")
    public ResponseEntity<ParametreFiche> desactiveParametre(@PathVariable String id) throws Exception {        
        return new ResponseEntity<>(parametreFicheService.desactive(id), HttpStatus.OK);
    }

    @GetMapping("/getParametreFiche")
    public ResponseEntity<List<ParametreFiche>> getAllParam() {
        return new ResponseEntity<>(parametreFicheService.getParametreFicheDonnees(), HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression du parametre")
    public String supprimerParam(@PathVariable String id){
        parametreFicheService.deleteParametreFiche(id);
        return "Supprimé avec succèss";
    }
}
