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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import projet.ais.models.Superficie;
import projet.ais.services.SuperficieService;

@RestController
@CrossOrigin
@RequestMapping("api-koumi/Superficie")
public class SuperficieController {
    

    @Autowired
    SuperficieService superficieService;
    
    @PostMapping("/addSuperficie")
    @Operation(summary = "Ajout de la superficie")
    public ResponseEntity<Superficie> saveSuperficie(@RequestBody Superficie superficie) {
        return new ResponseEntity<>(superficieService.createSuperficie(superficie), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Modification de la superficie")
    public ResponseEntity<Superficie> updatedSuperficie(@RequestBody Superficie superficie, String id) {
        return new ResponseEntity<>(superficieService.updateSuperficie(superficie, id), HttpStatus.OK);
    }

    @GetMapping("/getSuperficie")
    public ResponseEntity<List<Superficie>> liste() {
        return new ResponseEntity<>(superficieService.getAllSuperficie(), HttpStatus.OK);
    }

    @GetMapping("/getSuperficieByActeur/{id}")
    public ResponseEntity<List<Superficie>> listeByActeur(@PathVariable  String id) {
        return new ResponseEntity<>(superficieService.getAllSuperficieByActeur(id), HttpStatus.OK);
    }

    @GetMapping("/getAllSuperficieBySpeculation/{id}")
    public ResponseEntity<List<Superficie>> listeBySpeculation(@PathVariable  String id) {
        return new ResponseEntity<>(superficieService.getAllSuperficieBySpeculation(id), HttpStatus.OK);
    }

    @PutMapping("/activer/{id}")
    @Operation(summary="Activation")
    public ResponseEntity<Superficie> activeSuperficie(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(superficieService.active(id), HttpStatus.OK);
    }
    
    @PutMapping("/desactiver/{id}")
    @Operation(summary="Desactivation")
    public ResponseEntity<Superficie> desactiveSuperficie(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(superficieService.desactive(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression de la superficie ")
    public String deleteSuperficie( @PathVariable String id) {
        return superficieService.DeleteSuperficie(id);
    }
}
