package projet.ais.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import projet.ais.models.Acteur;
import projet.ais.models.Rating;
import projet.ais.services.RatingService;

import java.util.List;

@RestController
@RequestMapping("api-koumi/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    // @PostMapping("/stock/rate")
    // public Rating rateStock(@RequestParam String idStock, @RequestParam String idActeur, @RequestParam int stars) {
    //     return ratingService.addRatingToStock(idStock, idActeur, stars);
    // }


    // @PutMapping("/stock/rateUpdate")
    // public Rating updateRating(@RequestParam String idStock, @RequestParam String idActeur, @RequestParam int newStars) {
    //     return ratingService.updateRatingByActeurAndStock(idStock, idActeur, newStars);
    // }

    // @GetMapping("/stock/getAllActeurByStockRatingWithPagination")

    //     public Page<Acteur> getUsersWhoLikedStock(@RequestParam String idStock, 
    //                                         @RequestParam int page, 
    //                                         @RequestParam int size) {
    //     Pageable pageable = PageRequest.of(page, size);
    //     return ratingService.getUsersWhoLikedStock(idStock, pageable);
    // }


    // @GetMapping("/stock/{idStock}/count")
    // public String countRatingsByStockId(@PathVariable String idStock) {
    //     return ratingService.countRatingsByStockId(idStock);
    // }
    
}

