package projet.ais.controllers;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import projet.ais.models.Niveau1Pays;
import projet.ais.models.TypeActeur;
import projet.ais.repository.TypeActeurRepository;
import projet.ais.services.TypeActeurService;

@RestController
@RequestMapping("/typeActeur")
public class TypeActeurController {

    @Autowired
    private TypeActeurService typeActeurService;

    @Autowired
    private TypeActeurRepository typeActeurRepository;


     @PostMapping("/create")
     @Operation(summary = "Ajouté un type d'acteur")
     public ResponseEntity<String> createTypeActeur(@RequestBody TypeActeur typeActeur) {

        // Vérifier si le type d'acteur existe déjà
        TypeActeur typeActeurExistant = typeActeurRepository.findByLibelle(typeActeur.getLibelle());
        if (typeActeurExistant == null) {
            typeActeurService.createTypeActeur(typeActeur);
            return new ResponseEntity<>("Type d'acteur créer avec succès" , HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Type d'acetur " + typeActeurExistant.getLibelle() + " existe déjà", HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/update/{id}")
    @Operation(summary = "Modifier type acteur")
    public ResponseEntity<String> updateTypeActeur(@RequestBody TypeActeur typeActeur, @PathVariable String id) {
        TypeActeur typeActeurExistant = typeActeurRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("type d'acteur introuvable avec id :" +id));;

        if (typeActeurExistant != null) {
        typeActeurService.updateTypeActeur(typeActeur, id);
        return new ResponseEntity<>("Type d'acteur mis à jour avec succès", HttpStatus.OK);
    } else {
        return new ResponseEntity<>("Type d'acteur non existant avec l'id " + id, HttpStatus.BAD_REQUEST);
    }
}


           // Get Liste des  type acteur
      @GetMapping("/read")
      @Operation(summary = "Affichage de la liste des types d'acteur")
    public ResponseEntity<List<TypeActeur>> getAllTypeActeur() throws Exception {
        return new ResponseEntity<>(typeActeurService.getAllTypeActeur(), HttpStatus.OK);
    }

       //Supprimer type acteur
           @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression d'un type d'acteur")
    public ResponseEntity<String> deleteTypeActeur(@PathVariable String id){
        return new ResponseEntity<>(typeActeurService.deleteByIdTypeActeur(id), HttpStatus.OK);
    }


               //Activer type acteur
       @PutMapping("/activer/{id}")
    @Operation(summary="Activation d'un type d'acteur à travers son id")
    public ResponseEntity<TypeActeur> activeTypeActeur(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(typeActeurService.active(id), HttpStatus.OK);
    }

    //Desativer niveau 1 pays
    @PutMapping("/desactiver/{id}")
    @Operation(summary="Desactivation de niveau 1 pays à travers son id")
    public ResponseEntity<TypeActeur> desactiveTypeActeur(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(typeActeurService.desactive(id), HttpStatus.OK);
    }

}
