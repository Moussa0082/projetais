package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Acteur;
import projet.ais.models.Alerte;

import java.util.*;

import projet.ais.models.TypeActeur;

public interface ActeurRepository extends JpaRepository<Acteur, Integer>{

     Acteur findByIdActeur(Integer idActeur);

     Acteur findByEmailActeurAndPassword(String emailActeur, String Password);

     Acteur findByEmailActeur(String emailActeur);

    //  Acteur findByAlerte(Alerte alerteList);
    //  Acteur findByTypeActeur(TypeActeur typeActeur);

     Acteur findByTypeActeurLibelle(String libelle);     

     List<Acteur> findByTypeActeurIdTypeActeur(Integer idTypeActeur);

    List <Acteur> findByTypeActeur(TypeActeur typeActeur);

    //Pour modifier le mot de passe du user
    Optional<Acteur> findByResetToken(String resetToken);


    Acteur findByEmailActeurAndTypeActeur(String emailActeur, TypeActeur typeActeur);
    
}
