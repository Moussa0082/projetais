package projet.ais.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import projet.ais.models.Acteur;
import projet.ais.models.TypeActeur;
import projet.ais.repository.TypeActeurRepository;

@Service
public class TypeActeurService {

    @Autowired
    private TypeActeurRepository typeActeurRepository;

  
    //    public ResponseEntity<String> createTypeActeur(TypeActeur typeActeur) {
    //      if (typeActeurRepository.findByCodeTypeActeur(typeActeur.getCo == null) {
    //          typeActeurRepository.save(typeActeur);

    //          return new ResponseEntity<>("Type Acteur créer avec succès", HttpStatus.CREATED);
    //         } else {
                
    //             return new ResponseEntity<>("Type Acteur déjà existant.", HttpStatus.BAD_REQUEST);
    //      }
     
    // }

    
}
