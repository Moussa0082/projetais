package projet.ais.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import projet.ais.models.Acteur;
import projet.ais.models.TypeActeur;

public interface ActeurRepository extends JpaRepository<Acteur, String>{

     Acteur findByIdActeur(String idActeur);

     Acteur findByEmailActeurAndPassword(String emailActeur, String Password);

     Acteur findByEmailActeur(String emailActeur);
     Acteur findByUsername(String unsername);
     Acteur findByEmailActeurAndWhatsAppActeur(String emailActeur,String whatsAppActeur);
    //  Acteur findByWhatsAppActeurAndTelephoneActr(String emailActeur, String numero);
     
     Acteur findByWhatsAppActeur(String whatsAppActeur);
    // List<Acteur> findByWhatsAppActeur(String whatsAppActeur);

    //  Recuperer la liste des mails acteurs existants dans la base de donnés 
     List<Acteur> findAllByEmailActeur(String emailActeur);

    @Query(
        "SELECT a FROM Acteur a WHERE " +
        "(:typeActeurs IS NULL OR element(a.typeActeur).libelle IN :typeActeurs) AND " +
        "(:speculations IS NULL OR element(a.speculation).nomSpeculation IN :speculations)"
    )
    Page<Acteur> findByActeurWithCritere(
        @Param("typeActeurs") List<String> typeActeurs,
        @Param("speculations") List<String> speculations,
        Pageable pageable
    );
    
    

    Acteur findByTypeActeurLibelle(String libelle);     
    List<Acteur> findByTypeActeur_Libelle(String libelle);     

    List<Acteur> findByTypeActeurIdTypeActeur(String idTypeActeur);

     // Méthode pour trouver tous les acteurs par libellé de type d'acteur
    List<Acteur> findAllByTypeActeur_Libelle(String libelle);

    List <Acteur> findByTypeActeur(TypeActeur typeActeur);

    //Pour modifier le mot de passe du user
    Optional<Acteur> findByResetToken(String resetToken);


    Acteur findByEmailActeurAndTypeActeur(String emailActeur, TypeActeur typeActeur);

    List<Acteur> findAllByTypeActeur(TypeActeur typeActeur);

    Acteur findByEmailActeurAndTypeActeurIn(String emailActeur, List<TypeActeur> typeActeur);

    List<Acteur> findAllByTypeActeurLibelle(String libelle);
    // List<Acteur> findAllByTypeActeurIdTypeActeur(String id);

    Acteur findByEmailActeurAndResetToken(String emailActeur, String resetToken);
    Acteur findByWhatsAppActeurAndResetToken(String wathsApp, String resetToken);

    Acteur findByCodeActeur(String codeActeur);

    Acteur findByCodeActeurAndNomActeur(String codeActeur, String nomActeur);
    
}
