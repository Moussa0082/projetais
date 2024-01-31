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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import projet.ais.models.Materiel;
import projet.ais.services.MaterielService;

@RestController
@CrossOrigin
@RequestMapping("/Materiel")
public class MaterielController {
    
    @Autowired
    MaterielService materielService;

    @PostMapping("/addMateriel")
    @Operation(summary = "Ajout du materiel")
    public ResponseEntity<Materiel> saveMateriel(
        @Valid @RequestParam("materiel") String addMateriel,
        @Valid @RequestParam(value = "image", required = false)  MultipartFile imageFile) throws Exception
    {
        Materiel materiel = new Materiel();

       try {
        materiel =  new JsonMapper().readValue(addMateriel, Materiel.class);
       } catch (JsonProcessingException e) {
           throw new Exception(e.getMessage());
       }
       Materiel savedMateriel = materielService.createMateriel(materiel, imageFile);

       return new ResponseEntity<>(savedMateriel, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Modification du materiel")
    public ResponseEntity<Materiel> updatedMateriel(
        @Valid @RequestParam("materiel") String addMateriel,
        @Valid @RequestParam(value = "image", required = false)  MultipartFile imageFile, @PathVariable String id) throws Exception
    {
        Materiel materiel = new Materiel();

       try {
        materiel =  new JsonMapper().readValue(addMateriel, Materiel.class);
       } catch (JsonProcessingException e) {
           throw new Exception(e.getMessage());
       }
       Materiel savedMateriel = materielService.updateMateriel(materiel, id, imageFile);

       return new ResponseEntity<>(savedMateriel, HttpStatus.OK);
    }

    @PutMapping("/activer/{id}")
    public ResponseEntity<Materiel> activeMateriel(@PathVariable String id) throws Exception{
        return new ResponseEntity<>(materielService.active(id), HttpStatus.OK);
    }

    @PutMapping("/desactiver/{id}")
    public ResponseEntity<Materiel> desactiveMateriel(@PathVariable String id) throws Exception{
        return new ResponseEntity<>(materielService.desactive(id), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Materiel>> getAllMateriel(){
        return new ResponseEntity<>(materielService.getMateriels(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public String supprimer(@PathVariable String id){
        return materielService.deleteMateriel(id);
    }
}
