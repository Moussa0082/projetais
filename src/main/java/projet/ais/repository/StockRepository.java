package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.Stock;
import java.util.*;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer>{
    
    Stock findByIdStock(Integer id);
    List<Stock> findByActeurIdActeur(Integer id);
    List<Stock> findByMagasinIdMagasin(Integer id);

    Stock findByNomProduitAndActeurIdActeur(String nom, Integer id);
}
