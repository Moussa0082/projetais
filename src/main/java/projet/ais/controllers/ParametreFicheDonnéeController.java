package projet.ais.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import projet.ais.models.ParametreFicheDonnees;
import projet.ais.services.ParametreFicheDonneeService;

@RestController
@RequestMapping("/ParametreFicheDonnee")
public class ParametreFicheDonnéeController {
    
    @Autowired
    ParametreFicheDonneeService parametreFicheDonneeService;

    @PostMapping("/addParametre")
    @Operation(summary="Création du parametre fiche données")
    public ResponseEntity<ParametreFicheDonnees> saceParametre(@RequestBody ParametreFicheDonnees param ){
        return new ResponseEntity<>(parametreFicheDonneeService.createParametreFiche(param), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Modification du parametre")
    public ResponseEntity<ParametreFicheDonnees> modifier(@RequestBody ParametreFicheDonnees param , @PathVariable Integer id ){
        return new ResponseEntity<>(parametreFicheDonneeService.updateParametreFiche(param, id), HttpStatus.OK);
    }

    @GetMapping("/getAllParametres")
    @Operation(summary="Liste des parametre")
    public ResponseEntity<List<ParametreFicheDonnees>> listeParamatre(){
        return new ResponseEntity<>(parametreFicheDonneeService.getListe(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary="Suppression du parametre")
    public String deleteParametres(@PathVariable Integer id){
        return parametreFicheDonneeService.deleteParametreFiche(id);
    }
}
