package projet.ais.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Abonnement;
import projet.ais.models.Acteur;
import projet.ais.models.Alerte;
import projet.ais.models.CategorieProduit;
import projet.ais.models.Magasin;
import projet.ais.models.Speculation;
import projet.ais.models.Stock;
import projet.ais.models.TypeActeur;
import projet.ais.models.Unite;
import projet.ais.models.ZoneProduction;
import projet.ais.repository.AbonnementRepository;
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
    @Autowired
    AbonnementRepository aRepository;
    @Autowired
    HistoriqueService historiqueService;

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

         // Mettre à jour hasAssociation si ce n'est pas déjà fait
        if (!acteur.isHasAssociation()) {
            acteur.setHasAssociation(true);
            acteurRepository.save(acteur);
        }

        if (!magasin.isHasAssociation()) {
            magasin.setHasAssociation(true);
            magasinRepository.save(magasin);
        }

        if (!unite.isHasAssociation()) {
            unite.setHasAssociation(true);
            uniteRepository.save(unite);
        }

        if (!speculation.isHasAssociation()) {
            speculation.setHasAssociation(true);
            speculationRepository.save(speculation);
        }

        if (!zoneProduction.isHasAssociation()) {
            zoneProduction.setHasAssociation(true);
            zoneProductionRepository.save(zoneProduction);
        }

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

            // String qrCodeData = generateQRCodeData(stock);
            // String qrCodeImageName = generateQRCodeImage(qrCodeData);
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

    try {
        System.out.println("----------Methode Envoie-----------------");
        sendMessageToAllActeurWithAbonner(st);
    } catch (Exception e) {
        System.out.println("Erreur lors de l'envoie du message abonnement " + e.getMessage());
    }
     // Création de l'historique
     historiqueService.createHistorique("Création" , st.getNomProduit() ,st.getActeur().getNomActeur(), st.getActeur().getLocaliteActeur(),st.getActeur().getNiveau3PaysActeur(),"Création de produit " + st.getNomProduit());
        return st;
    }



    private String generateQRCodeData(Stock stock) {
        return stock.getNomProduit() + "_" + stock.getIdStock();
    }

