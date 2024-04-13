package projet.ais.controllers;

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
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import projet.ais.models.Alertes;
import projet.ais.services.AlertesService;

@RestController
@CrossOrigin
@RequestMapping("api-koumi/alertes")
public class AlertesController {
    
    @Autowired
    AlertesService alertesService;

    @PostMapping("/create")
    @Operation(summary = "Ajout d'un alerte")
    public ResponseEntity<Alertes> createConeil(
            @Valid @RequestParam("alerte") String alerteString,
            @RequestParam(value = "audio", required = false) MultipartFile audio,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            @RequestParam(value = "video", required = false) MultipartFile video)
            throws Exception {
                
                Alertes alerte = new Alertes();
                try {
                    alerte = new JsonMapper().readValue(alerteString, Alertes.class);
                } catch (JsonProcessingException e) {
                    throw new Exception(e.getMessage());
                }
            
                // je le cree et le sauvegarde.
                Alertes savedalerte = alertesService.createAlertes(alerte, imageFile, audio, video);
            
                return new ResponseEntity<>(savedalerte, HttpStatus.CREATED);
            }


             @PutMapping("/update/{id}")
      @Operation(summary = "Mise à jour d'un alerte ")
      public ResponseEntity<Alertes> updatealerte(
              @PathVariable String id,
              @Valid @RequestParam("alerte") String alerteString,
              @RequestParam(value = "image", required = false)  MultipartFile imageFile,
              @RequestParam(value = "audio", required = false)  MultipartFile audio,
              @RequestParam(value = "video", required = false)  MultipartFile video
              ){
                Alertes alerte = new Alertes();
          try {
               alerte = new JsonMapper().readValue(alerteString, Alertes.class);
          } catch (JsonProcessingException e) {
              return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
          }

          try {
            Alertes alerteMisAjour = alertesService.updateAlertes(alerte, imageFile, audio, video ,id);
            return new ResponseEntity<>(alerteMisAjour, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
  
      }


    //      //liste alerte
    // @GetMapping("/listealerteByActeur/{id}")
    // @Operation(summary = "affichage de la liste des alerte par acteur")
    // public ResponseEntity<List<Alertes>> listealerteByActeur(@PathVariable String id){
    //     return  new ResponseEntity<>(alertesService.getAllalerteByActeur(id), HttpStatus.OK);
    // }

                 // Get Liste des  alerte
      @GetMapping("/read")
      @Operation(summary = "Liste globale des alertes")
    public ResponseEntity<List<Alertes>> getAllVehicule() {
        return new ResponseEntity<>(alertesService.getAllAlertes(), HttpStatus.OK);
    }


    //Desactiver un alerte methode
    @PutMapping("/disable/{id}")
    @Operation(summary = "Désactiver un alerte ")
    public ResponseEntity <String> disablealerte(@PathVariable String id) throws Exception{
    
        alertesService.desactive(id);
        return new ResponseEntity<>("alerte desactiver avec succes", HttpStatus.ACCEPTED);
    }

    //Desactiver un alerte methode
      @PutMapping("/enable/{id}")
    @Operation(summary = "Activer un alerte ")
    public ResponseEntity <String> enableVehicule(@PathVariable String id) throws Exception{
    
        alertesService.active(id);
        return new ResponseEntity<>("alerte activer avec succes", HttpStatus.ACCEPTED);
    }


             //Supprimer un alerte
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression d'un alerte")
    public ResponseEntity<String> deletealerte(@PathVariable String id){
        return new ResponseEntity<>(alertesService.deleteAlertes(id), HttpStatus.OK);
    }
}
