package projet.ais.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import projet.ais.models.Conseil;
import projet.ais.models.Vehicule;
import projet.ais.services.ConseilService;

@RestController
@RequestMapping("/conseil")
public class ConseilController {


   @Autowired
   private ConseilService conseilService;



   
    @PostMapping("/create")
    @Operation(summary = "Ajout d'un conseil")
     public ResponseEntity<Conseil> createConeil(
            @Valid @RequestParam("conseil") String conseilString,
            @RequestParam(value = "audio", required = false) MultipartFile audio,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            @RequestParam(value = "video", required = false) MultipartFile video)
            throws Exception {
                

                Conseil conseil = new Conseil();
                try {
                    conseil = new JsonMapper().readValue(conseilString, Conseil.class);
                } catch (JsonProcessingException e) {
                    throw new Exception(e.getMessage());
                }
            
                // je le cree et le sauvegarde.
                Conseil savedConseil = conseilService.createConseil(conseil, imageFile, audio, video);
            
                return new ResponseEntity<>(savedConseil, HttpStatus.CREATED);
            }


             @PutMapping("/update/{id}")
      @Operation(summary = "Mise à jour d'un conseil ")
      public ResponseEntity<Conseil> updateConseil(
              @PathVariable String id,
              @Valid @RequestParam("conseil") String conseilString,
              @RequestParam(value = "image", required = false)  MultipartFile imageFile,
              @RequestParam(value = "audio", required = false)  MultipartFile audio,
              @RequestParam(value = "video", required = false)  MultipartFile video
              ){
              Conseil conseil = new Conseil();
          try {
               conseil = new JsonMapper().readValue(conseilString, Conseil.class);
          } catch (JsonProcessingException e) {
              return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
          }

          try {
            Conseil conseilMisAjour = conseilService.updateConseil(conseil, imageFile, audio, video ,id);
            return new ResponseEntity<>(conseilMisAjour, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
  
      }


         //liste conseil
    @GetMapping("/listeConseilByActeur/{id}")
    @Operation(summary = "affichage de la liste des conseil par acteur")
    public ResponseEntity<List<Conseil>> listeConseilByActeur(@PathVariable String id){
        return  new ResponseEntity<>(conseilService.getAllConseilByActeur(id), HttpStatus.OK);
    }

                 // Get Liste des  conseil
      @GetMapping("/read")
      @Operation(summary = "Liste globale des conseils")
    public ResponseEntity<List<Conseil>> getAllVehicule() {
        return new ResponseEntity<>(conseilService.getAllConseil(), HttpStatus.OK);
    }


    //Desactiver un conseil methode
    @PutMapping("/disable/{id}")
    @Operation(summary = "Désactiver un conseil ")
    public ResponseEntity <String> disableConseil(@PathVariable String id) throws Exception{
    
        conseilService.desactive(id);
        return new ResponseEntity<>("Conseil desactiver avec succes", HttpStatus.ACCEPTED);
    }

    //Desactiver un conseil methode
      @PutMapping("/enable/{id}")
    @Operation(summary = "Activer un conseil ")
    public ResponseEntity <String> enableVehicule(@PathVariable String id) throws Exception{
    
        conseilService.active(id);
        return new ResponseEntity<>("Conseil activer avec succes", HttpStatus.ACCEPTED);
    }


             //Supprimer un conseil
           @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression d'un conseil")
    public ResponseEntity<String> deleteConseil(@PathVariable String id){
        return new ResponseEntity<>(conseilService.deleteConseil(id), HttpStatus.OK);
    }



    
}
