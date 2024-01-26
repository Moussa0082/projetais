package projet.ais.controllers;

import java.util.List;

import org.apache.catalina.connector.Response;
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
import projet.ais.models.RenvoieParametre;
import projet.ais.services.RenvoieParametreService;

@RestController
@CrossOrigin
@RequestMapping("/RenvoiParametre")
public class RenvoieParametreController {
    
    @Autowired
    RenvoieParametreService renvoieParametreService;

    @PostMapping("/addParametreRenvoie")
    @Operation(summary = "Création du parametre renvoie")
    public ResponseEntity<RenvoieParametre> saveParam(@RequestBody RenvoieParametre renvoieParametre){
        return new ResponseEntity<>(renvoieParametreService.createParametreRenvoie(renvoieParametre), HttpStatus.CREATED);
    }

    @PutMapping("/updateParametreRenvoie/{id}")
    @Operation(summary = "Modification du parametre renvoie")
    public ResponseEntity<RenvoieParametre> updateParam(@RequestBody RenvoieParametre renvoieParametre, @PathVariable Integer id){
        return new ResponseEntity<>(renvoieParametreService.updateParametreRenvoie(renvoieParametre,id), HttpStatus.CREATED);
    }

    @GetMapping("/getAllParametre")
    @Operation(summary = "Liste du parametre")
    public ResponseEntity<List<RenvoieParametre> > getAllParametre(){
        return new ResponseEntity<>(renvoieParametreService.getAllRenvoie(), HttpStatus.OK);
    }

    @GetMapping("/getAllParamRenvoieByIdParamFicheDonne/{id}")
    @Operation(summary = "Liste du parametre")
    public ResponseEntity<List<RenvoieParametre> > getAllParamRenvoieByParamFicheDonnees(@PathVariable Integer id){
        return new ResponseEntity<>(renvoieParametreService.getAllRenvoieByIdParametre(id), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public String deleteParam(@PathVariable Integer id){
        return renvoieParametreService.deleteParametreRenvoie(id);
    }
}


