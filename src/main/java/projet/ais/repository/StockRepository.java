package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Stock;

public interface StockRepository extends JpaRepository<Stock, Long>{
    
}
