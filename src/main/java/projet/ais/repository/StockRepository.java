package projet.ais.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import projet.ais.models.CategorieProduit;
import projet.ais.models.Stock;
@Repository
public interface StockRepository extends JpaRepository<Stock, String>{

    Optional<Stock> findByNomProduit(String nomProduit);
    List<Stock> findByActeurIdActeur(String id);
    List<Stock> findBySpeculationIdSpeculation(String id);
    List<Stock> findBySpeculationNomSpeculation(String nomSpeculation);
    List<Stock> findByMagasinIdMagasin(String id);
   
    @Query("SELECT s FROM Stock s WHERE " +
    "(:nomProduit IS NULL OR s.nomProduit LIKE %:nomProduit%) AND " +
    "(:nomCategorie IS NULL OR s.speculation.categorieProduit.libelleCategorie = :nomCategorie) AND " +
    "(:speculations IS NULL OR s.speculation.nomSpeculation IN :speculations) AND " +
    "(:quantiteStock IS NULL OR s.quantiteStock = :quantiteStock) AND " +
    "(:prixMin IS NULL OR s.prix >= :prixMin) AND " +
    "(:prixMax IS NULL OR s.prix <= :prixMax)" +
    "ORDER BY s.dateAjout DESC")
        Page<Stock> findByProduit(
    @Param("nomProduit") String nomProduit, 
    @Param("nomCategorie") String nomCategorie, 
    @Param("speculations") List<String> speculations, 
    @Param("quantiteStock") Double quantiteStock, 
    @Param("prixMin") Integer prixMin, 
    @Param("prixMax") Integer prixMax, 
    Pageable pageable);


//     @Query("SELECT s FROM Stock s WHERE " +
//     "(:nomProduit IS NULL OR s.nomProduit = :nomProduit) AND " +
//     "(:prix IS NULL OR s.prix = :prix) AND " +
//     "(:quantiteStock IS NULL OR s.quantiteStock = :quantiteStock)")
//     Page<Stock> findByProduit(
//                         @Param("nomProduit") String nomProduit, 
//                         @Param("prix") Integer prix, 
//                         @Param("quantiteStock") Double quantiteStock, 
//                         Pageable pageable);

//     Page<Stock> findBySpeculationNomSpeculationAndPrixAndQuantiteStock(String nomSpeculation,int prix,double quantite,Pageable pageable);
//     Page<Stock> findBySpeculationNomSpeculationPrixMinAndPrixMaxAndQuantite(String nomSpeculation,int min,int max,int quantite,Pageable pageable);

    @Query("SELECT s FROM Stock s WHERE s.nomProduit LIKE %:nomProduit% ORDER BY s.dateAjout DESC")
    List<Stock> findTop10ByNomProduitContaining(@Param("nomProduit") String nomProduit);
    
    
//     @Query("SELECT s FROM Stock s WHERE s.nomProduit LIKE %:nomProduit% ORDER BY s.dateAjout DESC")
//     List<Stock> findTop10ByNomProduitContaining(@Param("nomProduit") String nomProduit);
// @Query("SELECT s FROM Stock s WHERE s.nomProduit = :nomProduit ORDER BY s.dateAjout DESC")
// List<Stock> findTop10ByNomProduit(@Param("nomProduit") String nomProduit);

    Stock findByNomProduitAndActeurIdActeur(String nom, String id);
    
    Stock findByCommandeIdCommande(String idCommande);
    // Stock findByMagasinIdMagasinAndCategorieProduitIdCategorieProduit(String idMagasin, String idCategorieProduit);
    Stock findByIdStock(String idStock);

    List<Stock> findByIdStockIn(List<String> idStock);
    // List<Stock> findByIdStock(List<String> idStock);
    //Recuperer les stocks par categorie produit
    List<Stock> findBySpeculation_CategorieProduitAndQuantiteStockGreaterThan(CategorieProduit categorie,double qte);
   
    //Recuperer les stocks par libelle categorie produit
        Page<Stock> findBySpeculation_CategorieProduit_libelleCategorieAndQuantiteStockGreaterThan(String libelle , Pageable pageable,double qte);
        // Page<Stock> findBySpeculation_CategorieProduit_filiere_libelleFiliere(String libelleFiliere, Pageable pageable);

