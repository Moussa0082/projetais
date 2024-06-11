package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import projet.ais.models.Acteur;
import projet.ais.models.CategorieProduit;
import projet.ais.models.Magasin;
import projet.ais.models.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import java.util.*;

@Repository
public interface StockRepository extends JpaRepository<Stock, String>{
    
    // Stock findByIdStock(String id);
    
    Page<Stock> findAll(Pageable pageable);
    List<Stock> findByActeurIdActeur(String id);
    List<Stock> findBySpeculationIdSpeculation(String id);
    List<Stock> findByMagasinIdMagasin(String id);

    Stock findByNomProduitAndActeurIdActeur(String nom, String id);
    
    Stock findByCommandeIdCommande(String idCommande);
    // Stock findByMagasinIdMagasinAndCategorieProduitIdCategorieProduit(String idMagasin, String idCategorieProduit);
    Stock findByIdStock(String idStock);

    List<Stock> findByIdStockIn(List<String> idStock);
    // List<Stock> findByIdStock(List<String> idStock);
    //Recuperer les stocks par categorie produit    
    List<Stock> findBySpeculation_CategorieProduit(CategorieProduit categorie);
    //Recuperer les stocks par libelle categorie produit
Page<Stock> findBySpeculation_CategorieProduit_libelleCategorie(String libelle , Pageable pageable);
Page<Stock> findBySpeculation_CategorieProduit_filiere_libelleFiliere(String libelle , Pageable pageable);
    //Recuperer les stock par magasin et par categorieProduit
    List<Stock> findBySpeculation_CategorieProduit_IdCategorieProduitAndMagasin_IdMagasin(String idCategorie, String idMagasin);
    // List<Stock> findBySpeculation_CategorieProduit_IdCategorieProduit(String idCategorie);
    List<Stock> findBySpeculation_CategorieProduit_IdCategorieProduitAndMagasin_IdMagasinAndActeurIdActeur(
            String idCategorieProduit, String idMagasin,String idActeur);
    Collection<Stock> findByNomProduit(String nomProduit);
    List<Stock> findByCommande_IdCommande(String id);
    List<Stock> findBySpeculation_CategorieProduit_IdCategorieProduitAndActeur_IdActeur(String idCategorieProduit,
            String idActeur);
    Page<Stock> findAllByStatutSotckAndActeurStatutActeur(boolean statutSotck,boolean statutActeur ,Pageable pageable);
    Page<Stock> findBySpeculation_CategorieProduitAndStatutSotckAndActeurStatutActeur(CategorieProduit categorie, boolean statutSotck, boolean statutActeur,
            Pageable pageable);
    Page<Stock> findBySpeculation_CategorieProduit_IdCategorieProduit_AndMagasin_IdMagasinAndStatutSotckAndActeurStatutActeur(
            String idCategorieProduit, String idMagasin, boolean statutSotck, boolean statutActeur,Pageable pageable);
    List<Stock> findBySpeculation_CategorieProduit_IdCategorieProduitAndMagasin_IdMagasinAndStatutSotck(
            String idCategorieProduit, String idMagasin, boolean statutSotck, Pageable pageable);
   Page<Stock> findByMagasin_IdMagasinAndStatutSotckAndActeurStatutActeur(String idMagasin, boolean statutSotck, boolean statutActeur,Pageable pageable);
   Page<Stock> findByActeur_IdActeur(String idActeur, Pageable pageable);
Page<Stock> findByMagasin_IdMagasinAndActeur_IdActeur(String idMagasin, String idActeur, Pageable pageable);
 
//  @Query("SELECT s FROM Stock s WHERE s.pays = :pays AND s.statutSotck = true AND s.acteurStatutActeur = true")
//     Page<Stock> findAllByPays(@Param("pays") String pays, Pageable pageable);

//     @Query("SELECT s FROM Stock s WHERE s.pays != :pays AND s.statutSotck = true AND s.acteurStatutActeur = true")
//     Page<Stock> findAllByPaysNot(@Param("pays") String pays, Pageable pageable);
Page<Stock> findAllByPaysAndStatutSotckTrueAndActeurStatutActeurTrue(String pays, Pageable pageable);
Page<Stock> findAllByPaysNotAndStatutSotckTrueAndActeurStatutActeurTrue(String pays, Pageable pageable);
Page<Stock> findAllByStatutSotckTrueAndActeurStatutActeurTrueAndActeurNiveau3PaysActeur(String niveau3PaysActeur,
                Pageable pageable);


}
