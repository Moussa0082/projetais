package projet.ais.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import projet.ais.models.Campagne;
import projet.ais.services.CampagneService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin
@RequestMapping("api-koumi/Campagne")
public class CampagneController {
    
    @Autowired
    CampagneService campagneService;

    @PostMapping("/addCampagne")
    @Operation(summary = "Ajout de campagne")
    public ResponseEntity<Campagne> saveCampagne(@RequestBody Campagne campagne) {
        return new ResponseEntity<>(campagneService.createCampagne(campagne), HttpStatus.CREATED);
    }
    
    @PutMapping("/update/{id}")
    @Operation(summary = "Modification de campagne")
    public ResponseEntity<Campagne> updatedCampagne(@RequestBody Campagne campagne, @PathVariable String id) {
        return new ResponseEntity<>(campagneService.updateCampagne(campagne, id), HttpStatus.OK);
    }

    @GetMapping("/getAllCampagne")
    public ResponseEntity<List<Campagne>> liste() {
        return new ResponseEntity<>(campagneService.getCampagnes(), HttpStatus.OK);
    }

    @GetMapping("/getAllCampagneByActeur/{id}")
    public ResponseEntity<List<Campagne>> liste(@PathVariable String id) {
        return new ResponseEntity<>(campagneService.getCampagneByActeur(id), HttpStatus.OK);
    }

    @PutMapping("/activer/{id}")
    @Operation(summary="Activation")
    public ResponseEntity<Campagne> activeCampagne(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(campagneService.active(id), HttpStatus.OK);
    }
    
    @PutMapping("/desactiver/{id}")
    @Operation(summary="Desactivation de la spéculation en fonction de l'id")
    public ResponseEntity<Campagne> desactiveCampagne(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(campagneService.desactive(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression du campagne")
    public String deleteCampagnes( @PathVariable String id) {
        return campagneService.deleteCampagne(id);
    }
}
