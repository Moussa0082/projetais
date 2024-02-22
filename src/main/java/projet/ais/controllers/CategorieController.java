package projet.ais.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import projet.ais.models.CategorieProduit;
import projet.ais.services.CategorieService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;



@RestController
@CrossOrigin
@RequestMapping("api-koumi/Categorie")
public class CategorieController {
    
    @Autowired
    CategorieService categorieService;

    @PostMapping("/addCategorie")
    @Operation(summary="Création de categorie de produit")
    public ResponseEntity<CategorieProduit> createCategories(@RequestBody CategorieProduit categorieProduit) {
        return new ResponseEntity<>(categorieService.createCategorie(categorieProduit), HttpStatus.OK);
    }
    
    @PutMapping("/update/{id}")
    @Operation(summary="Modification de categorie de produit à travers son id")
    public ResponseEntity<CategorieProduit> updateCategories(@PathVariable String id, @RequestBody CategorieProduit categorieProduit) {
        return new ResponseEntity<>(categorieService.updateCategorie(categorieProduit, id), HttpStatus.OK);
    }

    @PutMapping("/activer/{id}")
    @Operation(summary="Activation de categorie de produit à travers son id")
    public ResponseEntity<CategorieProduit> activeCategories(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(categorieService.active(id), HttpStatus.OK);
    }

    @PutMapping("/desactiver/{id}")
    @Operation(summary="Desactivation de categorie de produit à travers son id")
    public ResponseEntity<CategorieProduit> desactiveCategories(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(categorieService.desactive(id), HttpStatus.OK);
    }

    @GetMapping("/allCategorie")
    @Operation(summary="Récuperation de tout les catégories de categorie de produit")
    public ResponseEntity<List<CategorieProduit>> getAllCategories() {
        return new ResponseEntity<>(categorieService.getAllCategorie(), HttpStatus.OK);
    }

    @GetMapping("/allCategorieByFiliere/{id}")
    @Operation(summary="Récuperation de tout les catégories de categorie de produit en fonction de l'id de filiere")
    public ResponseEntity<List<CategorieProduit>> getAllCategorieByIdFilieres(@PathVariable String id) {
        return new ResponseEntity<>(categorieService.getAllCategorieByIdFiliere(id), HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/{id}")
    @Operation(summary="Supprimé de catégories de produit en fonction de l'id ")
    public String deleteFilieres(@PathVariable String id) {
        return categorieService.deleteCategorie(id);
    }


}
