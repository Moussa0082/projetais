package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
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
    //Recuperer les stock par magasin et par categorieProduit
    List<Stock> findBySpeculation_CategorieProduit_IdCategorieProduitAndMagasin_IdMagasin(String idCategorie, String idMagasin);
    // List<Stock> findBySpeculation_CategorieProduit_IdCategorieProduit(String idCategorie);
    List<Stock> findBySpeculation_CategorieProduit_IdCategorieProduitAndMagasin_IdMagasinAndActeurIdActeur(
            String idCategorieProduit, String idMagasin,String idActeur);
    Collection<Stock> findByNomProduit(String nomProduit);
    List<Stock> findByCommande_IdCommande(String id);
    List<Stock> findBySpeculation_CategorieProduit_IdCategorieProduitAndActeur_IdActeur(String idCategorieProduit,
            String idActeur);
    Page<Stock> findAllByStatutSotck(boolean statutSotck,Pageable pageable);
    Page<Stock> findBySpeculation_CategorieProduitAndStatutSotck(CategorieProduit categorie, boolean b,
            Pageable pageable);
    Page<Stock> findBySpeculation_CategorieProduit_IdCategorieProduit_AndMagasin_IdMagasinAndStatutSotck(
            String idCategorieProduit, String idMagasin, boolean statutSotck, Pageable pageable);
    List<Stock> findBySpeculation_CategorieProduit_IdCategorieProduitAndMagasin_IdMagasinAndStatutSotck(
            String idCategorieProduit, String idMagasin, boolean statutSotck, Pageable pageable);
   Page<Stock> findByMagasin_IdMagasinAndStatutSotck(String idMagasin, boolean statutSotck, Pageable pageable);
   Page<Stock> findByActeur_IdActeur(String idActeur, Pageable pageable);
Page<Stock> findByMagasin_IdMagasinAndActeur_IdActeur(String idMagasin, String idActeur, Pageable pageable);
 

}
