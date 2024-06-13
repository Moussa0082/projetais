package projet.ais.services;

import java.io.ByteArrayInputStream;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;



import javax.imageio.ImageIO;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.QRCode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
// import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Acteur;
import projet.ais.models.Alerte;
import projet.ais.models.CategorieProduit;
import projet.ais.models.Intrant;
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
    @Autowired
    FileUploade fileUploade;
    
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
                String imageLocation = "/ais";
                try {
                    Path imageRootLocation = Paths.get(imageLocation);
                    if (!Files.exists(imageRootLocation)) {
                        Files.createDirectories(imageRootLocation);
                    }
    
                    String imageName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                    Path imagePath = imageRootLocation.resolve(imageName);
                    Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                    String onlineImagePath =fileUploade.uploadImageToFTP(imagePath, imageName);

                    stock.setPhoto(imageName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }
            String codes = codeGenerator.genererCode();
            String idCode = idGenerator.genererCode();

            String qrCodeData = generateQRCodeData(stock);
            String qrCodeImageName = generateQRCodeImage(qrCodeData);
            stock.setPays(acteur.getNiveau3PaysActeur());

            stock.setIdStock(idCode);
            stock.setCodeStock(codes);

            
            String pattern = "yyyy-MM-dd HH:mm";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(formatter);

        stock.setDateAjout(formattedDateTime);
        stock.setDateProduction(formattedDateTime);
        Stock st = stockRepository.save(stock);
        
    //     if (st.getActeur().getTypeActeur() != null) {
    //     for (TypeActeur typeActeur : st.getActeur().getTypeActeur()) {
    //     if (typeActeur.getLibelle().equals("Producteur")) {
    //         System.out.println("Producteur mail: " + st.getActeur().getEmailActeur());
            
    //         // Récupérer tous les acteurs de type "Commerçant"
    //         List<Acteur> allCommercants = acteurRepository.findAllByTypeActeur_Libelle("Commerçant");
            
    //         // Envoyer un e-mail à chaque acteur commerçant
    //         for (Acteur commercant : allCommercants) {
    //             if (commercant != null) {
    //                 System.out.println("E-mail commerçant: " + commercant.getEmailActeur());
    //                 Alerte alerte = new Alerte(commercant.getEmailActeur(), "Nouveau produit ajouté", "Un nouveau produit a été ajouté");
    //                 emailService.sendSimpleMail(alerte);
    //             } else {
    //                 System.out.println("E-mail commerçant non trouvé");
    //             }
    //         }
    //         break; // Sortir de la boucle dès que "Producteur" est trouvé
    //     }
    // }
    // } else {
    //     System.out.println("Type d'acteur non trouvé");
    // }


    try {
        //  sendMessageToAllActeur(st);
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
    
        return st;
    }


    private String generateQRCodeData(Stock stock) {
        // Générer les données du QR code à partir des informations du stock
        // Vous pouvez personnaliser le contenu du QR code selon vos besoins
        // Par exemple, stock.getName(), stock.getId(), etc.
        return stock.getNomProduit() + "_" + stock.getIdStock();
    }

private String generateQRCodeImage(String qrCodeData) {
    // Générer l'image du QR code à partir des données fournies
    // Ici, vous pouvez utiliser une bibliothèque pour générer l'image du QR code
    // Retournez le nom de l'image générée
    // Assurez-vous de stocker cette image quelque part où elle peut être accessible publiquement
    // Par exemple, dans un dossier statique de votre application web
    // Assurez-vous également de manipuler les exceptions au besoin

    // Assumant que vous utilisez ZXing pour générer le QR code
    try {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, 250, 250);

        // Convertir la matrice de bits en image
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

        // Générer un nom unique pour l'image
        String imageName = UUID.randomUUID().toString() + ".png";

        // Enregistrer l'image sur le serveur
        Path imagePath = Paths.get("chemin/vers/dossier/static/qr_codes/" + imageName);
        Files.write(imagePath, outputStream.toByteArray());

        // Retourner le nom de l'image générée
        return imageName;
    } catch (Exception e) {
        // Manipuler les exceptions en fonction de vos besoins
        e.printStackTrace();
        return null;
    }

}

// public  BufferedImage generateQRCodeImage(String barcodeText) throws Exception {
//     QRCodeWriter barcodeWriter = new QRCodeWriter();
//     BitMatrix bitMatrix = 
//       barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);

