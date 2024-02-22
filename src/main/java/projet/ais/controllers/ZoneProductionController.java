package projet.ais.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import projet.ais.models.Unite;
import projet.ais.models.ZoneProduction;
import projet.ais.services.ZoneProductionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/ZoneProduction")
@CrossOrigin
public class ZoneProductionController {
    
    @Autowired
    ZoneProductionService  zoneProductionService;

    @PostMapping("/addZoneProduction")
    @Operation(summary = "Création de zone de production")
    public ResponseEntity<ZoneProduction> createZone(
         @Valid @RequestParam("zone") String zoneProduction,
         @Valid @RequestParam(value = "image", required = false)  MultipartFile imageFile) throws Exception{


            ZoneProduction zoneProductions = new  ZoneProduction();
            try {
                zoneProductions = new JsonMapper().readValue(zoneProduction,ZoneProduction.class);
            }  catch (JsonProcessingException e) {
                throw new Exception(e.getMessage());
            }

            ZoneProduction saveZone = zoneProductionService.createZoneProduction(zoneProductions, imageFile);
            return new ResponseEntity<>(saveZone, HttpStatus.OK);
    }

    @PutMapping("/updateZoneProduction/{id}")
    @Operation(summary = "Modification de zone de production")
    public ResponseEntity<ZoneProduction> updatezone(
         @Valid @RequestParam("zone") String zoneProduction,
         @Valid @RequestParam(value = "image" ,required = false) MultipartFile imageFile, @PathVariable String id) throws Exception{

            ZoneProduction zoneProductions = new  ZoneProduction();
            try {
                zoneProductions = new JsonMapper().readValue(zoneProduction,ZoneProduction.class);
            }  catch (JsonProcessingException e) {
                throw new Exception(e.getMessage());
            }

            ZoneProduction updatedZone = zoneProductionService.updateZoneProduction(zoneProductions,id, imageFile);
            return new ResponseEntity<>(updatedZone, HttpStatus.OK);
        }

    @PutMapping("/activer/{id}")
    @Operation(summary = "activation de la zone de production")
    public ResponseEntity<ZoneProduction> activeZone(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(zoneProductionService.active(id), HttpStatus.CREATED);
    }

    @PutMapping("/desactiver/{id}")
    @Operation(summary = "desactivation de la zone  de production" )
    public ResponseEntity<ZoneProduction> desactiveZone(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(zoneProductionService.desactive(id), HttpStatus.CREATED);
    }

        @GetMapping("/getAllZone")
        @Operation(summary = "Liste des zones de production")
        public ResponseEntity<List<ZoneProduction>> getAllZones() {
            return new ResponseEntity<>(zoneProductionService.getZoneProduction(), HttpStatus.OK);
        }
        
        @GetMapping("/getAllZonesByActeur/{id}")
        @Operation(summary = "Liste des zones de production")
        public ResponseEntity<List<ZoneProduction>> getAllZoneByActeur(String idActeur) {
            return new ResponseEntity<>(zoneProductionService.getZoneProductionByActeur(idActeur), HttpStatus.OK);
        }

        @DeleteMapping("/deleteZones/{id}")
        @Operation(summary = "Suppresion d'une zone production")
        public String supprimerZone(@PathVariable String id){
            return zoneProductionService.deleteZoneProduction(id);
        }
}