      //test libelle
        Page<Stock> findAllBySpeculation_CategorieProduit_filiere_LibelleFiliereAndStatutSotckAndActeurStatutActeurAndQuantiteStockGreaterThan(String libelleFiliere, boolean statutSotck, boolean statutActeur , Pageable pageable,double qte);

    Page<Stock> findAllBySpeculation_CategorieProduit_filiere_LibelleFiliereAndStatutSotckAndActeurStatutActeurAndPaysAndQuantiteStockGreaterThan(String libelleFiliere, boolean statutSotck, boolean statutActeur ,String pays, Pageable pageable,double qte);
    Page<Stock> findAllBySpeculation_CategorieProduit_filiere_LibelleFiliereAndStatutSotckAndActeurStatutActeurAndPaysNotAndQuantiteStockGreaterThan(String libelleFiliere, boolean statutSotck, boolean statutActeur , String pays, Pageable pageable,double qte);

    //teste libelle categorie 
    Page<Stock> findAllBySpeculation_CategorieProduit_idCategorieProduitAndSpeculation_CategorieProduit_filiere_LibelleFiliereAndStatutSotckAndActeurStatutActeurAndQuantiteStockGreaterThan(String idCategorie , String libelleFiliere, boolean statutSotck, boolean statutActeur , Pageable pageable,double qte);
    
    Page<Stock> findAllBySpeculation_CategorieProduit_idCategorieProduitAndSpeculation_CategorieProduit_filiere_LibelleFiliereAndStatutSotckAndActeurStatutActeurAndPaysAndQuantiteStockGreaterThan(String idCategorie , String libelleFiliere, boolean statutSotck, boolean statutActeur ,String pays, Pageable pageable,double qte);
    Page<Stock> findAllBySpeculation_CategorieProduit_idCategorieProduitAndSpeculation_CategorieProduit_filiere_LibelleFiliereAndStatutSotckAndActeurStatutActeurAndPaysNotAndQuantiteStockGreaterThan(String idCategorie , String libelleFiliere, boolean statutSotck, boolean statutActeur , String pays, Pageable pageable,double qte);
        // Page<Stock> findBySpeculation_CategorieProduit_filiere_libelleFiliereAndPays(String libelleFiliere ,String pays, Pageable pageable);
    //Recuperer les stock par magasin et par categorieProduit
    List<Stock> findBySpeculation_CategorieProduit_IdCategorieProduitAndMagasin_IdMagasinAndQuantiteStockGreaterThan(String idCategorie, String idMagasin,double qte);
    // List<Stock> findBySpeculation_CategorieProduit_IdCategorieProduit(String idCategorie);
    List<Stock> findBySpeculation_CategorieProduit_IdCategorieProduitAndMagasin_IdMagasinAndActeurIdActeurAndQuantiteStockGreaterThan(
            String idCategorieProduit, String idMagasin,String idActeur,double qte);
//     Collection<Stock> findByNomProduit(String nomProduit);
    List<Stock> findByCommande_IdCommande(String id);
    List<Stock> findBySpeculation_CategorieProduit_IdCategorieProduitAndActeur_IdActeurAndQuantiteStockGreaterThan(String idCategorieProduit,
            String idActeur,double qte);
//     Page<Stock> findAllByStatutSotckAndActeurStatutActeur(boolean statutSotck,boolean statutActeur ,Pageable pageable);
    Page<Stock> findBySpeculation_CategorieProduitAndStatutSotckAndActeurStatutActeurAndQuantiteStockGreaterThan(CategorieProduit categorie, boolean statutSotck, boolean statutActeur,
            Pageable pageable,double qte);
    Page<Stock> findBySpeculation_CategorieProduit_IdCategorieProduit_AndMagasin_IdMagasinAndStatutSotckAndActeurStatutActeurAndQuantiteStockGreaterThan(
            String idCategorieProduit, String idMagasin, boolean statutSotck, boolean statutActeur,Pageable pageable,double qte);
    
            Page<Stock> findBySpeculation_CategorieProduit_IdCategorieProduit_AndActeur_IdActeurAndQuantiteStockGreaterThan(
            String idCategorieProduit, String idActeur,Pageable pageable,double qte);
            
