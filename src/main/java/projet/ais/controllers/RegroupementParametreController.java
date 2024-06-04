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
import projet.ais.models.RegroupementParametre;
import projet.ais.services.RegroupementParametreService;

@RestController
@RequestMapping("api-koumi/Regroupement")
public class RegroupementParametreController {
    
    @Autowired
    private RegroupementParametreService regroupementSevice;

    @PostMapping("/create")
    @Operation(summary = "Création du regroupement")
    public ResponseEntity<RegroupementParametre> saveParametre(@RequestBody RegroupementParametre regroupementParametre){
        return new ResponseEntity<>(regroupementSevice.createRegroupementParametres(regroupementParametre), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Modification du regroupement parametre")
    public ResponseEntity<RegroupementParametre> updatedParametre(@RequestBody RegroupementParametre regroupementParametre, @PathVariable String id){
        return new ResponseEntity<>(regroupementSevice.updateRegroupementParametre(regroupementParametre, id), HttpStatus.OK);
    }

    @PutMapping("/activer/{id}")
    @Operation(summary = "Activation du regroupement parametre")
    public ResponseEntity<RegroupementParametre> activeParametre(@PathVariable String id) throws Exception{
        return new ResponseEntity<>(regroupementSevice.active(id), HttpStatus.OK);
    }
   
    @PutMapping("/desactiver/{id}")
    @Operation(summary = "Désactivation du regroupement parametre")
    public ResponseEntity<RegroupementParametre> desactivationParametre(@PathVariable String id) throws Exception{
        return new ResponseEntity<>(regroupementSevice.desactive(id), HttpStatus.OK);
    }

    @GetMapping("/getAllParametre")
    @Operation(summary = "Get all parameters")
    public ResponseEntity<List<RegroupementParametre>> getRegroupement(){
        return new ResponseEntity<>(regroupementSevice.getAllRegroupementParametres(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression")
    public ResponseEntity<String> supprimer(@PathVariable String id){
        return new ResponseEntity<>(regroupementSevice.deleteRegroupement(id), HttpStatus.OK);
    }
}
