package projet.ais.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import projet.ais.models.Magasin;
import projet.ais.services.MagasinService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@CrossOrigin
@RequestMapping("api-koumi/Magasin")
public class MagasinController {
    
    @Autowired
    MagasinService magasinService;

    @PostMapping("/addMagasin")
    public ResponseEntity<Magasin> saveMagasin(
        @Valid @RequestParam("magasin") String magasins,
        @Valid @RequestParam(value = "image",required = false) MultipartFile imageFile) throws Exception{

            Magasin magasin1 = new Magasin();
            try {
                magasin1 = new JsonMapper().readValue(magasins,Magasin.class);
            }  catch (JsonProcessingException e) {
                throw new Exception(e.getMessage());
            }
        Magasin saveMag = magasinService.createMagasin(magasin1, imageFile);
        return new ResponseEntity<>(saveMag, HttpStatus.CREATED);
    }
    
    @PutMapping("/update/{idMagasin}")
    public ResponseEntity<Magasin> updatedMagasin(
        @Valid @RequestParam("magasin") String magasin,
        @Valid @RequestParam(value = "image",required = false) MultipartFile imageFile, @PathVariable String idMagasin) throws Exception{

            Magasin magasin1 = new Magasin();
            try {
                magasin1 = new JsonMapper().readValue(magasin,Magasin.class);
            }  catch (JsonProcessingException e) {
                throw new Exception(e.getMessage());
            }
        Magasin updateMag = magasinService.updateMagasin(magasin1, imageFile,idMagasin);
        return new ResponseEntity<>(updateMag, HttpStatus.CREATED);
    }

    @PutMapping("/activer/{id}")
    @Operation(summary="Activation de magasin fonction de l'id de filiere")
    public ResponseEntity<Magasin> activeMagasin(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(magasinService.active(id), HttpStatus.OK);
    }

    @PutMapping("/desactiver/{id}")
    @Operation(summary="Desactivation de magasin fonction de l'id de filiere")
    public ResponseEntity<Magasin> desactiveMagasin(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(magasinService.desactive(id), HttpStatus.OK);
    }
    @GetMapping("/getAllMagagin")
    @Operation(summary = "Liste des magasins")
    public ResponseEntity<List<Magasin>> listeMagasin(){
        return new ResponseEntity<>(magasinService.getMagasin(), HttpStatus.OK);
    }

    @GetMapping("/getAllMagasinByActeur/{id}")
    @Operation(summary = "Liste des magasins par acteur")
    public ResponseEntity<List<Magasin>> listeMagasinByActeur(@PathVariable String id){
        return new ResponseEntity<>(magasinService.getMagasinByActeur(id), HttpStatus.OK);
    }
   
    @GetMapping("/getAllMagasinByActeurAndNiveau1Pays/{idActeur}/{idNiveau1Pays}")
    @Operation(summary = "Liste des magasins par region et par acteur")
    public ResponseEntity<List<Magasin>> listeMagasinByNiveau1PaysAndActeur(@PathVariable String idActeur, @PathVariable String idNiveau1Pays) throws Exception{
        return new ResponseEntity<>(magasinService.listeMagasinByNiveau1PaysAndActeur(idActeur, idNiveau1Pays), HttpStatus.OK);
    }

    @GetMapping("/getAllMagasinByPays/{id}")
    @Operation(summary = "Liste des magasins par niveau 1 pays")
    public ResponseEntity<List<Magasin>> listeMagasinByNiveau1Pays(@PathVariable String id){
        return new ResponseEntity<>(magasinService.getMagasinByNiveau1Pays(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression du magasin")
    public String supprimer(@PathVariable String id){
        return magasinService.supprimerMagagin(id);
    }
}
