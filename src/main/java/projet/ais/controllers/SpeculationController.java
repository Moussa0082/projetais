package projet.ais.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import projet.ais.models.Niveau1Pays;
import projet.ais.models.Speculation;
import projet.ais.services.SpeculationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
// @CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("api-koumi/Speculation")
public class SpeculationController {
    
    @Autowired
    SpeculationService speculationService;

    @PostMapping("/addSpeculation")
    @Operation(summary="Création de spéculation")
    public ResponseEntity<Speculation> createSpeculations(@RequestBody Speculation speculation) {
        return new ResponseEntity<>(speculationService.createSpeculation(speculation), HttpStatus.CREATED);
    }
    
    @PutMapping("/update/{id}")
    @Operation(summary="Modification de la spéculation en fonction de l'id")
    public ResponseEntity<Speculation> updateSpeculations(@PathVariable String id, @RequestBody Speculation speculation) {
        return new ResponseEntity<>(speculationService.updateSpeculation(speculation, id), HttpStatus.OK);
    }
    
    @PutMapping("/activer/{id}")
    @Operation(summary="Activation de la spéculation en fonction de l'id")
    public ResponseEntity<Speculation> activeSpeculations(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(speculationService.active(id), HttpStatus.OK);
    }


    @GetMapping("/getAllSpeculationsWithPagination")
    public ResponseEntity<Page<Speculation>> getSpeculatios(@RequestParam() int page,
                                                  @RequestParam() int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Speculation> speculations = speculationService.getAllSpeculationPageable(pageable);
        return ResponseEntity.ok().body(speculations);
    }

    
    @PutMapping("/desactiver/{id}")
    @Operation(summary="Desactivation de la spéculation en fonction de l'id")
    public ResponseEntity<Speculation> desactiveSpeculations(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(speculationService.desactive(id), HttpStatus.OK);
    }

    // @GetMapping("/getAllSpeculation")
    // @Operation(summary="Récuperation des spéculations")
    // public ResponseEntity<List<Speculation>> getAllSpeculations() {
    //     return new ResponseEntity<>(speculationService.getAllSpeculation(), HttpStatus.OK);
    // }
     @GetMapping("/getAllSpeculation")
    public ResponseEntity<List<Speculation>> getAllSpeculation() {
        List<Speculation> speculations = speculationService.getAllSpeculation();

        if (speculations.isEmpty()) {
            throw new EntityNotFoundException("Speculation non trouvé");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        return ResponseEntity.ok()
                .headers(headers)
                .body(speculations);
    }
    
    @GetMapping("/getAllSpeculationByActeur/{id}")
    @Operation(summary="Récuperation des spéculations")
    public ResponseEntity<List<Speculation>> getAllByActeur(@PathVariable String id) {
        return new ResponseEntity<>(speculationService.getAllSpeculationByActeur(id), HttpStatus.OK);
    }

    @GetMapping("/getAllSpeculationByCategorie/{id}")
    @Operation(summary="Récuperation des spéculations en fonction de l'id de categorie")
    public ResponseEntity<List<Speculation>> getAllSpeculationByCate(@PathVariable String id) {
        return new ResponseEntity<>(speculationService.getAllSpeculationByCategorie(id), HttpStatus.OK);
    }
    
    @DeleteMapping("/deleteSpeculation/{id}")
    @Operation(summary="Suppression d'une spéculations en fonction de l'id")
    public String supprimerSpeculation(@PathVariable String id) {
        return speculationService.DeleteSpeculations(id);
    }

    
    @GetMapping("/by-categories/{idsJson}")
    public List<Speculation> getSpeculationsByCategories(@PathVariable String idsJson) {
        return speculationService.getSpeculationsByCategories(idsJson);
    }

}
