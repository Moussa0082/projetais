package projet.ais.services;

import java.util.List;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import projet.ais.IdGenerator;
import projet.ais.models.Acteur;
import projet.ais.models.Rating;
import projet.ais.models.Stock;
import projet.ais.repository.ActeurRepository;
import projet.ais.repository.RatingRepository;
import projet.ais.repository.StockRepository;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ActeurRepository acteurRepository;


     public Rating addRatingToStock(String stockId, String idActeur, int stars) {
        Stock stock = stockRepository.findById(stockId).orElseThrow(() -> new RuntimeException("Stock non trouver"));
        Acteur acteur = acteurRepository.findById(idActeur).orElseThrow(() -> new RuntimeException("Acteur  non trouver"));

        Optional<Rating> existingRatingOpt = ratingRepository.findByStockAndActeur(stock, acteur);

        Rating rating;
        if (existingRatingOpt.isPresent()) {
            rating = existingRatingOpt.get();
            rating.setStars(stars);
        } else {
            rating = new Rating();
            rating.setIdRating(idGenerator.genererCode());
            rating.setStars(stars);
            rating.setStock(stock);
            rating.setActeur(acteur);
        }

        return ratingRepository.save(rating);
    }

    public Rating updateRatingByActeurAndStock(String idStock, String idActeur, int newStars) {
        Stock stock = stockRepository.findById(idStock).orElseThrow(() -> new RuntimeException("Stock non trouvé"));
        Acteur acteur = acteurRepository.findById(idActeur).orElseThrow(() -> new RuntimeException("Acteur non trouvé"));

        Rating rating = ratingRepository.findByStockAndActeur(stock, acteur)
                .orElseThrow(() -> new RuntimeException("Rating non trouvé"));

        rating.setStars(newStars);

        return ratingRepository.save(rating);
    }

    public Page<Acteur> getUsersWhoLikedStock(String idStock, Pageable pageable) {
        Stock stock = stockRepository.findById(idStock).orElseThrow(() -> new RuntimeException("Stock non trouver"));
        Page<Rating> ratings = ratingRepository.findByStock(stock, pageable);
        return ratings.map(Rating::getActeur);
    }

    public String countRatingsByStockId(String idStock) {
        return ratingRepository.countByStockIdStock(idStock);
    }

    

    
}
