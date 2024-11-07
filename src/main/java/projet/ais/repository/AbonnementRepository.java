package projet.ais.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.Abonnement;


@Repository
public interface AbonnementRepository  extends JpaRepository<Abonnement, String>{

    List<Abonnement> findByActeurIdActeur(String id);

      // Récupérer le dernier abonnement d'un acteur en fonction de la dateAjout
    // @Query("SELECT a FROM Abonnement a WHERE a.acteur.idActeur = :idActeur ORDER BY a.dateAjout DESC")
    // Abonnement findLatestAbonnementByActeurId(@Param("idActeur") String idActeur);
    Abonnement findTopByActeurIdActeurOrderByDateAjoutDesc(String idActeur);

}
