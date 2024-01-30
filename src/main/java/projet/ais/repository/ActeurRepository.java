package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Acteur;
import java.util.*;

import projet.ais.models.TypeActeur;

public interface ActeurRepository extends JpaRepository<Acteur, String>{

     Acteur findByIdActeur(String idActeur);

     Acteur findByEmailActeurAndPassword(String emailActeur, String Password);

     Acteur findByEmailActeur(String emailActeur);

     Acteur findByTypeActeur(TypeActeur typeActeur);

     Acteur findByTypeActeurLibelle(String libelle);     

     List<Acteur> findByTypeActeurIdTypeActeur(String idTypeActeur);

     Acteur findByTypeActeur(Acteur acteur);

    Acteur findByEmailActeurAndTypeActeur(String emailActeur, TypeActeur typeActeur);
    
}
