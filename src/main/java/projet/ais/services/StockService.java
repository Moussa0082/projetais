package projet.ais.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Acteur;
import projet.ais.models.Alerte;
import projet.ais.models.CategorieProduit;
import projet.ais.models.Magasin;
import projet.ais.models.Speculation;
import projet.ais.models.Stock;
import projet.ais.models.TypeActeur;
import projet.ais.models.Unite;
import projet.ais.models.ZoneProduction;
import java.time.format.DateTimeFormatter;

import projet.ais.repository.ActeurRepository;
import projet.ais.repository.AlerteRepository;
import projet.ais.repository.MagasinRepository;
import projet.ais.repository.SpeculationRepository;
import projet.ais.repository.StockRepository;
import projet.ais.repository.TypeActeurRepository;
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
    private EmailService emailService;

    @Autowired
    private AlerteRepository alerteRepository;

    @Autowired
    ZoneProductionRepository zoneProductionRepository;

    @Autowired
    TypeActeurRepository typeActeurRepository;
    
    @Autowired
    CodeGenerator codeGenerator;

    @Autowired
    IdGenerator idGenerator ;
    @Autowired
    MessageService messageService;
    
    public Stock createStock(Stock stock, MultipartFile imageFile) throws Exception {
        Unite unite = uniteRepository.findByIdUnite(stock.getUnite().getIdUnite());
        Magasin magasin = magasinRepository.findByIdMagasin(stock.getMagasin().getIdMagasin());
        Acteur acteur = acteurRepository.findByIdActeur(stock.getActeur().getIdActeur());
        Speculation speculation = speculationRepository.findByIdSpeculation(stock.getSpeculation().getIdSpeculation());
        ZoneProduction zoneProduction = zoneProductionRepository.findByidZoneProduction(stock.getZoneProduction().getIdZoneProduction());
        
        if(zoneProduction == null)
            throw new IllegalStateException("Aucune zone production trouvé");
        if(speculation == null)
            throw new IllegalStateException("Aucune speculation trouvé ");
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
            String idCode = idGenerator.genererCode();

            stock.setCodeStock(codes);
            stock.setIdStock(idCode);
             
            
            String pattern = "yyyy-MM-dd HH:mm";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(formatter);

        stock.setDateAjout(formattedDateTime);
        stock.setDateProduction(formattedDateTime);
        Stock st = stockRepository.save(stock);
        
        if (st.getActeur().getTypeActeur() != null) {
        for (TypeActeur typeActeur : st.getActeur().getTypeActeur()) {
        if (typeActeur.getLibelle().equals("Producteur")) {
            System.out.println("Producteur mail: " + st.getActeur().getEmailActeur());
            
            // Récupérer tous les acteurs de type "Commerçant"
            List<Acteur> allCommercants = acteurRepository.findAllByTypeActeur_Libelle("Commerçant");
            
            // Envoyer un e-mail à chaque acteur commerçant
            for (Acteur commercant : allCommercants) {
                if (commercant != null) {
                    System.out.println("E-mail commerçant: " + commercant.getEmailActeur());
                    Alerte alerte = new Alerte(commercant.getEmailActeur(), "Nouveau produit ajouté", "Un nouveau produit a été ajouté");
                    emailService.sendSimpleMail(alerte);
                } else {
                    System.out.println("E-mail commerçant non trouvé");
                }
            }
            break; // Sortir de la boucle dès que "Producteur" est trouvé
        }
    }
    } else {
        System.out.println("Type d'acteur non trouvé");
    }


    try {
        // sendMessageToAllActeur(st);
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
    
        return st;
    }

    
    public ResponseEntity<String> sendMessageToAllActeur(Stock stock) {
        List<Acteur> allActeurs = acteurRepository.findAll();
        Acteur ac = stock.getActeur();

        TypeActeur transporteur = typeActeurRepository.findByLibelle("Transporteur");
        TypeActeur fournisseur = typeActeurRepository.findByLibelle("Fournisseur");
        for (Acteur acteur : allActeurs) {
            if (!acteur.getIdActeur().equals(ac.getIdActeur())  && !acteur.getTypeActeur().contains(transporteur) && !acteur.getTypeActeur().contains(fournisseur)) {
            
            // Envoyer le message uniquement aux autres acteurs, pas à celui qui a ajouté le stock et pas aux transporteurs
            String mes = "Bonjour M. " + acteur.getNomActeur() + " M. " +  ac.getNomActeur() + " habitant à " + ac.getAdresseActeur() + " vient d'ajouter un produit au stock: " 
                + stock.getNomProduit() + "\n\n Lien vers le produit est : " + stock.getPhoto();
                try {
                    messageService.sendMessageAndSave(acteur.getWhatsAppActeur(), mes,  ac);
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());
                }
            }
        
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    public ResponseEntity<String> sendEmailToAllActeur(Stock stock) {
        List<Acteur> allActeurs = acteurRepository.findAll();
        Acteur ac = stock.getActeur();

        TypeActeur transporteur = typeActeurRepository.findByLibelle("Transporteur");
        TypeActeur fournisseur = typeActeurRepository.findByLibelle("Fournisseur");
        for (Acteur acteur : allActeurs) {
            if (!acteur.getIdActeur().equals(ac.getIdActeur())  && !acteur.getTypeActeur().contains(transporteur) && !acteur.getTypeActeur().contains(fournisseur)) {
            
            // Envoyer le message uniquement aux autres acteurs, pas à celui qui a ajouté le stock et pas aux transporteurs
            String mes = "Bonjour M. " + acteur.getNomActeur() + " M. " +  ac.getNomActeur() + " habitant à " + ac.getAdresseActeur() + " vient d'ajouter un produit au stock: " 
                + stock.getNomProduit() + "\n\n Lien vers le produit est : " + stock.getPhoto();
                try {
                    Alerte alerte = new Alerte(acteur.getEmailActeur(), mes, "Nouveau produit");
                    emailService.sendSimpleMail(alerte);
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());
                }
            }
        
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    // public ResponseEntity<String> sendMessageToAllActeur(Stock stock) {
    //     List<Acteur> allActeurs = acteurRepository.findAll();
    //     Acteur ac = stock.getActeur();
    //     // Envoi de message à tous les autres acteurs à l'exception de l'acteur courant
    //     for (Acteur acteur : allActeurs) {
    //         if (!acteur.getIdActeur().equals(ac.getIdActeur())) {
    //             // Envoyer le message uniquement aux autres acteurs, pas à celui qui a ajouté le stock
    //             String mes = "Bonjour M. " + acteur.getNomActeur() + " M. " +  ac.getNomActeur() + " habitant à " + ac.getAdresseActeur() + " vient d'ajouter un produit au stock: " 
    //             + stock.getNomProduit() + "\n\n Lien vers le produit est : " + stock.getPhoto();
    //             try {
    //                 messageService.sendMessageAndSave(acteur.getWhatsAppActeur(), mes,  ac.getNomActeur());
    //             } catch (Exception e) {
    //                 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());
    //             }
    //         }
        
    //     }
    //     return new ResponseEntity<>(HttpStatus.ACCEPTED);
    // }
    

    public Stock updateStock(Stock stock, MultipartFile imageFile,String id) throws Exception {
        Stock stocks = stockRepository.findById(id).orElseThrow(null);

        stocks.setNomProduit(stock.getNomProduit());
        stocks.setFormeProduit(stock.getFormeProduit());
        stocks.setDateProduction(stock.getDateProduction());
        stocks.setQuantiteStock(stock.getQuantiteStock());
        stocks.setDescriptionStock(stock.getDescriptionStock());
        stocks.setPersonneModif(stock.getPersonneModif());       

        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);

        stocks.setDateModif(formattedDateTime);

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


    public Stock updateQuantiteStock(Stock stock,String id) throws Exception {
        Stock stocks = stockRepository.findById(id).orElseThrow(null);

         
        double ancienQuantity = stocks.getQuantiteStock();
        double  newQuantity = ancienQuantity + stock.getQuantiteStock();
        stocks.setQuantiteStock(newQuantity);
         return stockRepository.save(stocks);
    }


    
    public List<Stock> getAllStock(){
        List<Stock> stockList = stockRepository.findAll();

        if(stockList.isEmpty())
            throw new IllegalStateException("Aucun stock trouvé");
        
            stockList = stockList
             .stream().sorted((s1,s2) -> s2.getDescriptionStock().compareTo(s1.getDescriptionStock()))
        .collect(Collectors.toList());
        //  System.out.println("service : "+stockList);

        return stockList;
    }

    public List<Stock> getAllStockByActeur(String id){
        List<Stock> stockList = stockRepository.findByActeurIdActeur(id);

        if(stockList.isEmpty())
            throw new IllegalStateException("Aucun stock trouvé");
        
            stockList = stockList
             .stream().sorted((s1,s2) -> s2.getDescriptionStock().compareTo(s1.getDescriptionStock()))
        .collect(Collectors.toList());

        return stockList;
    }

     
    //recuperer les stock par categorie produit et magasin
    public List<Stock> getStocksByCategorieAndMagasin(String idCategorieProduit, String idMagasin) {
        return stockRepository.findBySpeculation_CategorieProduit_IdCategorieProduitAndMagasin_IdMagasin(idCategorieProduit, idMagasin);
    }

      // Récupérer les stocks par catégorie
    public List<Stock> getStocksByCategorie(CategorieProduit categorie) {
        return stockRepository.findBySpeculation_CategorieProduit(categorie);
    }

    public List<Stock> listeStockByCategorieProduitAndMagasinAndActeur( String idCategorie, String idMagasin ,String idActeur) throws Exception {

        List<Stock> stockList = stockRepository.findBySpeculation_CategorieProduit_IdCategorieProduitAndMagasin_IdMagasinAndActeurIdActeur(idCategorie,idMagasin,idActeur);
   
        if(stockList.isEmpty())
            throw new IllegalStateException("Aucun stock trouvé");
        
            stockList = stockList
            .stream().sorted((s1,s2) -> s2.getDescriptionStock().compareTo(s1.getDescriptionStock()))

        .collect(Collectors.toList());

        return stockList;

    }

public List<Stock> getAllStockBySpeculation(String id){
    List<Stock> stockList = stockRepository.findBySpeculationIdSpeculation(id);

    if(stockList.isEmpty())
        throw new IllegalStateException("Aucun stock trouvé");
    
        stockList = stockList
        .stream().sorted((s1,s2) -> s2.getDescriptionStock().compareTo(s1.getDescriptionStock()))

    .collect(Collectors.toList());

    return stockList;
}



    public List<Stock> getAllStockByMagasin(String id){
        List<Stock> stockList = stockRepository.findByMagasinIdMagasin(id);

        if(stockList.isEmpty())
            throw new IllegalStateException("Aucun stock trouvé");
        
            stockList = stockList
            .stream().sorted((s1,s2) -> s2.getDescriptionStock().compareTo(s1.getDescriptionStock()))
        .collect(Collectors.toList());

        return stockList;
    }

    public String deleteStock(String id){
        Stock stock = stockRepository.findById(id).orElseThrow(null);

        stockRepository.delete(stock);

        return "Supprimé avec success";
    }

    public Stock active(String id) throws Exception{
        Stock stock = stockRepository.findById(id).orElseThrow(null);

        try {
            stock.setStatutSotck(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation : " + e.getMessage());
        }
        return stockRepository.save(stock);
    }

    public Stock desactive(String id) throws Exception{
        Stock stock = stockRepository.findById(id).orElseThrow(null);

        try {
            stock.setStatutSotck(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation : " + e.getMessage());
        }
        return stockRepository.save(stock);
    }
}
