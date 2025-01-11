package projet.ais.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import projet.ais.models.CategorieProduit;
import projet.ais.models.Materiels;
import projet.ais.models.Stock;
import projet.ais.repository.StockRepository;
import projet.ais.services.FileUploade;
import projet.ais.services.StockService;

@RestController
// @CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("api-koumi/Stock")
public class StockController {
    
    @Autowired
    StockService stockService;
    @Autowired
    StockRepository stockRepository;
    @Autowired
    FileUploade fileUploade;

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

            Stock saveStock = stockService.createStock(stock, imageFile);
        //  System.out.println("controller : "+saveStock.toString());
            return new ResponseEntity<>(saveStock, HttpStatus.CREATED);
        }

    
    @PutMapping("/updateStock/{idStock}")
    @Operation(summary = "Modification de stock")
    public ResponseEntity<Stock> updatedStock(
        @Valid @RequestParam("stock")  String addstocks,
        @Valid @RequestParam(value = "image",required = false) MultipartFile imageFile,
        @PathVariable String idStock
        ) throws Exception{
            Stock stock = new Stock();

            try {
                stock = new JsonMapper().readValue(addstocks, Stock.class);
            } catch (JsonProcessingException e) {
                throw new Exception(e.getMessage());
            }

            Stock saveStock = stockService.updateStock(stock, imageFile, idStock);
            return new ResponseEntity<>(saveStock, HttpStatus.OK);
        }

    //     @PostMapping(value = "/zxing/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    // public ResponseEntity<BufferedImage> zxingQRCode(@RequestBody String barcode) throws Exception {
    //     return  ResponseEntity.ok(stockService.generateQRCodeImage(barcode));
    // }


     @PutMapping("/activer/{id}")
    public ResponseEntity<Stock> activeStock(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(stockService.active(id), HttpStatus.OK);
    }

        @PutMapping("/desactiver/{id}")
    public ResponseEntity<Stock> desactiveStock(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(stockService.desactive(id), HttpStatus.OK);
    }

    @GetMapping("getStockById/{idStock}")
    public Stock getStockById(@PathVariable String idStock) {
        return stockService.getStockById(idStock);
    }

   
    @PutMapping("/updateView/{id}")
    @Operation(summary = "Update view")
    public ResponseEntity<Stock> updateViews(@PathVariable String id) throws Exception{
        return new ResponseEntity<>(stockService.updateNbViev(id), HttpStatus.OK);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Stock>> searchStocks(
            @RequestParam String nomProduit) {
        List<Stock> stocks = stockService.searchStocksByProductName(nomProduit);
        return ResponseEntity.ok(stocks);
    }
    
    @GetMapping("/{stockId}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable String stockId) {
    try {
        // Récupérer le nom de l'image associée au véhicule
        Stock stock = stockRepository.findByIdStock(stockId);
        if (stock == null || stock.getPhoto() == null) {
            return ResponseEntity.notFound().build();
        }

        String imageName = stock.getPhoto();

        // Récupérer l'image à partir du serveur FTP
        byte[] imageBytes = fileUploade.getImageByName(imageName);

        // Détecter le type de contenu de l'image en fonction de son extension
    MediaType contentType = detectContentType(imageName);

    // Retourner l'image avec le type de contenu approprié
    return ResponseEntity.ok()
            .contentType(contentType)
            .body(imageBytes);
} catch (IOException e) {
    e.printStackTrace();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
}
}

private MediaType detectContentType(String imageName) {
    String[] parts = imageName.split("\\.");
    if (parts.length > 1) {
        String extension = parts[parts.length - 1].toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "png":
                return MediaType.IMAGE_PNG;
            case "gif":
                return MediaType.IMAGE_GIF;
            // Ajoutez d'autres cas pour les types de contenu supplémentaires si nécessaire
            default:
                break;
        }
    }
    // Par défaut, retourner MediaType.APPLICATION_OCTET_STREAM
    return MediaType.APPLICATION_OCTET_STREAM;
}
  

