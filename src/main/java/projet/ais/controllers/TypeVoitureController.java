package projet.ais.controllers;

import java.util.List;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import projet.ais.models.TypeVoiture;
import projet.ais.services.TypeVoitureService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin
@RequestMapping("api-koumi/TypeVoiture")
public class TypeVoitureController {
    
    @Autowired
    TypeVoitureService typeVoitureService;

    @PostMapping("/create")
    @Operation(summary = "Creation de type de voiture")
   public ResponseEntity<TypeVoiture> createType(@RequestBody TypeVoiture typeVoiture){
    return new ResponseEntity<>(typeVoitureService.create(typeVoiture), HttpStatus.CREATED);
   }
    
    @PutMapping("/update/{id}")
    @Operation(summary = "Modification de type de voiture")
   public ResponseEntity<TypeVoiture> updateType(@RequestBody TypeVoiture typeVoiture, @PathVariable String id){
    return new ResponseEntity<>(typeVoitureService.updates(typeVoiture,id), HttpStatus.OK);
   }

   @PutMapping("/activer/{id}")
    @Operation(summary = "Activation de type de voiture")
   public ResponseEntity<TypeVoiture> activeType(@PathVariable String id) throws Exception{
    return new ResponseEntity<>(typeVoitureService.active(id), HttpStatus.OK);
   }

   @PutMapping("/desactiver/{id}")
   @Operation(summary = "Desactivation de type de voiture")
  public ResponseEntity<TypeVoiture> desactiverType(@PathVariable String id) throws Exception{
   return new ResponseEntity<>(typeVoitureService.desactive(id), HttpStatus.OK);
  }

   @GetMapping("/read")
   @Operation(summary = "Liste des types voitures")
   public ResponseEntity<List<TypeVoiture>> getAllTypeVoiture(){
    return new ResponseEntity<>(typeVoitureService.getTypeVoiture(), HttpStatus.OK);
   }

   @GetMapping("/listeByActeur/{id}")
   @Operation(summary = "Liste des types voitures")
   public ResponseEntity<List<TypeVoiture>> getAllTypeVoitureByActeur(@PathVariable String id){
    return new ResponseEntity<>(typeVoitureService.getTypeVoitureByActeur(id), HttpStatus.OK);
   }

   @DeleteMapping("/delete/{id}")
   @Operation(summary = "Delete a type voiture")
   public String deleteTypes(@PathVariable String id){
    return typeVoitureService.deleteType(id);
   }
}
