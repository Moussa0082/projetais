package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Sortie_Stock;

public interface Sortie_StockRepository extends JpaRepository<Sortie_Stock, Integer>{
    
}
