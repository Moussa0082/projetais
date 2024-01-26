package projet.ais.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import java.util.*;
import org.springframework.http.ResponseEntity;
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
import projet.ais.models.ParametreGeneraux;
import projet.ais.repository.ParametreGenerauxRepository;
import projet.ais.services.ParametreGenerauxService;

@RestController
@RequestMapping("/parametreGeneraux")
public class ParametreGenerauxController {



    @Autowired
    private ParametreGenerauxService parametreGenerauxService;


     //Create user
          @PostMapping("/create")
    @Operation(summary = "Création d'un paramètre général")
    public ResponseEntity<String> createParametregeneraux(
            @Valid @RequestParam("parametreGeneral") String parametreGeneralString,
            @RequestParam(value = "image", required = false) MultipartFile imageFile)
            throws Exception {
        ParametreGeneraux parametreGeneraux = new ParametreGeneraux();
        try {
            parametreGeneraux = new JsonMapper().readValue(parametreGeneralString, ParametreGeneraux.class);
        } catch (JsonProcessingException e) {
            throw new Exception(e.getMessage());
        }

        ResponseEntity<String> response = parametreGenerauxService.createParametreGeneral(parametreGeneraux, imageFile);
        // Check for successful creation (HTTP status code 201)
    if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
        return new ResponseEntity<>("Paramètre général ajouté avec succès", HttpStatus.CREATED);
    } else 
    {
          return new ResponseEntity<>("Paramètre général non ajouté ", HttpStatus.BAD_REQUEST);
         // Return the original response with error information
    }
  
    }

     
    //Mettre à jour un user
      @PutMapping("/update/{id}")
    @Operation(summary = "Mise à jour d'un paramètre général par son Id ")
    public ResponseEntity<ParametreGeneraux> updateParametreGeneraux(
            @PathVariable Integer id,
            @Valid @RequestParam("parametreGeneral") String parametreGeneralString,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        ParametreGeneraux parametreGeneraux = new ParametreGeneraux();
        try {
            parametreGeneraux = new JsonMapper().readValue(parametreGeneralString, ParametreGeneraux.class);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            ParametreGeneraux parametreGeneralMisAjour = parametreGenerauxService.updateParametreGeneraux(parametreGeneraux, id, imageFile);
            return new ResponseEntity<>(parametreGeneralMisAjour, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
     


         @GetMapping("/read")
     @Operation(summary = "Affichage de la  liste des paramètres généraux")
    public ResponseEntity<List<ParametreGeneraux>> getAllParametreGeneraux() throws Exception{
        return new ResponseEntity<>(parametreGenerauxService.getAllParametreGeneraux(),HttpStatus.OK);}


       
    //Lire un user spécifique
    @GetMapping("/read/{id}")
    public ResponseEntity<?> getParametreGeneralById(@PathVariable Integer id) {
        return parametreGenerauxService.findById(id);
    }


        //Supprimer un paramètre général
           @DeleteMapping("/delete/{id}")
    @Operation(summary = "Supprimer un paramètre général")
    public String delete(@Valid @PathVariable Integer id) {
        return parametreGenerauxService.deleteByIdParametreGeneraux(id);
    }
    
}