//     return MatrixToImageWriter.toBufferedImage(bitMatrix);
// }


    // public Page<Stock> getAllStocksPageable(Pageable pageable) {
    //     return stockRepository.findAllByStatutSotckAndActeurStatutActeur(true, true,pageable);
    // }


       @Transactional
    public Page<Stock> getAllStockPageableByPaysByCategorie(CategorieProduit categorie, String niveau3PaysActeur, Pageable pageable) {
        // Fetch stock from the specified country
        Page<Stock> stocksByPays = stockRepository.findBySpeculation_CategorieProduitAndPaysAndStatutSotckAndActeurStatutActeur(
            categorie,  niveau3PaysActeur.trim().toLowerCase(), true, true,  pageable);

        List<Stock> stocksList = new ArrayList<>(stocksByPays.getContent());

        // Fetch stocks from other countries if needed
        if (stocksList.size() < pageable.getPageSize()) {
            Pageable complementPageable = PageRequest.of(0, pageable.getPageSize() - stocksList.size());
            Page<Stock> stocksComplement = stockRepository.findBySpeculation_CategorieProduitAndPaysNotAndStatutSotckAndActeurStatutActeur(
                categorie, niveau3PaysActeur.trim().toLowerCase(), true, true, complementPageable);
            stocksList.addAll(stocksComplement.getContent());
        }

        return new PageImpl<>(stocksList, pageable, stocksByPays.getTotalElements() + stocksList.size());
    }

       @Transactional
    public Page<Stock> getAllStockPageableByPaysByMagasinAndCategorie(String idCategorieProduit, String idMagasin, String niveau3PaysActeur, Pageable pageable) {
        // Fetch stock by cat , pays and store from the specified country
        Page<Stock> stocksByPays = stockRepository.findBySpeculation_CategorieProduit_IdCategorieProduit_AndMagasin_IdMagasinAndPaysAndStatutSotckAndActeurStatutActeur(
            idCategorieProduit, idMagasin, niveau3PaysActeur.trim().toLowerCase(), true, true,  pageable);

        List<Stock> stocksList = new ArrayList<>(stocksByPays.getContent());

        // Fetch stocks by  magasin et pays from other countries if needed
        if (stocksList.size() < pageable.getPageSize()) {
            Pageable complementPageable = PageRequest.of(0, pageable.getPageSize() - stocksList.size());
            Page<Stock> stocksComplement = stockRepository.findBySpeculation_CategorieProduit_IdCategorieProduit_AndMagasin_IdMagasinAndPaysNotAndStatutSotckAndActeurStatutActeur(
                idCategorieProduit, idMagasin, niveau3PaysActeur.trim().toLowerCase(), true, true, complementPageable);
            stocksList.addAll(stocksComplement.getContent());
        }

        return new PageImpl<>(stocksList, pageable, stocksByPays.getTotalElements() + stocksList.size());
    }

       @Transactional
    public Page<Stock> getAllStockPageableByPaysByMagasin(String idMagasin, String niveau3PaysActeur, Pageable pageable) {
        // Fetch stock by  pays and store from the specified country
        Page<Stock> stocksByPays = stockRepository.findByMagasin_IdMagasinAndPaysAndStatutSotckAndActeurStatutActeur(
     niveau3PaysActeur.trim().toLowerCase() , idMagasin, true, true,  pageable);

        List<Stock> stocksList = new ArrayList<>(stocksByPays.getContent());

        // Fetch stocks by magasin et pays from other countries if needed
        if (stocksList.size() < pageable.getPageSize()) {
            Pageable complementPageable = PageRequest.of(0, pageable.getPageSize() - stocksList.size());
            Page<Stock> stocksComplement = stockRepository.findByMagasin_IdMagasinAndPaysNotAndStatutSotckAndActeurStatutActeur(
                 niveau3PaysActeur.trim().toLowerCase(), idMagasin, true, true, complementPageable);
            stocksList.addAll(stocksComplement.getContent());
        }

        return new PageImpl<>(stocksList, pageable, stocksByPays.getTotalElements() + stocksList.size());
    }

    
    public Page<Stock> getAllStocksPageableByPays(String niveau3PaysActeur, Pageable pageable) {
        Page<Stock> stocksByPays = stockRepository.findAllByStatutSotckTrueAndPaysAndActeurStatutActeurTrue(niveau3PaysActeur.trim().toLowerCase(), pageable);
        
        if (!stocksByPays.hasContent()) {
            System.out.println("Pas d'autres stock à fetch pour le pays " + niveau3PaysActeur);
            return stockRepository.findAllByStatutSotckAndActeurStatutActeur(true, true, pageable);
        } else {
            System.out.println("stock fetch pour le pays " + niveau3PaysActeur);
            List<Stock> stocksList = new ArrayList<>(stocksByPays.getContent());

            // Si le nombre de stocks est inférieur au nombre requis, compléter avec des stocks d'autres pays
            if (stocksList.size() < pageable.getPageSize()) {
                Pageable complementPageable = PageRequest.of(0, pageable.getPageSize() - stocksList.size());
                Page<Stock> stocksComplement = stockRepository.findAllByStatutSotckTrueAndActeurStatutActeurTrueAndPaysNot(niveau3PaysActeur.trim().toLowerCase(), complementPageable);
                stocksList.addAll(stocksComplement.getContent());
            }

            return new PageImpl<>(stocksList, pageable, stocksByPays.getTotalElements() + stocksList.size());
        }
    }

    @Transactional
    public void updatePaysForStocks() {
        // Récupérer tous les stocks
        List<Stock> stocks = stockRepository.findAll();

        // Parcourir chaque stock
        for (Stock stock : stocks) {
            // Récupérer l'acteur lié au stock
            Acteur acteur = stock.getActeur();

            if (acteur != null) {
                // Récupérer le niveau3Pays de l'acteur lié au stock
                String niveau3Pays = acteur.getNiveau3PaysActeur();

                // Mettre à jour la colonne pays du stock
                stock.setPays(niveau3Pays);
            } else {
                // Gérer le cas où l'acteur est null
                System.out.println("L'acteur lié au stock ID " + stock.getIdStock() + " est null.");
            }
        }

        // Sauvegarder les modifications
        stockRepository.saveAll(stocks);
    }

    public Page<Stock> getAllStocksPageable(Pageable pageable) {
        return stockRepository.findAllByStatutSotckAndActeurStatutActeur(true, true,pageable);
    }

    
    public ResponseEntity<String> sendMessageToAllActeur(Stock stock) {
        List<Acteur> allActeurs = acteurRepository.findAll();
        Acteur ac = stock.getActeur();

        // TypeActeur transporteur = typeActeurRepository.findByLibelle("Transporteur");
        // TypeActeur fournisseur = typeActeurRepository.findByLibelle("Fournisseur");
        for (Acteur acteur : allActeurs) {
            // if (!acteur.getIdActeur().equals(ac.getIdActeur())  && !acteur.getTypeActeur().contains(transporteur) && !acteur.getTypeActeur().contains(fournisseur)) {
              if(ac != acteur){

                  // Envoyer le message uniquement aux autres acteurs, pas à celui qui a ajouté le stock et pas aux transporteurs
                  String mes = "Bonjour M. " + acteur.getNomActeur() + " M. " +  ac.getNomActeur() + " habitant à " + ac.getAdresseActeur() + " vient d'ajouter un produit au stock: " 
                      + stock.getNomProduit() + "\n\n Lien vers le produit est : " + "https://koumi.ml/api-koumi/Stock/"+stock.getIdStock()+"/image";
                      try {
                          messageService.sendMessageAndSave(acteur.getWhatsAppActeur(), mes,  acteur);
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
                + stock.getNomProduit() + "\n \n Lien vers le produit est : " + "https://koumi.ml/api-koumi/Stock/"+stock.getIdStock()+"/image";
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
        stocks.setOrigineProduit(stock.getOrigineProduit());
        stocks.setPrix(stock.getPrix());
        stocks.setDateProduction(stock.getDateProduction());
        stocks.setQuantiteStock(stock.getQuantiteStock());
        stocks.setDescriptionStock(stock.getDescriptionStock());
        stocks.setTypeProduit(stock.getTypeProduit());
        stocks.setPersonneModif(stock.getPersonneModif());       

        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);

        stocks.setDateModif(formattedDateTime);

        
        if(stock.getMonnaie() != null){
            stocks.setMonnaie(stock.getMonnaie());
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
        
          stocks.setUnite(stock.getUnite());
        
        if (imageFile != null) {
            String imageLocation = "/ais";
            try {
                Path imageRootLocation = Paths.get(imageLocation);
                if (!Files.exists(imageRootLocation)) {
                    Files.createDirectories(imageRootLocation);
                }

                String imageName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Path imagePath = imageRootLocation.resolve(imageName);
                Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                String onlineImagePath =fileUploade.uploadImageToFTP(imagePath, imageName);

                stock.setPhoto(imageName);
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

     public List<Stock> getLastTenStocks() {
    // Création d'un objet Pageable pour récupérer les 10 premiers éléments, triés par date d'ajout décroissante
    Pageable pageable = PageRequest.of(0, 10, Sort.by("dateAjout").descending());
    
    // Récupération des stocks à partir de la page
    Page<Stock> stockPage = stockRepository.findAll(pageable);

    // Vérification si la page est vide
    if (stockPage.isEmpty()) {
        throw new IllegalStateException("Aucun stock trouvé");
    }

    // Retourne les stocks de la page
    return stockPage.getContent();
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

    //liste des stock par libelle categorie
    // public List<Stock> getAllStockByLibelleCategorie(String libelle){
    //     List<Stock> stockList = stockRepository.findBySpeculation_CategorieProduit_libelleCategorie(libelle);

    //     if(stockList.isEmpty())
    //         throw new IllegalStateException("Aucun stock trouvé");
        
    //         stockList = stockList
    //          .stream().sorted((s1,s2) -> s2.getDescriptionStock().compareTo(s1.getDescriptionStock()))
    //     .collect(Collectors.toList());

    //     return stockList;
    // }

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

    // recuperer les stock par  magasin avec pagination
    public Page<Stock> getStocksByMagasinWithPagination(String idMagasin,Pageable pageable) {
        return stockRepository.findByMagasin_IdMagasinAndStatutSotckAndActeurStatutActeur(idMagasin,true, true,pageable);
    }

     // recuperer les intrants par  libelle categorie
    public Page<Stock> getAllStockByLibelleCategorie(String libelle,Pageable pageable) {
        return stockRepository.findBySpeculation_CategorieProduit_filiere_libelleFiliere(libelle, pageable);
    }
    
    // recuperer les stock par  acteur avec pagination
    public Page<Stock> getStocksByActeurWithPagination(String idActeur,Pageable pageable) {
        return stockRepository.findByActeur_IdActeur(idActeur, pageable);
    }

    // recuperer les stock par  acteur avec pagination
    public Page<Stock> getStocksByMagasinAndActeurWithPagination(String idMagasin,String idActeur,Pageable pageable) {
        return stockRepository.findByMagasin_IdMagasinAndActeur_IdActeur(idMagasin,idActeur, pageable);
    }


    
    //Avec pagination stock par magasin , acteur  et categorie 
    public Page<Stock> listeStockByCategorieProduitAndMagasinWithPagination(String idCategorieProduit, String idMagasin, Pageable pageable) {
        return stockRepository.findBySpeculation_CategorieProduit_IdCategorieProduit_AndMagasin_IdMagasinAndStatutSotckAndActeurStatutActeur(idCategorieProduit,idMagasin, true, true,pageable);
    }

    //recuperer les stock par categorie produit et idActeur
    public List<Stock> getStocksByCategorieAndActeurIdacteur(String idCategorieProduit, String idActeur) {
        return stockRepository.findBySpeculation_CategorieProduit_IdCategorieProduitAndActeur_IdActeur(idCategorieProduit, idActeur);
    }

      // Récupérer les stocks par catégorie
    public List<Stock> getStocksByCategorie(CategorieProduit categorie) {
        return stockRepository.findBySpeculation_CategorieProduit(categorie);
    }


    public Page<Stock> getStocksByCategorieWithPagination(CategorieProduit categorie, Pageable pageable) {
        return stockRepository.findBySpeculation_CategorieProduitAndStatutSotckAndActeurStatutActeur(categorie,true, true, pageable);
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

    // public List<Stock> listeStockByCategorieProduit( String idCategorie) throws Exception {

    //     List<Stock> stockList = stockRepository.findBySpeculation_CategorieProduit_IdCategorieProduit(idCategorie);

    //     if(stockList.isEmpty())
    //         throw new IllegalStateException("Aucun stock trouvé");
        
    //         stockList = stockList
    //         .stream().sorted((s1,s2) -> s2.getDescriptionStock().compareTo(s1.getDescriptionStock()))

    //     .collect(Collectors.toList());

    //     return stockList;

    // }


// public List<Stock> getAllStockBySpeculation(String id){
//     List<Stock> stockList = stockRepository.findBySpeculationIdSpeculation(id);

//     if(stockList.isEmpty())
//         throw new IllegalStateException("Aucun stock trouvé");
    
//         stockList = stockList
//         .stream().sorted((s1,s2) -> s2.getDescriptionStock().compareTo(s1.getDescriptionStock()))

//     .collect(Collectors.toList());

//     return stockList;
// }



    public List<Stock> getAllStockByMagasin(String id){
        List<Stock> stockList = stockRepository.findByMagasinIdMagasin(id);

        if(stockList.isEmpty())
            throw new IllegalStateException("Aucun stock trouvé");
        
            stockList = stockList
            .stream().sorted((s1,s2) -> s2.getDescriptionStock().compareTo(s1.getDescriptionStock()))
        .collect(Collectors.toList());

        return stockList;
    }

    public List<Stock> getAllStockByCommande(String id){
        List<Stock> stockList = stockRepository.findByCommande_IdCommande(id);

        if(stockList.isEmpty())
            throw new IllegalStateException("Aucun stock trouvé");
        
            stockList = stockList
            .stream().sorted((s1,s2) -> s2.getNomProduit().compareTo(s1.getNomProduit()))
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
            stock.setStatutSotck(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation : " + e.getMessage());
        }
        return stockRepository.save(stock);
    }
}