private String generateQRCodeImage(String qrCodeData) {
   
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

    @Transactional
    public Page<Stock> getAllStockPageableByPaysByCategorie(CategorieProduit categorie, String niveau3PaysActeur, Pageable pageable) {
        // Fetch stock from the specified country
        Page<Stock> stocksByPays = stockRepository.findBySpeculation_CategorieProduitAndPaysAndStatutSotckAndActeurStatutActeurAndQuantiteStockGreaterThan(
            categorie, niveau3PaysActeur.trim().toLowerCase(), true, true, pageable,0.0);

        List<Stock> stocksList = new ArrayList<>(stocksByPays.getContent());

        // If no stocks are found for the specified country, fetch stocks from other countries
        if (stocksList.isEmpty()) {
            Page<Stock> stocksFromOtherCountries = stockRepository.findBySpeculation_CategorieProduitAndStatutSotckAndActeurStatutActeurAndQuantiteStockGreaterThan(
                categorie, true, true, pageable,0.0);

            return new PageImpl<>(stocksFromOtherCountries.getContent(), pageable, stocksFromOtherCountries.getTotalElements());
        }

        // Fetch stocks from other countries if needed to fill the page
        if (stocksList.size() < pageable.getPageSize()) {
            Pageable complementPageable = PageRequest.of(0, pageable.getPageSize() - stocksList.size());
            Page<Stock> stocksComplement = stockRepository.findBySpeculation_CategorieProduitAndPaysNotAndStatutSotckAndActeurStatutActeurAndQuantiteStockGreaterThan(
                categorie, niveau3PaysActeur.trim().toLowerCase(), true, true, complementPageable,0.0);
            stocksList.addAll(stocksComplement.getContent());
        }

        return new PageImpl<>(stocksList, pageable, stocksByPays.getTotalElements() + stocksList.size());
    }

////New methode
    public Page<Stock> getAllStockPageableByPaysAndStatut(String niveau3PaysActeur, Pageable pageable) {
        Page<Stock> stockByPays = stockRepository.findAllByStatutSotckTrueAndPaysAndActeurStatutActeurTrueAndQuantiteStockGreaterThan(niveau3PaysActeur.trim().toLowerCase(), pageable,0.0);
        
        if (!stockByPays.hasContent()) {
            System.out.println("Pas d'autres stock à fetch pour le pays " + niveau3PaysActeur);
            return stockRepository.findAllByStatutSotckAndActeurStatutActeurAndQuantiteStockGreaterThan(true, true, pageable,0.0);
        } else {
            System.out.println("stock fetch pour le pays " + niveau3PaysActeur);
            List<Stock> stocksList = new ArrayList<>(stockByPays.getContent());

            // Si le nombre d'intrants est inférieur au nombre requis, compléter avec des intrants d'autres pays
            if (stocksList.size() < pageable.getPageSize()) {
                Pageable complementPageable = PageRequest.of(0, pageable.getPageSize() - stocksList.size());
                Page<Stock> stockComplement = stockRepository.findAllByStatutSotckTrueAndActeurStatutActeurTrueAndPaysNotAndQuantiteStockGreaterThan(niveau3PaysActeur.trim().toLowerCase(), complementPageable,0.0);
                stocksList.addAll(stockComplement.getContent());
            }

            return new PageImpl<>(stocksList, pageable, stockByPays.getTotalElements() + stocksList.size());
        }
    }

    public Page<Stock> getAllStockPageableByPaysByMagasinAndCategorie(String idCategorieProduit, String idMagasin, Pageable pageable) {
        return stockRepository.findBySpeculation_CategorieProduit_IdCategorieProduit_AndMagasin_IdMagasinAndStatutSotckAndQuantiteStockGreaterThan(
                    idCategorieProduit, idMagasin, true, pageable,0.0);
    }

    public Page<Stock> getAllStockPageableByPaysByMagasin(String idMagasin, Pageable pageable) {
        return stockRepository.findByMagasin_IdMagasinAndStatutSotckAndQuantiteStockGreaterThan(
                    idMagasin, true, pageable,0.0);
    }

    public Page<Stock> getAllStockByActeurAndCategorie(String idCategorie, String idActeur, Pageable pageable) {
        return stockRepository.findBySpeculation_CategorieProduit_IdCategorieProduit_AndActeur_IdActeurAndQuantiteStockGreaterThan(
                    idCategorie,idActeur, pageable,0.0);
    }


    //test get all 
    public Page<Stock> getAllStocksPageableByPays(Pageable pageable) {
        return stockRepository.findAllByStatutSotckTrueAndActeurStatutActeurTrueAndQuantiteStockGreaterThan(pageable,0.0);
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
        return stockRepository.findAllByStatutSotckAndActeurStatutActeurAndQuantiteStockGreaterThan(true, true,pageable,0.0);
    }

    public Page<Stock> getAllStocksPageableAndStatut(Pageable pageable) {
        return stockRepository.findAllByStatutSotckAndActeurStatutActeurAndQuantiteStockGreaterThan(true, true,pageable,0.0);
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
                      + stock.getNomProduit() + "\n\n Lien vers le produit est : " + "http://api.koumi.ml/Stock/"+stock.getIdStock()+"/image";
                      try {
                          messageService.sendMessageAndSave(acteur.getWhatsAppActeur(), mes,  acteur);
                      } catch (Exception e) {
                          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());
                      }
                  
              }
        
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
   
    public ResponseEntity<String> sendMessageToAllActeurWithAbonner(Stock stock) {
        System.out.println("Debut de l'envoie");
        Acteur ac = stock.getActeur();
        Abonnement ab = aRepository.findTopByActeurIdActeurOrderByDateAjoutDesc(ac.getIdActeur());
    
        // Vérifiez si l'abonnement est actif
        if (ab != null && Boolean.TRUE.equals(ab.getStatutAbonnement())) {
            List<String> optionsList = ab.getOptions();
    
            // Pour chaque option dans l'abonnement
            for (String option : optionsList) {
                System.out.println("Recuperation et envoie");
                // Récupérer les acteurs par type
                List<Acteur> allActeurs = acteurRepository.findByTypeActeur_Libelle(option);
    
                // Filtrer les acteurs à notifier
                allActeurs.stream()
                    .filter(acteur -> !acteur.getIdActeur().equals(ac.getIdActeur()))
                    .forEach(acteur -> sendNotification(acteur, ac, stock)); // Envoyer la notification
            }
        }
    
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    
    private void sendNotification(Acteur acteur, Acteur ac, Stock stock) {
        // Envoyer le message uniquement aux autres acteurs, pas à celui qui a ajouté le stock et pas aux transporteurs
         // Extraire les détails nécessaires du stock
    String nomProduit = stock.getNomProduit();
    double quantiteStock = stock.getQuantiteStock();
    String uniteMesure = stock.getUnite().getNomUnite(); // Exemple pour extraire l'unité
    int prix = stock.getPrix();
    String zoneProduction = stock.getZoneProduction().getNomZoneProduction();
    String pays = ac.getNiveau3PaysActeur();
    String contact = ac.getWhatsAppActeur();
    
    // Lien vers l'image ou la page du stock
    String lienProduit = "http://api.koumi.ml/Stock/" + stock.getIdStock() + "/image";
    
    // Message de notification à envoyer
    String message = String.format(
        "Bonjour M. %s,\n\n"
        + "M. %s habitant à %s vient d'ajouter un nouveau stock :\n\n"
        + "Produit : %s\n"
        + "Quantité : %.2f %s\n"
        + "Prix : %d F CFA\n"
        + "Zone de production : %s\n"
        + "Pays : %s\n"
        + "Contact : %s\n\n"
        + "Lien vers le produit : %s",
        acteur.getNomActeur(),
        ac.getNomActeur(),
        ac.getAdresseActeur(),
        nomProduit,
        quantiteStock,
        uniteMesure,
        prix,
        zoneProduction,
        pays,
        contact,
        lienProduit
    );
    
    // Envoi de la notification (par exemple via WhatsApp)
    try {
        System.out.println("Envoie de la notif : "+message);
        messageService.sendMessageAndSave(acteur.getWhatsAppActeur(), message, ac);
    } catch (Exception e) {
        System.err.println("Erreur lors de l'envoi de la notification : " + e.getMessage());
    }
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
                + stock.getNomProduit() + "\n \n Lien vers le produit est : " + "http://api.koumi.ml/Stock/"+stock.getIdStock()+"/image";
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
    
    //methode de recherche
    // public List<Stock> searchStocksByProductName(String nomProduit) {
    //     return stockRepository.findTop10ByNomProduit(nomProduit);
    // }
    public List<Stock> searchStocksByProductName(String nomProduit) {
        return stockRepository.findTop10ByNomProduitContaining(nomProduit);
    }

    public Page<Stock> getStocksByProduct(String nomProduit,String nomCategorie,List<String> speculations, Double quantiteStock, Integer prixMin,Integer prixMax,  Pageable pageable) {
    return stockRepository.findByProduit(nomProduit,nomCategorie,speculations, quantiteStock, prixMin,prixMax, pageable);
    }
    
    //fecth by id stock 
    public Stock getStockById(String idStock) {
        Stock s = stockRepository.findByIdStock(idStock);
        if(s == null)
            {
                throw new IllegalStateException("Aucun stock trouvé avec l'ID : " + idStock);
            }

     return s;
    }
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
            System.out.println("Televersement en cours");
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

                stocks.setPhoto(imageName);
                System.out.println("Televersement terminer " + imageName);
            } catch (IOException e) {
                throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
            }
        }

        Stock st = stockRepository.save(stocks);
          // Création de l'historique
     historiqueService.createHistorique("Modification" , st.getNomProduit() ,st.getActeur().getNomActeur(), st.getActeur().getLocaliteActeur(),st.getActeur().getNiveau3PaysActeur(),"Modification  de produit " + stocks.getNomProduit());

            return  st;
    }

    public Stock updateQuantiteStock(String id, double nouvelleQuantite) throws Exception {
        Optional<Stock> stockOpt = stockRepository.findById(id);

        if (stockOpt.isPresent()) {
            Stock stock = stockOpt.get();
            stock.setQuantiteStock(nouvelleQuantite);

            // Mettre à jour la date de modification
            stock.setDateModif(LocalDateTime.now().toString());

            return stockRepository.save(stock);
        } else {
            throw new Exception("Stock non trouvé avec l'ID : " + id);
        }
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

    public Stock updateNbViev(String id) throws Exception {
        Optional<Stock> vOpt = stockRepository.findById(id);
        
        if (vOpt.isPresent()) {
            Stock st = vOpt.get();
            int count = st.getNbreView() + 1;
            st.setNbreView(count);

            return stockRepository.save(st);
        } else {
            throw new Exception("Une erreur s'est produite");
        }
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
        return stockRepository.findBySpeculation_CategorieProduit_IdCategorieProduitAndMagasin_IdMagasinAndQuantiteStockGreaterThan(idCategorieProduit, idMagasin,0.0);
    }
    
    // recuperer les stock par  magasin avec pagination
    public Page<Stock> getStocksByMagasinWithPagination(String idMagasin,Pageable pageable) {
        return stockRepository.findByMagasin_IdMagasinAndStatutSotckAndActeurStatutActeurAndQuantiteStockGreaterThan(idMagasin,true, true,pageable,0.0);
    }

    public Page<Stock> getStocksByPaysWithPagination(String nomPays,Pageable pageable) {
        return stockRepository.findAllByPaysAndStatutSotckTrueAndActeurStatutActeurTrueAndQuantiteStockGreaterThan(nomPays,pageable,0.0);
    }

  
   //test libelle
   public Page<Stock> getAllStockByLibelleCategorie(String libelleFiliere, Pageable pageable) {
    return stockRepository.findAllBySpeculation_CategorieProduit_filiere_LibelleFiliereAndStatutSotckAndActeurStatutActeurAndQuantiteStockGreaterThan( libelleFiliere,true,true,pageable,0.0);
    }

    //liste stock par libelle 
    // public Page<Stock> getAllStockByLibelleCategorie(String libelleFiliere, String pays, Pageable pageable) {
    //     // Première requête pour récupérer les matériels pour le pays spécifique
    //     Page<Stock> stockByPays = stockRepository.findAllBySpeculation_CategorieProduit_filiere_LibelleFiliereAndStatutSotckAndActeurStatutActeurAndPaysAndQuantiteStockGreaterThan(
    //         libelleFiliere,true,true, pays.trim().toLowerCase(), pageable,0.0);
    
    //     // Si aucun matériel trouvé pour le pays spécifique
    //     if (!stockByPays.hasContent()) {
    //         System.out.println("Pas d'autres stock à fetch pour le pays " + pays);
    //         // Récupérer les matériels pour d'autres pays
    //         return stockRepository.findAllBySpeculation_CategorieProduit_filiere_LibelleFiliereAndStatutSotckAndActeurStatutActeurAndPaysNotAndQuantiteStockGreaterThan(
    //             libelleFiliere,true,true, pays.trim().toLowerCase(), pageable,0.0);
    //     } else {
    //         System.out.println(" fetch pour le pays " + pays);
    //         List<Stock> stockList = new ArrayList<>(stockByPays.getContent());
    
    //         // Si le nombre d' intrant est inférieur au nombre requis, compléter avec des intrants d'autres pays
    //         if (stockList.size() < pageable.getPageSize()) {
    //             Pageable complementPageable = PageRequest.of(0, pageable.getPageSize() - stockList.size());
    //             Page<Stock> intrantComplement =  stockRepository.findAllBySpeculation_CategorieProduit_filiere_LibelleFiliereAndStatutSotckAndActeurStatutActeurAndPaysNotAndQuantiteStockGreaterThan(
    //                 libelleFiliere,true,true, pays.trim().toLowerCase(), complementPageable,0.0);
    //                 stockList.addAll(intrantComplement.getContent());
    //         }
    //         return new PageImpl<>(stockList, pageable, stockByPays.getTotalElements() + stockList.size());
    //     }
    // }
    
       ///stock libelle filiere
    public Page<Stock> getAllByFiliereAndPays(String libelleFiliere,String nomPays,Pageable pageable) {
        return stockRepository.findAllBySpeculation_CategorieProduit_filiere_LibelleFiliereAndStatutSotckAndActeurStatutActeurAndPaysAndQuantiteStockGreaterThan(libelleFiliere,true,true, nomPays,pageable,0.0);
    }

    ///stock par idCategorie et libelle filiere
    @Transactional
    public Page<Stock> getAllStockPageableByPaysByCategorieAndFiliere(String idcategorie,String libelleFiliere, Pageable pageable) {
        return stockRepository.findAllBySpeculation_CategorieProduit_idCategorieProduitAndSpeculation_CategorieProduit_filiere_LibelleFiliereAndStatutSotckAndActeurStatutActeurAndQuantiteStockGreaterThan(idcategorie, libelleFiliere, true, true,pageable,0.0);
    }
  

    // @Transactional
    // public Page<Stock> getAllStockPageableByPaysByCategorieAndFiliere(String idcategorie,String libelleFiliere, String niveau3PaysActeur, Pageable pageable) {
    //     // Fetch stock from the specified country
    //     Page<Stock> stocksByPays = stockRepository.findAllBySpeculation_CategorieProduit_idCategorieProduitAndSpeculation_CategorieProduit_filiere_LibelleFiliereAndStatutSotckAndActeurStatutActeurAndPaysAndQuantiteStockGreaterThan(
    //         idcategorie, libelleFiliere, true, true, niveau3PaysActeur.trim().toLowerCase(), pageable,0.0);

    //     List<Stock> stocksList = new ArrayList<>(stocksByPays.getContent());

    //     // If no stocks are found for the specified country, fetch stocks from other countries
    //     if (stocksList.isEmpty()) {
    //         Page<Stock> stocksFromOtherCountries = stockRepository.findAllBySpeculation_CategorieProduit_idCategorieProduitAndSpeculation_CategorieProduit_filiere_LibelleFiliereAndStatutSotckAndActeurStatutActeurAndPaysNotAndQuantiteStockGreaterThan(
    //             idcategorie, libelleFiliere ,true, true,  niveau3PaysActeur.trim().toLowerCase(), pageable,0.0);

    //         return new PageImpl<>(stocksFromOtherCountries.getContent(), pageable, stocksFromOtherCountries.getTotalElements());
    //     }

    //     // Fetch stocks from other countries if needed to fill the page
    //     if (stocksList.size() < pageable.getPageSize()) {
    //         Pageable complementPageable = PageRequest.of(0, pageable.getPageSize() - stocksList.size());
    //         Page<Stock> stocksComplement = stockRepository.findAllBySpeculation_CategorieProduit_idCategorieProduitAndSpeculation_CategorieProduit_filiere_LibelleFiliereAndStatutSotckAndActeurStatutActeurAndPaysNotAndQuantiteStockGreaterThan(
    //             idcategorie, libelleFiliere, true, true,  niveau3PaysActeur.trim().toLowerCase(), complementPageable,0.0);
    //         stocksList.addAll(stocksComplement.getContent());
    //     }

    //     return new PageImpl<>(stocksList, pageable, stocksByPays.getTotalElements() + stocksList.size());
    // }

    // recuperer les stock par  acteur avec pagination
    public Page<Stock> getStocksByActeurWithPagination(String idActeur,Pageable pageable) {
        return stockRepository.findByActeur_IdActeurAndQuantiteStockGreaterThan(idActeur, pageable,0.0);
    }

    // recuperer les stock par  acteur avec pagination
    public Page<Stock> getStocksByMagasinAndActeurWithPagination(String idMagasin,String idActeur,Pageable pageable) {
        return stockRepository.findByMagasin_IdMagasinAndActeur_IdActeurAndQuantiteStockGreaterThan(idMagasin,idActeur, pageable,0.0);
    }


    
    //Avec pagination stock par magasin , acteur  et categorie 
    public Page<Stock> listeStockByCategorieProduitAndMagasinWithPagination(String idCategorieProduit, String idMagasin, Pageable pageable) {
        return stockRepository.findBySpeculation_CategorieProduit_IdCategorieProduit_AndMagasin_IdMagasinAndStatutSotckAndActeurStatutActeurAndQuantiteStockGreaterThan(idCategorieProduit,idMagasin, true, true,pageable,0.0);
    }

    //recuperer les stock par categorie produit et idActeur
    public List<Stock> getStocksByCategorieAndActeurIdacteur(String idCategorieProduit, String idActeur) {
        return stockRepository.findBySpeculation_CategorieProduit_IdCategorieProduitAndActeur_IdActeurAndQuantiteStockGreaterThan(idCategorieProduit, idActeur,0.0);
    }

      // Récupérer les stocks par catégorie
    public List<Stock> getStocksByCategorie(CategorieProduit categorie) {
        return stockRepository.findBySpeculation_CategorieProduitAndQuantiteStockGreaterThan(categorie,0.0);
    }


    public Page<Stock> getStocksByCategorieWithPagination(CategorieProduit categorie, Pageable pageable) {
        return stockRepository.findBySpeculation_CategorieProduitAndStatutSotckAndActeurStatutActeurAndQuantiteStockGreaterThan(categorie,true, true, pageable,0.0);
    }


    public List<Stock> listeStockByCategorieProduitAndMagasinAndActeur( String idCategorie, String idMagasin ,String idActeur) throws Exception {

        List<Stock> stockList = stockRepository.findBySpeculation_CategorieProduit_IdCategorieProduitAndMagasin_IdMagasinAndActeurIdActeurAndQuantiteStockGreaterThan(idCategorie,idMagasin,idActeur,0.0);

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
        Stock st = stockRepository.findById(id).orElseThrow(null);

        stockRepository.delete(st);

        historiqueService.createHistorique("Suppression" , st.getNomProduit() ,st.getActeur().getNomActeur(), st.getActeur().getLocaliteActeur(),st.getActeur().getNiveau3PaysActeur(),"Suppression de produit " + st.getNomProduit());

        return "Supprimé avec success";
    }

    public Stock active(String id) throws Exception{
        Stock stock = stockRepository.findById(id).orElseThrow(null);

        try {
            stock.setStatutSotck(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation : " + e.getMessage());
        }

        Stock st = stockRepository.save(stock);
        historiqueService.createHistorique("Activation" , st.getNomProduit() ,st.getActeur().getNomActeur(), st.getActeur().getLocaliteActeur(),st.getActeur().getNiveau3PaysActeur(),"Activation de produit " + st.getNomProduit());

        return st;
    }

    public Stock desactive(String id) throws Exception{
        Stock stock = stockRepository.findById(id).orElseThrow(null);

        try {
            stock.setStatutSotck(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation : " + e.getMessage());
        }

        Stock st = stockRepository.save(stock);
        historiqueService.createHistorique("Désactivation" , st.getNomProduit() ,st.getActeur().getNomActeur(), st.getActeur().getLocaliteActeur(),st.getActeur().getNiveau3PaysActeur(),"Désactivation de produit " + st.getNomProduit());

        return st;
    }
}