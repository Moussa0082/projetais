package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Acteur;
import java.util.*;

import projet.ais.models.TypeActeur;

public interface ActeurRepository extends JpaRepository<Acteur, Integer>{

     Acteur findByIdActeur(long idActeur);

     Acteur findByEmailActeurAndPassword(String emailActeur, String Password);

     Acteur findByEmailActeur(String emailActeur);

     Acteur findByTypeActeur(TypeActeur typeActeur);

     List<Acteur> findByTypeActeurIdTypeActeur(Integer idTypeActeur);
    
}
