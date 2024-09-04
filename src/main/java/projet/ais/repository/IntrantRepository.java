

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
    List<Intrant> findAllByCategorieProduit_IdCategorieProduitAndQuantiteIntrantGreaterThan(String idCategorieProduit,double quantite);

    //test  categorie
    // Page<Intrant> findAllByCategorieProduit_IdCategorieProduitAndStatutIntrantTrueAndActeurStatutActeurTrueAndQuantiteIntrantGreaterThan(String idCategorieProduit, Pageable pageable,double qte);
    
    //Intrant par categorie et par pays
    Page<Intrant> findAllByCategorieProduit_IdCategorieProduitAndStatutIntrantTrueAndPaysAndActeurStatutActeurTrueAndQuantiteIntrantGreaterThan(String pays,String idCategorieProduit, Pageable pageable,double qte);
    
    //Intrant par categorie et non par pays
    Page<Intrant> findAllByCategorieProduit_IdCategorieProduitAndStatutIntrantTrueAndActeurStatutActeurTrueAndPaysNotAndQuantiteIntrantGreaterThan(String pays,String idCategorieProduit, Pageable pageable,double qte);
   
    //Non utiliser
    Page<Intrant> findAllByCategorieProduit_libelleCategorieAndQuantiteIntrantGreaterThan(String libelle , Pageable pageable,double qte);
    
    //Intrant par libelle categorie
    Page<Intrant> findAllByCategorieProduit_filiere_libelleFiliereAndStatutIntrantTrueAndActeurStatutActeurTrueAndQuantiteIntrantGreaterThan(String libelle , Pageable pageable,double qte);
    
    //Intrant par libelle categorie
    Page<Intrant> findAllByCategorieProduit_filiere_libelleFiliereAndQuantiteIntrantGreaterThan(String libelle , Pageable pageable,double qte);
   
    //test
    // Page<Intrant> findAllByCategorieProduit_filiere_LibelleFiliereAndStatutIntrantAndActeurStatutActeurAndQuantiteIntrantGreaterThan(String libelleFiliere, boolean statutIntrant, boolean statutActeur, Pageable pageable,double qte);
  
    //test
    Page<Intrant> findAllByCategorieProduit_filiere_LibelleFiliereAndStatutIntrantAndActeurStatutActeurAndQuantiteIntrantGreaterThan(String libelleFiliere, boolean statutIntrant, boolean statutActeur, Pageable pageable,double qte);
  
    Page<Intrant> findAllByCategorieProduit_filiere_LibelleFiliereAndStatutIntrantAndActeurStatutActeurAndPaysAndQuantiteIntrantGreaterThan(String libelleFiliere, boolean statutIntrant, boolean statutActeur, String pays, Pageable pageable,double qte);
    Page<Intrant> findAllByCategorieProduit_filiere_LibelleFiliereAndStatutIntrantAndActeurStatutActeurAndPaysNotAndQuantiteIntrantGreaterThan(String libelleFiliere,boolean statutIntrant, boolean statutActeur, String pays, Pageable pageable,double qte);

    Page<Intrant> findAllByCategorieProduit_idCategorieProduitAndCategorieProduit_filiere_LibelleFiliereAndStatutIntrantAndActeurStatutActeurAndPaysAndQuantiteIntrantGreaterThan(String idCategorie ,String libelleFiliere, boolean statutIntrant, boolean statutActeur, String pays, Pageable pageable,double qte);
    Page<Intrant> findAllByCategorieProduit_idCategorieProduitAndCategorieProduit_filiere_LibelleFiliereAndStatutIntrantAndActeurStatutActeurAndPaysNotAndQuantiteIntrantGreaterThan(String idCategorie ,String libelleFiliere,boolean statutIntrant, boolean statutActeur, String pays, Pageable pageable,double qte);
    // Page<Intrant> findAllByCategorieProduit_filiere_LibelleFiliere(String libelleFiliere, Pageable pageable);

    //Intrant par libelle categorie et  par pays
    Page<Intrant> findAllByCategorieProduit_filiere_libelleFiliereAndPaysAndStatutIntrantTrueAndActeurStatutActeurTrueAndQuantiteIntrantGreaterThan(String pays , String libelle , Pageable pageable,double qte);
    
    //Intrant par libelle categorie et non par pays
    Page<Intrant> findAllByCategorieProduit_filiere_libelleFiliereAndStatutIntrantTrueAndActeurStatutActeurTrueAndPaysNotAndQuantiteIntrantGreaterThan(String pays , String libelle , Pageable pageable,double qte);
    
    // Collection<Intrant> findByNomIntrant(String nomIntrant);
        
         //ancien
        Page<Intrant> findByCategorieProduit_IdCategorieProduitAndStatutIntrantAndActeurStatutActeurAndQuantiteIntrantGreaterThan(String idCategorieProduit, boolean statutIntrant,
        boolean statutActeur,  Pageable pageable,double qte);

    Page<Intrant> findByCategorieProduit_IdCategorieProduitAndStatutIntrantAndPaysAndActeurStatutActeurAndQuantiteIntrantGreaterThan(String idCategorieProduit, String pays, boolean statutIntrant,
         boolean statutActeur,  Pageable pageable,double qte);

    Page<Intrant> findByActeur_IdActeurAndQuantiteIntrantGreaterThan(String idActeur, Pageable pageable,double qte);

    Page<Intrant> findAllByStatutIntrantAndActeurStatutActeurAndQuantiteIntrantGreaterThan(boolean statutIntrant, boolean statutActeur , Pageable pageable,double qte);

    //test get all
   Page<Intrant> findAllByStatutIntrantTrueAndActeurStatutActeurTrueAndQuantiteIntrantGreaterThan(Pageable pageable,double qte);
   
   Page<Intrant> findAllByStatutIntrantTrueAndPaysAndActeurStatutActeurTrueAndQuantiteIntrantGreaterThan(String pays, Pageable pageable,double qte);

//    @Query("SELECT i FROM Intrant i JOIN i.acteur a WHERE i.statutIntrant = :statutIntrant AND a.statutActeur = :statutActeur AND LOWER(i.pays) <> LOWER(:pays)")
//    Page<Intrant> findAllByStatutIntrantAndActeurStatutActeurAndPaysNot(@Param("statutIntrant") boolean statutIntrant,
//                                                                        @Param("statutActeur") boolean statutActeur,
//                                                                        @Param("pays") String pays,
//                                                                        Pageable pageable);
   Page<Intrant> findAllByStatutIntrantTrueAndActeurStatutActeurTrueAndPaysNotAndQuantiteIntrantGreaterThan(String pays, Pageable pageable,double qte);

    Page<Intrant> findAllByCategorieProduit_IdCategorieProduitAndStatutIntrantTrueAndActeurStatutActeurTrueAndQuantiteIntrantGreaterThan(
        String idCategorieProduit, Pageable pageable,double qte);


    // Page<Intrant> findAllByStatutIntrantTrueAndPaysAndActeurStatutActeurTrue(String pays, Pageable pageable);
}
