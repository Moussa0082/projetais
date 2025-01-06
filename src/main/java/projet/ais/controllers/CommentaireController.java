package projet.ais.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import java.util.*;
import projet.ais.services.CommentaireService;
import projet.ais.models.CategorieProduit;
import projet.ais.models.Commentaire;
import projet.ais.models.Conseil;

@RestController
@RequestMapping("api-koumi/commentaire")
public class CommentaireController {
    
    @Autowired
    CommentaireService commentaireService;

    @PostMapping("/addCommentaire")
    @Operation(summary = "Ajouter un commentaire")
    public ResponseEntity<Commentaire> addCommentaire(@RequestBody Commentaire commentaire) {
        Commentaire c = commentaireService.createCommentaire(commentaire);
        return new ResponseEntity<>(c, HttpStatus.CREATED);
    } 

    @GetMapping("/getAllCommentaire")
    @Operation(summary = "Récupérer tous les commentaires")
    public ResponseEntity<List<Commentaire>> getAllCommentaire() {
        return new ResponseEntity<>(commentaireService.getAllCommentaire(), HttpStatus.OK);
    }

    @GetMapping("/getCommentaireWithPagination")
    @Operation(summary = "Récupérer tous les commentaires avec pagination")
    public ResponseEntity<Page<Commentaire>> getCommentWithPagiantion(@RequestParam() int page,
                                                  @RequestParam() int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Commentaire> c = commentaireService.getAllCommentaireByPageable(pageable);
        return ResponseEntity.ok().body(c);
    }
  

    // get by id
    @GetMapping("/getCommentaireById/{id}")
    @Operation(summary = "Récupérer un commentaire par son id")
    public ResponseEntity<Commentaire> getCommentaireById(@PathVariable long id) {
        Commentaire c = commentaireService.getCommentaireById(id);
        return new ResponseEntity<>(c, HttpStatus.OK);
    }

    @DeleteMapping("/deleteCommentaire/{id}")
    @Operation(summary = "Supprimer un commentaire")
    public ResponseEntity<String> deleteCommentaire(@PathVariable long id) {
        String message = commentaireService.deleteCommentaire(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
