package projet.ais.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import projet.ais.CodeGenerator;
import projet.ais.models.Acteur;
import projet.ais.models.Magasin;
import projet.ais.models.Speculation;
import projet.ais.models.Stock;
import projet.ais.models.Unite;
import projet.ais.models.ZoneProduction;
import projet.ais.repository.ActeurRepository;
import projet.ais.repository.MagasinRepository;
import projet.ais.repository.SpeculationRepository;
import projet.ais.repository.StockRepository;
import projet.ais.repository.UniteRepository;
import projet.ais.repository.ZoneProductionRepository;

@Service
public class StockService {
    
    @Autowired
    StockRepository stockRepository;
    @Autowired
    UniteRepository uniteRepository;
    @Autowired
    ActeurRepository acteurRepository;
    @Autowired
    MagasinRepository magasinRepository;
    @Autowired
    SpeculationRepository speculationRepository;
    @Autowired
    ZoneProductionRepository zoneProductionRepository;
    @Autowired
    CodeGenerator codeGenerator;

    public Stock createStock(Stock stock, MultipartFile imageFile) throws Exception {
        Unite unite = uniteRepository.findByIdUnite(stock.getUnite().getIdUnite());
        Magasin magasin = magasinRepository.findByIdMagasin(stock.getMagasin().getIdMagasin());
        Acteur acteur = acteurRepository.findByIdActeur(stock.getActeur().getIdActeur());
        Speculation speculation = speculationRepository.findByIdSpeculation(stock.getSpeculation().getIdSpeculation());
        ZoneProduction zoneProduction = zoneProductionRepository.findByidZoneProduction(stock.getZoneProduction().getIdZoneProduction());
        
        if(zoneProduction == null)
            throw new IllegalStateException("Aucune zone production");
        if(speculation == null)
            throw new IllegalStateException("Aucune speculations ");
        if(unite == null)
            throw new IllegalStateException("Aucune unité trouvé");
        if(magasin == null)
            throw new IllegalStateException("Aucun magasin trouvé");
        if(acteur == null)
            throw new IllegalStateException("Aucun acteur trouvé");

            if (imageFile != null) {
                String imageLocation = "C:\\xampp\\htdocs\\ais";
                try {
                    Path imageRootLocation = Paths.get(imageLocation);
                    if (!Files.exists(imageRootLocation)) {
                        Files.createDirectories(imageRootLocation);
                    }
    
                    String imageName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                    Path imagePath = imageRootLocation.resolve(imageName);
                    Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                    stock.setPhoto("ais/" + imageName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }
            String codes = codeGenerator.genererCode();
            stock.setCodeStock(codes);
              Date dates = new Date();
        Instant instant = dates.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        stock.setDateModif(dates);
        stock.setDateAjout(dates);
            return stockRepository.save(stock);
    }

    public Stock updateStock(Stock stock, MultipartFile imageFile,Integer id) throws Exception {
        Stock stocks = stockRepository.findById(id).orElseThrow(null);

        stocks.setNomProduit(stock.getNomProduit());
        stocks.setFormeProduit(stock.getFormeProduit());
        stocks.setDateProduction(stock.getDateProduction());
        stocks.setQuantiteStock(stock.getQuantiteStock());
        stocks.setDescriptionStock(stock.getDescriptionStock());
        stocks.setDateAjout(stocks.getDateAjout());
        
        Date dates = new Date();
        Instant instant = dates.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        stocks.setDateModif(dates);

        if(stock.getUnite() != null){
            stocks.setUnite(stock.getUnite());
        }
         
        if(stock.getMagasin() != null){
            stocks.setMagasin(stock.getMagasin());
        }
        
        if(stock.getZoneProduction() != null){
            stocks.setZoneProduction(stock.getZoneProduction());
        }
            
        if(stock.getSpeculation() != null){
            stocks.setSpeculation(stock.getSpeculation());
        }
        

            if (imageFile != null) {
                String imageLocation = "C:\\xampp\\htdocs\\ais";
                try {
                    Path imageRootLocation = Paths.get(imageLocation);
                    if (!Files.exists(imageRootLocation)) {
                        Files.createDirectories(imageRootLocation);
                    }
    
                    String imageName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                    Path imagePath = imageRootLocation.resolve(imageName);
                    Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                    stocks.setPhoto("ais/" + imageName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }
           
            return stockRepository.save(stocks);
    }


    public Stock updateQuantiteStock(Stock stock,Integer id) throws Exception {
        Stock stocks = stockRepository.findById(id).orElseThrow(null);

         
        double ancienQuantity = stocks.getQuantiteStock();
        double  newQuantity = ancienQuantity + stock.getQuantiteStock();
        stocks.setQuantiteStock(newQuantity);

        stocks.setDateAjout(stocks.getDateAjout());
        
        Date dates = new Date();
        Instant instant = dates.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        stocks.setDateModif(dates);

        if(stock.getUnite() != null){
            stocks.setUnite(stock.getUnite());
        }
         
        if(stock.getMagasin() != null){
            stocks.setMagasin(stock.getMagasin());
        }
        
        if(stock.getZoneProduction() != null){
            stocks.setZoneProduction(stock.getZoneProduction());
        }
            
        if(stock.getSpeculation() != null){
            stocks.setSpeculation(stock.getSpeculation());
        }
        

           
           
            return stockRepository.save(stocks);
    }


    
    public List<Stock> getAllStock(){
        List<Stock> stockList = stockRepository.findAll();

        if(stockList.isEmpty())
            throw new IllegalStateException("Aucun stock trouvé");
        
            stockList = stockList
             .stream().sorted((s1,s2) -> s2.getDescriptionStock().compareTo(s1.getDescriptionStock()))
        .collect(Collectors.toList());

        return stockList;
    }

    public List<Stock> getAllStockByActeur(Integer id){
        List<Stock> stockList = stockRepository.findByActeurIdActeur(id);

        if(stockList.isEmpty())
            throw new IllegalStateException("Aucun stock trouvé");
        
            stockList = stockList
             .stream().sorted((s1,s2) -> s2.getDescriptionStock().compareTo(s1.getDescriptionStock()))
        .collect(Collectors.toList());

        return stockList;
    }

    public List<Stock> getAllStockByMagasin(Integer id){
        List<Stock> stockList = stockRepository.findByMagasinIdMagasin(id);

        if(stockList.isEmpty())
            throw new IllegalStateException("Aucun stock trouvé");
        
            stockList = stockList
            .stream().sorted((s1,s2) -> s2.getDescriptionStock().compareTo(s1.getDescriptionStock()))
        .collect(Collectors.toList());

        return stockList;
    }

    public String deleteStock(Integer id){
        Stock stock = stockRepository.findById(id).orElseThrow(null);

        stockRepository.delete(stock);

        return "Supprimé avec success";
    }
}
