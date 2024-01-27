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
import projet.ais.models.ZoneProduction;
import projet.ais.services.MagasinService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin
@RequestMapping("/Magasin")
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
    
    @PostMapping("/update/{id}")
    public ResponseEntity<Magasin> updatedMagasin(
        @Valid @RequestParam("magasin") String magasins,
        @Valid @RequestParam(value = "image") MultipartFile imageFile, @PathVariable Integer id) throws Exception{

            Magasin magasin1 = new Magasin();
            try {
                magasin1 = new JsonMapper().readValue(magasins,Magasin.class);
            }  catch (JsonProcessingException e) {
                throw new Exception(e.getMessage());
            }
        Magasin updateMag = magasinService.updateMagasin(magasin1, imageFile,id);
        return new ResponseEntity<>(updateMag, HttpStatus.CREATED);
    }

    @GetMapping("/getAllMagagin")
    @Operation(summary = "Liste des magasins")
    public ResponseEntity<List<Magasin>> listeMagasin(){
        return new ResponseEntity<>(magasinService.getMagasin(), HttpStatus.OK);
    }

    @GetMapping("/getAllMagaginByActeur/{id}")
    @Operation(summary = "Liste des magasins")
    public ResponseEntity<List<Magasin>> listeMagasinByActeur(@PathVariable Integer id){
        return new ResponseEntity<>(magasinService.getMagasinByActeur(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression du magasin")
    public String supprimer(@PathVariable Integer id){
        return magasinService.supprimerMagagin(id);
    }
}