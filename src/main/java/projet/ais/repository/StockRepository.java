package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.Stock;
import java.util.*;

@Repository
public interface StockRepository extends JpaRepository<Stock, String>{
    
    // Stock findByIdStock(String id);
    List<Stock> findByActeurIdActeur(String id);
    List<Stock> findByMagasinIdMagasin(String id);

    Stock findByNomProduitAndActeurIdActeur(String nom, String id);
    Stock findByCommandeIdCommande(String idCommande);
    List<Stock> findByIdStock(String idStock);
    List<Stock> findByIdStockIn(List<String> idStock);
}
