package projet.ais.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Sortie_Stock;
import projet.ais.models.Stock;

public interface Sortie_StockRepository extends JpaRepository<Sortie_Stock, Integer>{
    

    Sortie_Stock findByIdSortieStock(Integer idSortieStock);

    List<Sortie_Stock> findByStockIdStock(int idStock);

    List<Sortie_Stock> findByDateSortieBetween(Date startDate, Date endDate);

    // List<Sortie_Stock> findByStockAndActeurIdActeur(Stock stock, Integer idActeur);

}
