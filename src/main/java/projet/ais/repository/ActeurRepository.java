package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Acteur;
import projet.ais.models.Alerte;

import java.util.*;

import projet.ais.models.TypeActeur;

public interface ActeurRepository extends JpaRepository<Acteur, String>{

     Acteur findByIdActeur(String idActeur);

     Acteur findByEmailActeurAndPassword(String emailActeur, String Password);

     Acteur findByEmailActeur(String emailActeur);
     
     Acteur findByWhatsAppActeur(String whatsAppActeur);

    //  Recuperer la liste des mails acteurs existants dans la base de donnés 
     List<Acteur> findAllByEmailActeur(String emailActeur);

    //  Acteur findByAlerte(Alerte alerteList);
    //  Acteur findByTypeActeur(TypeActeur typeActeur);

     Acteur findByTypeActeurLibelle(String libelle);     

     List<Acteur> findByTypeActeurIdTypeActeur(String idTypeActeur);

    List <Acteur> findByTypeActeur(TypeActeur typeActeur);

    //Pour modifier le mot de passe du user
    Optional<Acteur> findByResetToken(String resetToken);


    Acteur findByEmailActeurAndTypeActeur(String emailActeur, TypeActeur typeActeur);
    
}
