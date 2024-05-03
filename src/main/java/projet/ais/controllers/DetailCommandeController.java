package projet.ais.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import projet.ais.models.DetailCommande;
import projet.ais.models.Magasin;
import projet.ais.services.DetailCommandeService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RestController
@CrossOrigin
@RequestMapping("/api-koumi")
public class DetailCommandeController {


    @Autowired
    DetailCommandeService detailCommandeService;


    @GetMapping("/getAllDetailByCommande/{idCommande}")
    @Operation(summary = "Liste des details commandes by commande")
    public ResponseEntity<List<DetailCommande>> listeDetailCommandeByCommande(@PathVariable String idCommande){
        return new ResponseEntity<>(detailCommandeService.getAllDetailCommandeByIdCommande(idCommande), HttpStatus.OK);
    }
    
}
