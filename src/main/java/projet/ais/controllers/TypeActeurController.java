package projet.ais.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projet.ais.models.TypeActeur;
import projet.ais.services.TypeActeurService;

@RestController
@RequestMapping("/typeActeur")
public class TypeActeurController {

    @Autowired
    private TypeActeurService typeActeurService;


     @PostMapping("/create")
     public ResponseEntity<String> createTypeActeur(@RequestBody TypeActeur typeActeur){
          
         typeActeurService.createTypeActeur(typeActeur);
       return new ResponseEntity<>("Type acteur créer avec succès", HttpStatus.OK);
        
    }
    
    @PutMapping("/update")
    public ResponseEntity<String> updateTypeActeur(@RequestBody TypeActeur typeActeur, Integer id){
         
        typeActeurService.updateTypeActeur(id, typeActeur);
      return new ResponseEntity<>("Type acteur modifier avec succès", HttpStatus.OK);
       
   }
}
