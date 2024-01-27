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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import projet.ais.models.RegroupementParametre;
import projet.ais.services.RegroupementSevice;

@RestController
@CrossOrigin
@RequestMapping("/RegroupemenParametre")
public class RegroupementController {
    
    @Autowired
    RegroupementSevice regroupementSevice;

    @PostMapping("/addRegroupement")
    @Operation(summary = "Création du regroupement")
    public ResponseEntity<RegroupementParametre> saveParametre(@RequestBody RegroupementParametre regroupementParametre){
        return new ResponseEntity<>(regroupementSevice.createParametreRegroupement(regroupementParametre), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Modification du regroupement parametre")
    public ResponseEntity<RegroupementParametre> updatedParametre(@RequestBody RegroupementParametre regroupementParametre, @PathVariable Integer id){
        return new ResponseEntity<>(regroupementSevice.updateParametreRegroupement(regroupementParametre, id), HttpStatus.CREATED);
    }

    @GetMapping("/getAllParametre")
    @Operation(summary = "Get all parameters")
    public ResponseEntity<List<RegroupementParametre>> getRegroupement(){
        return new ResponseEntity<>(regroupementSevice.getAllRegroupementParametres(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression")
    public String  supprimer(@PathVariable Integer id){
        return regroupementSevice.deleteRegroupement(id);
    }
}
