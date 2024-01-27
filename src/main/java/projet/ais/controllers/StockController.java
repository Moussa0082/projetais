package projet.ais.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import projet.ais.models.Stock;
import projet.ais.services.StockService;

@RestController
@RequestMapping("/Stock")
public class StockController {
    
    @Autowired
    StockService stockservice;

    @PostMapping("/addStock")
    @Operation(summary = "Création de stock")
    public ResponseEntity<Stock> saveStocks(
        @Valid @RequestParam("stock")  String addstocks,
        @Valid @RequestParam(value = "image", required = false) MultipartFile imageFile
        ) throws Exception{
            Stock stock = new Stock();

            try {
                stock = new JsonMapper().readValue(addstocks, Stock.class);
            } catch (JsonProcessingException e) {
                throw new Exception(e.getMessage());
            }

            Stock saveStock = stockservice.createStock(stock, imageFile);
            return new ResponseEntity<>(saveStock, HttpStatus.CREATED);
        }

        @PutMapping("/updateStock/{id}")
    @Operation(summary = "Modification de stock")
    public ResponseEntity<Stock> updatedStock(
        @Valid @RequestParam("stock")  String addstocks,
        @Valid @RequestParam(value = "image") MultipartFile imageFile,
        @PathVariable Integer id
        ) throws Exception{
            Stock stock = new Stock();

            try {
                stock = new JsonMapper().readValue(addstocks, Stock.class);
            } catch (JsonProcessingException e) {
                throw new Exception(e.getMessage());
            }

            Stock saveStock = stockservice.updateStock(stock, imageFile, id);
            return new ResponseEntity<>(saveStock, HttpStatus.OK);
        }

        

        @PutMapping("/updateQuantiteStock/{id}")
    @Operation(summary = "Modification de stock")
    public ResponseEntity<Stock> updatedQuantiteStock(
        @Valid @RequestParam("stock")  String updateQuantiteStocks,
        @PathVariable Integer id
        ) throws Exception{
            Stock stock = new Stock();

            try {
                stock = new JsonMapper().readValue(updateQuantiteStocks, Stock.class);
            } catch (JsonProcessingException e) {
                throw new Exception(e.getMessage());
            }

            Stock saveStock = stockservice.updateQuantiteStock(stock,id);
            return new ResponseEntity<>(saveStock, HttpStatus.OK);
        }

        @GetMapping("/getAllStocks")
        @Operation(summary = "Liste des stocks")
        public ResponseEntity<List<Stock>> listeStock(){
            return new ResponseEntity<>(stockservice.getAllStock(), HttpStatus.OK);
        }

        @GetMapping("/getAllStocksByActeurs/{id}")
        @Operation(summary = "Liste des stocks par d'un acteur ")
        public ResponseEntity<List<Stock>> listeStockParActeur(@PathVariable Integer id){
            return new ResponseEntity<>(stockservice.getAllStockByActeur(id), HttpStatus.OK);
        }

        @GetMapping("/getAllStocksByIdMagasin/{id}")
        @Operation(summary = "Liste des stocks par d'un magasin ")
        public ResponseEntity<List<Stock>> listeStockParMagasin(@PathVariable Integer id){
            return new ResponseEntity<>(stockservice.getAllStockByMagasin(id), HttpStatus.OK);
        }

        @DeleteMapping("/deleteStocks")
        @Operation(summary = "Suppression des stocks")
        public String supprimer(@PathVariable Integer id){
            return stockservice.deleteStock(id);
        }
}