@PutMapping("/{id}/quantite")
public Stock updateQuantiteStock(@PathVariable String id, @RequestParam double quantite) throws Exception {
    return stockService.updateQuantiteStock(id, quantite);
}


        @GetMapping("/getAllStocks")
        @Operation(summary = "Liste des stocks")
        public ResponseEntity<List<Stock>> listeStock(){
            return new ResponseEntity<>(stockService.getAllStock(), HttpStatus.OK);
        }

        //

    @GetMapping("/getStockByCritereWithPagination")
    @Operation(summary = "Recuperer les stocks par critères")
    public ResponseEntity<Page<Stock>> getStocksWithCritere(
            @RequestParam(required = false) String nomProduit,
            @RequestParam(required = false) String nomCategorie,
            @RequestParam(required = false) List<String> speculations,
            @RequestParam(required = false) Double quantiteStock,
            @RequestParam(required = false) Integer prixMin,
            @RequestParam(required = false) Integer prixMax,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Stock> stocks = stockService.getStocksByProduct(nomProduit,nomCategorie,speculations,quantiteStock, prixMin, prixMax, pageable);
        return ResponseEntity.ok().body(stocks);
    }


        
        @GetMapping("/getAllStocksByActeurWithPagination")
        public ResponseEntity<Page<Stock>> getStocksByActeur(@RequestParam String idActeur,
                                                                @RequestParam int page,
                                                                @RequestParam int size) {
  
            Pageable pageable = PageRequest.of(page, size);
            Page<Stock> stocks = stockService.getStocksByActeurWithPagination(idActeur, pageable);
    
            return ResponseEntity.ok().body(stocks);
        }

        @GetMapping("/getAllStocksByCategorieWithPagination")
        public ResponseEntity<Page<Stock>> getStocksByCategorie(@RequestParam String idCategorie,
                                                                @RequestParam int page,
                                                                @RequestParam int size) {
            CategorieProduit categorie = new CategorieProduit();
            categorie.setIdCategorieProduit(idCategorie);
    
            Pageable pageable = PageRequest.of(page, size);
            Page<Stock> stocks = stockService.getStocksByCategorieWithPagination(categorie, pageable);
    
            return ResponseEntity.ok().body(stocks);
        }


        

    @GetMapping("/getAllStocksByCategorieAndMagasinWithPagination")
    public ResponseEntity<Page<Stock>> listeStockByCategorieProduitAndMagasinWithPagination(
            @RequestParam String idCategorie,
            @RequestParam String idMagasin,
            @RequestParam int page,
            @RequestParam int size) {

        CategorieProduit categorie = new CategorieProduit();
        categorie.setIdCategorieProduit(idCategorie);

        Pageable pageable = PageRequest.of(page, size);
        Page<Stock> stocks = stockService.listeStockByCategorieProduitAndMagasinWithPagination(idCategorie, idMagasin, pageable);

        return ResponseEntity.ok().body(stocks);
    }

    @GetMapping("/getAllStocksByMagasinAndActeurWithPagination")
    public ResponseEntity<Page<Stock>> getStocksByMagasinAndActeurWithPagination(
            @RequestParam String idMagasin,
            @RequestParam String idActeur,
            @RequestParam int page,
            @RequestParam int size) {


        Pageable pageable = PageRequest.of(page, size);
        Page<Stock> stocks = stockService.getStocksByMagasinAndActeurWithPagination(idMagasin, idActeur,pageable);

        return ResponseEntity.ok().body(stocks);
    }

    @GetMapping("/listeStockByLibelleCategorie")
    public ResponseEntity<Page<Stock>> getStocksByLibelleCategorie(
        @RequestParam() String libelle,
        @RequestParam() int page,
        @RequestParam() int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Stock> stocks = stockService.getAllStockByLibelleCategorie(libelle, pageable);
        return ResponseEntity.ok().body(stocks);
    }
    
  

    @GetMapping("/getAllStocksByMagasinWithPagination")
    public ResponseEntity<Page<Stock>> getStocksByMagasinWithPagination(
            @RequestParam String idMagasin,
            @RequestParam int page,
            @RequestParam int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Stock> stocks = stockService.getStocksByMagasinWithPagination(idMagasin, pageable);

        return ResponseEntity.ok().body(stocks);
    }

    @GetMapping("/getAllStocksByPays")
    public ResponseEntity<Page<Stock>> getStocksByPaysWithPagination(
            @RequestParam String nomPays,
            @RequestParam int page,
            @RequestParam int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Stock> stocks = stockService.getStocksByPaysWithPagination(nomPays, pageable);
        return ResponseEntity.ok().body(stocks);
    }
   
    @GetMapping("/getAllByFiliereAndPays")
    public ResponseEntity<Page<Stock>> getStocksByFilierePaysWithPagination(
            @RequestParam String libelle,
            @RequestParam String nomPays,
            @RequestParam int page,
            @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Stock> stocks = stockService.getAllByFiliereAndPays(libelle,nomPays, pageable);
        return ResponseEntity.ok().body(stocks);
    }

        @GetMapping("/getStocksByPaysWithPagination")
        public ResponseEntity<Page<Stock>> getAllStocksPageableByPays(
                // @RequestParam String niveau3PaysActeur,
                @RequestParam int page,
                @RequestParam int size) {
    
            Pageable pageable = PageRequest.of(page, size);
            Page<Stock> stocks = stockService.getAllStocksPageableByPays(pageable);
    
            return ResponseEntity.ok(stocks);
        }

        @GetMapping("/getStocksByCategorieAndActeur")
        public ResponseEntity<Page<Stock>> getAllStocksByCateAndActeur(
                @RequestParam String idCategorie,
                @RequestParam String idActeur,
                @RequestParam int page,
                @RequestParam int size) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Stock> stocks = stockService.getAllStockByActeurAndCategorie(idCategorie,idActeur, pageable);
            return ResponseEntity.ok(stocks);
        }

        @GetMapping("/getStocksByPaysAndMagasinWithPagination")
        public Page<Stock> getAllStocksPageableByPaysAndMagasin(@RequestParam String idMagasin, Pageable pageable) {
            return stockService.getAllStockPageableByPaysByMagasin(idMagasin, pageable);
        }


        @GetMapping("/getStocksByPaysAndMagasinAndCategorieProduitWithPagination")
        public Page<Stock> getAllStocksPageableByPaysAndMagasins(@RequestParam String idCategorieProduit, @RequestParam String idMagasin, Pageable pageable) {
            return stockService.getAllStockPageableByPaysByMagasinAndCategorie(idCategorieProduit, idMagasin, pageable);
        }

        // @GetMapping("/getStocksByPaysAndMagasinAndCategorieProduitWithPagination")
        // public Page<Stock> getAllStocksPageableByPaysAndMagasin(@RequestParam String idCategorieProduit, @RequestParam String idMagasin,  Pageable pageable) {
        //     return stockService.getAllStockPageableByPaysByMagasinAndCategorie(idCategorieProduit, idMagasin, pageable);
        // }

        @GetMapping("/getAllStocksByCategorieAndPaysWithPagination")
        public ResponseEntity<Page<Stock>> listeStockByCategorieProduitAndPaysWithPagination(
                @RequestParam String idCategorie,
                @RequestParam String niveau3PaysActeur,
                @RequestParam int page,
                @RequestParam int size) {
    
            CategorieProduit categorie = new CategorieProduit();
            categorie.setIdCategorieProduit(idCategorie);
    
            Pageable pageable = PageRequest.of(page, size);
            Page<Stock> stocks = stockService.getAllStockPageableByPaysByCategorie(categorie, niveau3PaysActeur,pageable);
    
            return ResponseEntity.ok().body(stocks);
        }


        @GetMapping("/getAllStocksByCategorieAndFiliere")
        public ResponseEntity<Page<Stock>> listeStockByCategorieAndLibelleFiliere(
                @RequestParam String idCategorie,
                @RequestParam String libelleFiliere,
                @RequestParam int page,
                @RequestParam int size) {
    
            CategorieProduit categorie = new CategorieProduit();
            categorie.setIdCategorieProduit(idCategorie);

            Pageable pageable = PageRequest.of(page, size);
            Page<Stock> stocks = stockService.getAllStockPageableByPaysByCategorieAndFiliere(idCategorie, libelleFiliere,pageable);
    
            return ResponseEntity.ok().body(stocks);
        }
        // @GetMapping("/getAllStocksByCategorieAndFiliere")
        // public ResponseEntity<Page<Stock>> listeStockByCategorieAndLibelleFiliere(
        //         @RequestParam String idCategorie,
        //         @RequestParam String libelleFiliere,
        //         @RequestParam String niveau3PaysActeur,
        //         @RequestParam int page,
        //         @RequestParam int size) {
    
        //     CategorieProduit categorie = new CategorieProduit();
        //     categorie.setIdCategorieProduit(idCategorie);
    
        //     Pageable pageable = PageRequest.of(page, size);
        //     Page<Stock> stocks = stockService.getAllStockPageableByPaysByCategorieAndFiliere(idCategorie, libelleFiliere, niveau3PaysActeur,pageable);
    
        //     return ResponseEntity.ok().body(stocks);
        // }
    



        @PutMapping("/update-pays")
        public String updatePaysForStocks() {
            stockService.updatePaysForStocks();
            return "Mise à jour de la colonne pays pour tous les stocks réussie";
        }

        @GetMapping("/getAllStocksWithPagination")
        public ResponseEntity<Page<Stock>> getAllStocksPageable(
                @RequestParam int page,
                @RequestParam int size) {
    
            Pageable pageable = PageRequest.of(page, size);
            Page<Stock> stocks = stockService.getAllStocksPageable(pageable);
    
            return ResponseEntity.ok(stocks);
        }

        @GetMapping("/getAllStockAndStatutsWithPagination")
        public ResponseEntity<Page<Stock>> getAllStocksPageableAndStock(
                @RequestParam int page,
                @RequestParam int size) {
    
            Pageable pageable = PageRequest.of(page, size);
            Page<Stock> stocks = stockService.getAllStocksPageableAndStatut(pageable);
    
            return ResponseEntity.ok(stocks);
        }

        @GetMapping("/getAllStockByPaysWithPagination")
        public ResponseEntity<Page<Stock>> getAllStocksPageableByPaysAndStatut(
                @RequestParam String niveau3PaysActeur,
                @RequestParam int page,
                @RequestParam int size) {
    
            Pageable pageable = PageRequest.of(page, size);
            Page<Stock> stocks = stockService.getAllStockPageableByPaysAndStatut(niveau3PaysActeur, pageable);
    
            return ResponseEntity.ok(stocks);
        }
        
    // @GetMapping("all")
    // public List<Stock> allUsers(@RequestParam(name = "page",defaultValue = "0") Integer page) {
      
    //   Integer size = 2;
    //   Pageable pageable = PageRequest.of(page, size);
    //   Page<Stock> pageStock = stockRepository.findAll(pageable);
  
    //   return pageStock.getContent();
    // }
  
    

        @GetMapping("/getAllStocksByActeurs/{id}")
        @Operation(summary = "Liste des stocks par d'un acteur ")
        public ResponseEntity<List<Stock>> listeStockParActeur(@PathVariable String id){
            return new ResponseEntity<>(stockService.getAllStockByActeur(id), HttpStatus.OK);
        }
        
        // @GetMapping("/getAllStocksByLibelleCategorie/{libelle}")
        // @Operation(summary = "Liste des stocks par libelle categorie ")
        // public ResponseEntity<List<Stock>> listeStockParLibelleCategorie(@PathVariable String libelle){
        //     return new ResponseEntity<>(stockService.getAllStockByLibelleCategorie(libelle), HttpStatus.OK);
        // }


        @GetMapping("/getAllStocksBySpeculation/{id}")
        @Operation(summary = "Liste des stocks par d'un acteur ")
        public ResponseEntity<List<Stock>> listeStockParSpeculation(@PathVariable String id){
            return new ResponseEntity<>(stockService.getAllStockBySpeculation(id), HttpStatus.OK);
        }
        // Recuperer les stocks par categorie produit
          // Endpoint pour récupérer les stocks par catégorie
    @GetMapping("/categorieProduit/{idCategorie}")
    public List<Stock> getStocksByCategorie(@PathVariable String idCategorie) {
        // Ici, vous pouvez utiliser l'ID de la catégorie pour récupérer l'objet CategorieProduit
        // Si vous n'avez pas l'ID de la catégorie mais que vous avez l'objet CategorieProduit, vous pouvez passer l'objet directement à la méthode du service
        CategorieProduit categorie = new CategorieProduit();
        categorie.setIdCategorieProduit(idCategorie); // Définissez l'ID de la catégorie

        // Utiliser le service pour récupérer les stocks par catégorie
        return stockService.getStocksByCategorie(categorie);
    }

        @GetMapping("/getAllStocksByidMagasin/{id}")
        @Operation(summary = "Liste des stocks par d'un magasin ")
        public ResponseEntity<List<Stock>> listeStockParMagasin(@PathVariable String id){
            return new ResponseEntity<>(stockService.getAllStockByMagasin(id), HttpStatus.OK);
        }

        @GetMapping("/categorieAndMagasin/{idCategorie}/{idMagasin}")
        public List<Stock> getStocksByCategorieAndMagasin(@PathVariable String idCategorie, @PathVariable String idMagasin) {
            return stockService.getStocksByCategorieAndMagasin(idCategorie, idMagasin);
        }

        @GetMapping("/categorieAndIdActeur/{idCategorie}/{idActeur}")
        public List<Stock> getStocksByCategorieAndActeur(@PathVariable String idCategorie, @PathVariable String idActeur) {
            return stockService.getStocksByCategorieAndActeurIdacteur(idCategorie, idActeur);
        }

        // @GetMapping("/categorie/{idCategorie}")
        // public List<Stock> listeStockByCategorieProduit(@PathVariable String idCategorie) throws Exception {
        //     return stockService.listeStockByCategorieProduit(idCategorie);
        // }

        @GetMapping("/commande/{id}")
        public List<Stock> getStocksByCommande(@PathVariable String id) {
            return stockService.getAllStockByCommande(id);
        }

        @GetMapping("/categorieAndActeur/{idCategorie}/{idMagasin}/{idActeur}")
        public List<Stock> getStocksByCategorieAndMagasinAndActeur(@PathVariable String idCategorie, @PathVariable String idMagasin , @PathVariable String idActeur) throws Exception {
            return stockService.listeStockByCategorieProduitAndMagasinAndActeur(idCategorie, idMagasin,idActeur);
        }

        @DeleteMapping("/deleteStocks/{id}")
        @Operation(summary = "Suppression des stocks")
        public String supprimer(@PathVariable String id){
            return stockService.deleteStock(id);
        }
}