    List<Stock> findBySpeculation_CategorieProduit_IdCategorieProduitAndMagasin_IdMagasinAndStatutSotckAndQuantiteStockGreaterThan(
            String idCategorieProduit, String idMagasin, boolean statutSotck, Pageable pageable,double qte);
   Page<Stock> findByMagasin_IdMagasinAndStatutSotckAndActeurStatutActeurAndQuantiteStockGreaterThan(String idMagasin, boolean statutSotck, boolean statutActeur,Pageable pageable,double qte);
   Page<Stock> findByActeur_IdActeurAndQuantiteStockGreaterThan(String idActeur, Pageable pageable,double qte);
Page<Stock> findByMagasin_IdMagasinAndActeur_IdActeurAndQuantiteStockGreaterThan(String idMagasin, String idActeur, Pageable pageable,double qte);
 
//  @Query("SELECT s FROM Stock s WHERE s.pays = :pays AND s.statutSotck = true AND s.acteurStatutActeur = true")
//     Page<Stock> findAllByPays(@Param("pays") String pays, Pageable pageable);

//     @Query("SELECT s FROM Stock s WHERE s.pays != :pays AND s.statutSotck = true AND s.acteurStatutActeur = true")
//     Page<Stock> findAllByPaysNot(@Param("pays") String pays, Pageable pageable);
        Page<Stock> findAllByPaysAndStatutSotckTrueAndActeurStatutActeurTrueAndQuantiteStockGreaterThan(String pays, Pageable pageable,double qte);
        Page<Stock> findAllByPaysNotAndStatutSotckTrueAndActeurStatutActeurTrueAndQuantiteStockGreaterThan(String pays, Pageable pageable,double qte);
        Page<Stock> findAllByStatutSotckTrueAndActeurStatutActeurTrueAndActeurNiveau3PaysActeurAndQuantiteStockGreaterThan(String niveau3PaysActeur,
                        Pageable pageable,double qte);

        Page<Stock> findAllByStatutSotckAndQuantiteStockGreaterThan(boolean StatutSotck, boolean statutActeur, Pageable pageable,double qte);

      
                Page<Stock> findBySpeculation_CategorieProduitAndPaysAndStatutSotckAndActeurStatutActeurAndQuantiteStockGreaterThan(CategorieProduit categorie, String pays ,
                boolean statutSotck, boolean statutActeur,
                Pageable pageable,double qte);
                Page<Stock> findBySpeculation_CategorieProduitAndPaysNotAndStatutSotckAndActeurStatutActeurAndQuantiteStockGreaterThan(CategorieProduit categorie, String pays ,
                boolean statutSotck, boolean statutActeur,
                Pageable pageable,double qte);

 Page<Stock> findBySpeculation_CategorieProduit_IdCategorieProduit_AndMagasin_IdMagasinAndStatutSotckAndQuantiteStockGreaterThan(
       String idCategorieProduit, String idMagasin, boolean statutSotck,Pageable pageable,double qte);

                Page<Stock> findBySpeculation_CategorieProduit_IdCategorieProduit_AndMagasin_IdMagasinAndPaysNotAndStatutSotckAndActeurStatutActeurAndQuantiteStockGreaterThan(
                        String idCategorieProduit, String idMagasin, String pays, boolean statutSotck, boolean statutActeur,Pageable pageable,double qte);

                        Page<Stock> findByMagasin_IdMagasinAndStatutSotckAndQuantiteStockGreaterThan( String idMagasin, boolean statutSotck,Pageable pageable,double qte);


                        Page<Stock> findByMagasin_IdMagasinAndPaysNotAndStatutSotckAndActeurStatutActeurAndQuantiteStockGreaterThan(String pays, String idMagasin, boolean statutSotck, boolean statutActeur,Pageable pageable,double qte);
                        Page<Stock> findAllByStatutSotckTrueAndPaysNotAndActeurStatutActeurTrueAndQuantiteStockGreaterThan(String pays,
                                Pageable complementPageable,double qte);

                        //test 
                        Page<Stock> findAllByStatutSotckTrueAndActeurStatutActeurTrueAndQuantiteStockGreaterThan( Pageable pageable,double qte);
                       
                        Page<Stock> findAllByStatutSotckTrueAndPaysAndActeurStatutActeurTrueAndQuantiteStockGreaterThan(String pays, Pageable pageable,double qte);
                        Page<Stock> findAllByStatutSotckAndActeurStatutActeurAndQuantiteStockGreaterThan(boolean StatutSotck, boolean statutActeur , Pageable pageable,double qte);
                        Page<Stock> findAllByStatutSotckTrueAndActeurStatutActeurTrueAndPaysNotAndQuantiteStockGreaterThan(String pays, Pageable pageable,double qte);
}
