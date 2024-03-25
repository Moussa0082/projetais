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
import projet.ais.models.TypeMateriel;
import projet.ais.services.TypeMaterielService;

@RestController
@CrossOrigin
@RequestMapping("api-koumi/TypeMateriel")
public class TypeMaterielController {
    
    @Autowired
    TypeMaterielService typeMaterielService;

    @PostMapping("/create")
    @Operation(summary = "Creation de type de materiel")
   public ResponseEntity<TypeMateriel> createType(@RequestBody TypeMateriel typeMateriel){
    return new ResponseEntity<>(typeMaterielService.create(typeMateriel), HttpStatus.CREATED);
   }

   @PutMapping("/update/{id}")
    @Operation(summary = "Modification de type de materiel")
   public ResponseEntity<TypeMateriel> updateType(@RequestBody TypeMateriel typeMateriel, @PathVariable String id){
    return new ResponseEntity<>(typeMaterielService.updates(typeMateriel,id), HttpStatus.OK);
   }

   @PutMapping("/activer/{id}")
    @Operation(summary = "Activation de type de materiel")
   public ResponseEntity<TypeMateriel> activeType(@PathVariable String id) throws Exception{
    return new ResponseEntity<>(typeMaterielService.active(id), HttpStatus.OK);
   }

   @PutMapping("/desactiver/{id}")
   @Operation(summary = "Desactivation de type de materiel")
  public ResponseEntity<TypeMateriel> desactiverType(@PathVariable String id) throws Exception{
   return new ResponseEntity<>(typeMaterielService.desactive(id), HttpStatus.OK);
  }

   @GetMapping("/read")
   @Operation(summary = "Liste des types materiel")
   public ResponseEntity<List<TypeMateriel>> getAllTypeMateriel(){
    return new ResponseEntity<>(typeMaterielService.getTypeMateriel(), HttpStatus.OK);
   }

   @DeleteMapping("/delete/{id}")
   @Operation(summary = "Delete a type materiel")
   public String deleteTypes(@PathVariable String id){
    return typeMaterielService.deleteType(id);
   }
}
