package projet.ais.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import projet.ais.models.Acteur;
import projet.ais.models.Rating;
import projet.ais.models.Stock;

public interface RatingRepository extends JpaRepository<Rating, String> {
        Optional<Rating> findByStockAndActeur(Stock stock, Acteur acteur);

        Page<Rating> findByStock(Stock stock, Pageable pageable);

        
        String countByStockIdStock(String idStock);

    
}
