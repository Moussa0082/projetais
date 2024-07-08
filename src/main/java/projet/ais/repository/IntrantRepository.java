

package projet.ais.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import projet.ais.models.Intrant;
import projet.ais.models.Superficie;

public interface IntrantRepository extends JpaRepository<Intrant , String> {

    Intrant findByIdIntrant(String idIntrant);
    
    Optional<Intrant> findByNomIntrant(String nomIntrant);


    List<Intrant> findByIdIntrantIn(List<String> idIntrants);

    List<Intrant> findAllByActeurIdActeur(String idIntrant);
    // List<Intrant> findAllBySpeculationIdSpeculation(String idSpeculation);
    List<Intrant> findAllByCategorieProduit_IdCategorieProduit(String idCategorieProduit);

    //Intrant par categorie et par pays
    Page<Intrant> findAllByCategorieProduit_IdCategorieProduitAndStatutIntrantTrueAndPaysAndActeurStatutActeurTrue(String pays,String idCategorieProduit, Pageable pageable);
    
    //Intrant par categorie et non par pays
    Page<Intrant> findAllByCategorieProduit_IdCategorieProduitAndStatutIntrantTrueAndActeurStatutActeurTrueAndPaysNot(String pays,String idCategorieProduit, Pageable pageable);
   
    //Non utiliser
    Page<Intrant> findAllByCategorieProduit_libelleCategorie(String libelle , Pageable pageable);
    
    //Intrant par libelle categorie
    Page<Intrant> findAllByCategorieProduit_filiere_libelleFiliereAndStatutIntrantTrueAndActeurStatutActeurTrue(String libelle , Pageable pageable);
    
    //Intrant par libelle categorie
    Page<Intrant> findAllByCategorieProduit_filiere_libelleFiliere(String libelle , Pageable pageable);
    
    Page<Intrant> findAllByCategorieProduit_filiere_LibelleFiliereAndStatutIntrantAndActeurStatutActeurAndPays(String libelleFiliere, boolean statutIntrant, boolean statutActeur, String pays, Pageable pageable);
    Page<Intrant> findAllByCategorieProduit_filiere_LibelleFiliereAndStatutIntrantAndActeurStatutActeurAndPaysNot(String libelleFiliere,boolean statutIntrant, boolean statutActeur, String pays, Pageable pageable);

    Page<Intrant> findAllByCategorieProduit_idCategorieProduitAndCategorieProduit_filiere_LibelleFiliereAndStatutIntrantAndActeurStatutActeurAndPays(String idCategorie ,String libelleFiliere, boolean statutIntrant, boolean statutActeur, String pays, Pageable pageable);
    Page<Intrant> findAllByCategorieProduit_idCategorieProduitAndCategorieProduit_filiere_LibelleFiliereAndStatutIntrantAndActeurStatutActeurAndPaysNot(String idCategorie ,String libelleFiliere,boolean statutIntrant, boolean statutActeur, String pays, Pageable pageable);
    // Page<Intrant> findAllByCategorieProduit_filiere_LibelleFiliere(String libelleFiliere, Pageable pageable);

    //Intrant par libelle categorie et  par pays
    Page<Intrant> findAllByCategorieProduit_filiere_libelleFiliereAndPaysAndStatutIntrantTrueAndActeurStatutActeurTrue(String pays , String libelle , Pageable pageable);
    
    //Intrant par libelle categorie et non par pays
    Page<Intrant> findAllByCategorieProduit_filiere_libelleFiliereAndStatutIntrantTrueAndActeurStatutActeurTrueAndPaysNot(String pays , String libelle , Pageable pageable);
    
    // Collection<Intrant> findByNomIntrant(String nomIntrant);
        
         //ancien
        Page<Intrant> findByCategorieProduit_IdCategorieProduitAndStatutIntrantAndActeurStatutActeur(String idCategorieProduit, boolean statutIntrant,
        boolean statutActeur,  Pageable pageable);

    Page<Intrant> findByCategorieProduit_IdCategorieProduitAndStatutIntrantAndPaysAndActeurStatutActeur(String idCategorieProduit, String pays, boolean statutIntrant,
         boolean statutActeur,  Pageable pageable);

    Page<Intrant> findByActeur_IdActeur(String idActeur, Pageable pageable);

    Page<Intrant> findAllByStatutIntrantAndActeurStatutActeur(boolean statutIntrant, boolean statutActeur , Pageable pageable);

   Page<Intrant> findAllByStatutIntrantTrueAndPaysAndActeurStatutActeurTrue(String pays, Pageable pageable);

//    @Query("SELECT i FROM Intrant i JOIN i.acteur a WHERE i.statutIntrant = :statutIntrant AND a.statutActeur = :statutActeur AND LOWER(i.pays) <> LOWER(:pays)")
//    Page<Intrant> findAllByStatutIntrantAndActeurStatutActeurAndPaysNot(@Param("statutIntrant") boolean statutIntrant,
//                                                                        @Param("statutActeur") boolean statutActeur,
//                                                                        @Param("pays") String pays,
//                                                                        Pageable pageable);
   Page<Intrant> findAllByStatutIntrantTrueAndActeurStatutActeurTrueAndPaysNot(String pays, Pageable pageable);

    Page<Intrant> findAllByCategorieProduit_IdCategorieProduitAndStatutIntrantTrueAndActeurStatutActeurTrue(
        String idCategorieProduit, Pageable pageable);


    // Page<Intrant> findAllByStatutIntrantTrueAndPaysAndActeurStatutActeurTrue(String pays, Pageable pageable);
}
