package projet.ais.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;

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
import projet.ais.models.Intrant;
import projet.ais.services.IntrantService;

@RestController
@CrossOrigin
@RequestMapping("api-koumi/intrant")
public class IntrantController {

    @Autowired
    private IntrantService intrantService;

    
    
    @PostMapping("/create")
    @Operation(summary = "création d'un intrant")
     public ResponseEntity<Intrant> createIntrant(
            @Valid @RequestParam("intrant") String intrantString,
            @RequestParam(value = "image", required = false) MultipartFile imageFile)
            throws Exception {
                
                Intrant intrant = new Intrant();
                try {
                    intrant = new JsonMapper().readValue(intrantString, Intrant.class);
                } catch (JsonProcessingException e) {
                    throw new Exception(e.getMessage());
                }
            
                // je le cree et le sauvegarde.
                Intrant savedIntrant = intrantService.createIntrant(intrant, imageFile);
            
                return new ResponseEntity<>(savedIntrant, HttpStatus.CREATED);
            }


             @PutMapping("/update/{id}")
      @Operation(summary = "Mise à jour d'un intrant ")
      public ResponseEntity<Intrant> updateIntrant(
              @PathVariable String id,
              @Valid @RequestParam("intrant") String intrantString,
              @RequestParam(value = "image", required = false)  MultipartFile imageFile){
              Intrant intrant = new Intrant();
          try {
            intrant = new JsonMapper().readValue(intrantString, Intrant.class);
          } catch (JsonProcessingException e) {
              return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
          }

          try {
            Intrant intrantMisAjour = intrantService.updateIntrant(intrant, imageFile, id);
            return new ResponseEntity<>(intrantMisAjour, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
  
      }


         //liste intrant pas acteur
    @GetMapping("/listeIntrantByActeur/{id}")
    @Operation(summary = "affichage de la liste des intrants par acteur")
    public ResponseEntity<List<Intrant>> listeIntrantByActeur(@PathVariable String id){
        return  new ResponseEntity<>(intrantService.getAllIntrantByActeur(id), HttpStatus.OK);
    }

    // @GetMapping("/listeIntrantBySuperficie/{id}")
    // @Operation(summary = "affichage de la liste des intrants par superficie")
    // public ResponseEntity<List<Intrant>> listeIntrantBySuperficie(@PathVariable String id){
    //     return  new ResponseEntity<>(intrantService.getAllIntrantBySuperficie(id), HttpStatus.OK);
    // }

                 // Get Liste des  intrants
      @GetMapping("/read")
      @Operation(summary = "Liste globale des intrants")
    public ResponseEntity<List<Intrant>> getAllIntrant() {
        return new ResponseEntity<>(intrantService.getAllIntrant(), HttpStatus.OK);
    }


    @PutMapping("/disable/{id}")
    //Desactiver un intrant methode
    @Operation(summary = "Désactiver un intrant ")
    public ResponseEntity<String> disableIntrant(@PathVariable String id) throws Exception{
    
        intrantService.desactive(id);
        return new ResponseEntity<>("Intrant desactiver avec succes", HttpStatus.ACCEPTED);
    }

    //Aciver intrant
      @PutMapping("/enable/{id}")
      @Operation(summary = "Activer intrant ")
    public ResponseEntity <String> enableIntrant(@PathVariable String id) throws Exception{
    
        intrantService.active(id);
        return new ResponseEntity<>("Intrant activer avec succes", HttpStatus.ACCEPTED);
    }


             //Supprimer un intrant
           @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression d'un intrant")
    public ResponseEntity<String> deleteIntrant(@PathVariable String id){
        return new ResponseEntity<>(intrantService.deleteIntrant(id), HttpStatus.OK);
    }


    
}
