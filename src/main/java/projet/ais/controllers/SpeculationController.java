package projet.ais.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projet.ais.models.Speculation;
import projet.ais.services.SpeculationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/Speculation")
public class SpeculationController {
    
    @Autowired
    SpeculationService speculationService;

    @PostMapping("/addSpeculation")
    public ResponseEntity<Speculation> createSpeculations(@RequestBody Speculation speculation) {
        return new ResponseEntity<>(speculationService.createSpeculation(speculation), HttpStatus.CREATED);
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<Speculation> updateSpeculations(@PathVariable Integer id, @RequestBody Speculation speculation) {
        return new ResponseEntity<>(speculationService.updateSpeculation(speculation, id), HttpStatus.OK);
    }

    @GetMapping("/getAllSpeculation")
    public ResponseEntity<List<Speculation>> getAllSpeculations() {
        return new ResponseEntity<>(speculationService.getAllSpeculation(), HttpStatus.OK);
    }
    
    @GetMapping("/getAllSpeculationByCategorie/{id}")
    public ResponseEntity<List<Speculation>> getAllSpeculationByCate(@PathVariable Integer id) {
        return new ResponseEntity<>(speculationService.getAllSpeculationByCategorie(id), HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/{id}")
    public String deleteSpeculation(Integer id) {
        return speculationService.deleteSpeculation(id);
    }
}
