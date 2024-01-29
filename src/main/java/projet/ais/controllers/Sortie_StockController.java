package projet.ais.controllers;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import projet.ais.models.Sortie_Stock;
import projet.ais.models.SousRegion;
import projet.ais.models.Stock;
import projet.ais.services.Sortie_StockService;

@RestController
@RequestMapping("/sortieStock")
public class Sortie_StockController {
    

    @Autowired
    private Sortie_StockService sortie_StockService;

    
      @PostMapping("/create")
     @Operation(summary = "Sortie de stock")
     public ResponseEntity<Sortie_Stock> createSortieStock(@RequestBody Sortie_Stock sortie_Stock) throws Exception {

        
            Sortie_Stock st =  sortie_StockService.createSortieStock(sortie_Stock);
            return new ResponseEntity<>( st, HttpStatus.OK);
       
    }

    @GetMapping("/byStockId/{idStock}")
    public ResponseEntity<List<Sortie_Stock>> getSortieStocksByStockId(@PathVariable int idStock) {
        List<Sortie_Stock> sortieStocks = sortie_StockService.getAllSortieStocksByStockId(idStock);
        return new ResponseEntity<>(sortieStocks, HttpStatus.OK);
    }

    // @GetMapping("/byActeur/{idActeur}")
    // public ResponseEntity<List<Sortie_Stock>> getSortieStocksByActeur(@PathVariable Stock stock, int idActeur) {
    //     List<Sortie_Stock> sortieStocks = sortie_StockService.getAllSortieStocksByActeur(stock, idActeur);
    //     return new ResponseEntity<>(sortieStocks, HttpStatus.OK);
    // }

    @GetMapping("/betweenDates")
    @Operation(summary  = "recherches de l'historique de la sortie des stocks entre 2 dates specifiques")
    public ResponseEntity<List<Sortie_Stock>> getSortieStocksBetweenDates(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        List<Sortie_Stock> sortieStocks = sortie_StockService.getSortieStocksBetweenDates(startDate, endDate);
        return new ResponseEntity<>(sortieStocks, HttpStatus.OK);
    }


    //Supprimer sortie stock
           @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression d'une sortie sortie")
    public ResponseEntity<String> deleteSortieStock(@PathVariable Integer id){
        return new ResponseEntity<>(sortie_StockService.deleteByIdSortieStock(id), HttpStatus.OK);
    }
    

}
