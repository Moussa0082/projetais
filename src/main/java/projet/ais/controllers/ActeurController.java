package projet.ais.controllers;

import org.apache.el.stream.Optional;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.*;
import projet.ais.models.Acteur;
import projet.ais.repository.ActeurRepository;
import projet.ais.services.ActeurService;

@RestController
@RequestMapping("/acteur")
public class ActeurController {


    @Autowired
    private ActeurService acteurService;

    @Autowired
    private ActeurRepository acteurRepository;

    @PostMapping("/create")
     public ResponseEntity<Acteur> createActeur(
            @Valid @RequestParam("acteur") String adminString,
            @RequestParam(value = "image1", required = false) MultipartFile imageFile1,
            @RequestParam(value = "image2", required = false) MultipartFile imageFile2)
            throws Exception {
                

                Acteur acteur = new Acteur();
                try {
                    acteur = new JsonMapper().readValue(adminString, Acteur.class);
                } catch (JsonProcessingException e) {
                    throw new Exception(e.getMessage());
                }
            
                // je le cree et le sauvegarde.
                Acteur savedActeur = acteurService.createActeur(acteur, imageFile1, imageFile2);
            
                return new ResponseEntity<>(savedActeur, HttpStatus.CREATED);
            }



             // Get Liste des  admin
      @GetMapping("/read")
    public ResponseEntity<List<Acteur>> getAllActeur() {
        return new ResponseEntity<>(acteurService.getAllActeur(), HttpStatus.OK);
    }

   
    @PutMapping("/disable/{id}")
    //Desactiver un admin methode
    public ResponseEntity <String> disableActeur(@PathVariable Integer id){
    
        acteurService.disableActeur(id);
        return new ResponseEntity<>("Acteur desactiver avec succes", HttpStatus.ACCEPTED);
    }

    //Aciver admin
      @PutMapping("/enable/{id}")
    //Desactiver un admin methode
    public ResponseEntity <String> enableAdmin(@PathVariable Integer id){
    
        acteurService.enableActeur(id);
        return new ResponseEntity<>("Acteur activer avec succes", HttpStatus.ACCEPTED);
    }


           //Supprimer un admin
           @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression d'un utilisateur")
    public ResponseEntity<String> deleteActeur(@PathVariable Integer id){
        return new ResponseEntity<>(acteurService.deleteByIdActeur(id), HttpStatus.OK);
    }

    
}
