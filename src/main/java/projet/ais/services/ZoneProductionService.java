package projet.ais.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Acteur;
import projet.ais.models.Alerte;
import projet.ais.models.Commande;
import projet.ais.models.Stock;
import projet.ais.models.Unite;
import projet.ais.models.ZoneProduction;
import projet.ais.repository.ActeurRepository;
import projet.ais.repository.ZoneProductionRepository;
import com.sun.jdi.request.DuplicateRequestException;

import jakarta.persistence.EntityNotFoundException;


@Service
public class ZoneProductionService {
    
    @Autowired
    ZoneProductionRepository zoneProductionRepository;
    @Autowired
    CodeGenerator codeGenerator;
    @Autowired
    IdGenerator idGenerator ;
    @Autowired
    ActeurRepository acteurRepository;

    public ZoneProduction createZoneProduction(ZoneProduction zoneProduction, MultipartFile imageFile) throws Exception{
        ZoneProduction zoneProductions = zoneProductionRepository.findByNomZoneProduction(zoneProduction.getNomZoneProduction());
        //  Acteur acteur = acteurRepository.findByIdActeur(zoneProduction.getActeur().getIdActeur());

        // if(acteur == null)
        //     throw new IllegalStateException("Aucun acteur trouvé");

        if(zoneProductions != null)
            throw new DuplicateRequestException("Cette zone de production existe déjà");

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
                    zoneProduction.setPhotoZone("ais/" + imageName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }
            String codes = codeGenerator.genererCode();
            String idCodes = idGenerator.genererCode();

        zoneProduction.setCodeZone(codes);
        zoneProduction.setStatutZone(true);
        zoneProduction.setIdZoneProduction(idCodes);
        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);  
        zoneProduction.setDateAjout(formattedDateTime);
        return zoneProductionRepository.save(zoneProduction);
    }

    public ZoneProduction updateZoneProduction(ZoneProduction zoneProduction, String id,MultipartFile imageFile) throws Exception{
        ZoneProduction zoneProductions = zoneProductionRepository.findById(id).orElseThrow(null);

        zoneProductions.setNomZoneProduction(zoneProduction.getNomZoneProduction());
        zoneProduction.setLatitude(zoneProduction.getLatitude());
        zoneProductions.setLongitude(zoneProduction.getLongitude());
        zoneProductions.setDateAjout(zoneProductions.getDateAjout());
        zoneProductions.setPersonneModif(zoneProduction.getPersonneModif() );
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
                zoneProductions.setPhotoZone("ais/" + imageName);
            } catch (IOException e) {
                throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
            }
        }
        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);       
        zoneProduction.setDateModif(formattedDateTime);
        return zoneProductionRepository.save(zoneProductions);
    }

    public List<ZoneProduction> getZoneProduction(){
        List<ZoneProduction> zoneProductionList = zoneProductionRepository.findAll();

        if(zoneProductionList.isEmpty())
            throw new EntityNotFoundException("Speculation non trouvé");
        zoneProductionList = zoneProductionList
        .stream().sorted((z1,z2) -> z2.getNomZoneProduction().compareTo(z1.getNomZoneProduction()))
        .collect(Collectors.toList());

        return zoneProductionList;
    }

    // public List<ZoneProduction> getZoneProductionByActeur(String idActeur){
    //     List<ZoneProduction> zoneProductionList = zoneProductionRepository.findByActeurIdActeur(idActeur);

    //     if(zoneProductionList.isEmpty())
    //         throw new EntityNotFoundException("Speculation non trouvé");
    //     zoneProductionList = zoneProductionList
    //     .stream().sorted((z1,z2) -> z2.getNomZoneProduction().compareTo(z1.getNomZoneProduction()))
    //     .collect(Collectors.toList());

    //     return zoneProductionList;
    // }

     public List<ZoneProduction> getAllZoneByActeur(String id){
        List<ZoneProduction> zoneList = zoneProductionRepository.findByActeurIdActeur(id);

        if(zoneList.isEmpty())
             throw new EntityNotFoundException("Liste sous region vide");
             
             zoneList = zoneList
        .stream().sorted((u1,u2) -> u2.getNomZoneProduction().compareTo(u1.getNomZoneProduction()))
        .collect(Collectors.toList());

        return zoneList;
    }

    public String deleteZoneProduction(String id){
        ZoneProduction zoneProduction = zoneProductionRepository.findById(id).orElseThrow(null);

        zoneProductionRepository.delete(zoneProduction);
        return "Supprimé avec success";
    }

    public ZoneProduction active(String id) throws Exception{
        ZoneProduction zoneProduction = zoneProductionRepository.findById(id).orElseThrow(null);

        try {
            zoneProduction.setStatutZone(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation  de la zone de production : " + e.getMessage());
        }
        return zoneProductionRepository.save(zoneProduction);
    }

    public ZoneProduction desactive(String id) throws Exception{
        ZoneProduction zoneProduction = zoneProductionRepository.findById(id).orElseThrow(null);

        try {
            zoneProduction.setStatutZone(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la descativation de la zone de production : " + e.getMessage());
        }
        return zoneProductionRepository.save(zoneProduction);
    }

  // faire une commande d'un ou plusieurs produits à la fois
//     public Commande ajouterStocksACommande(Commande commande, List<Stock> stocks, List<Double> quantitesDemandees) throws Exception {
//         List<String> stockIds = new ArrayList<>();
//         for (Stock stock : stocks) {
//             stockIds.add(stock.getIdStock());
//         } 
//         List<Stock> stocksFound = stockRepository.findByIdStockIn(stockIds);
//         String pattern = "yyyy-MM-dd HH:mm";
//         DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
//         LocalDateTime now = LocalDateTime.now();
//         String formattedDateTime = now.format(formatter);
//     // Maintenant, stocksFound contient les stocks correspondant aux identifiants de stocks fournis
//     // Mettre à jour les autres informations de la commande
//     commande.setIdCommande(idGenerator.genererCode());
    
//     commande.setCodeCommande(codeGenerator.genererCode());
//     commande.setQuantiteNonLivree(commande.getQuantiteDemande() - commande.getQuantiteLivree());
//     commande.setDateCommande(formattedDateTime); // Mettez la date actuelle ou toute autre logique de date que vous souhaitez
//      commande.setStatutCommande(true);
//     Commande savedCommande = commandeRepository.save(commande);
//     // Enregistrer la commande avec les stocks associés et les informations mises à jour
//     String msg = "Votre commande a été passé avec succès veuillez patienter avant qu'elle soit confirmer par les vendeurs des produits que vous avez commandé. Votre numéro de commande est "+ savedCommande.getCodeCommande();
//     // messageService.sendMessageAndSave(savedCommande.getActeur().getWhatsAppActeur(), msg, savedCommande.getActeur());
//     Alerte alerte = new Alerte(savedCommande.getActeur().getEmailActeur(), msg, "Commande passé avec succès");
//     alerte.setId(idGenerator.genererCode());
//     alerte.setDateAjout(formattedDateTime);
//     alerte.setActeur(savedCommande.getActeur());

//     alerteRepository.save(alerte);
//     // emailService.sendSimpleMail(alerte);
//       // Calculer la quantité non livrée en utilisant la somme des quantités demandées
//     double quantiteDemandeTotale = 0.0;
//     for (Double quantite : quantitesDemandees) {
//         quantiteDemandeTotale += quantite;
//     }

//     commande.setQuantiteNonLivree(quantiteDemandeTotale - commande.getQuantiteLivree());

//      // Enregistrer les détails de la commande pour chaque produit
//     //  for (int i = 0; i < stocksFound.size(); i++) {
//     //     Stock stock = stocksFound.get(i);
//     //     double quantiteDemandee = quantitesDemandees.get(i);

//     //     // Créer une nouvelle instance de DetailCommande
//     //     DetailCommande detailCommande = new DetailCommande();
//     //     detailCommande.setIdDetailCommande(idGenerator.genererCode());
//     //     // detailCommande.setCodeProduit(stock.getCodeStock());
//     //     detailCommande.setQuantiteDemande(quantiteDemandee);
//     //     detailCommande.setQuantiteLivree(savedCommande.getQuantiteLivree()); // Initialement aucun n'a été livré
//     //     // detailCommande.setNomProduit(stock.getNomProduit());
//     //     detailCommande.setDateAjout(formattedDateTime);
//     //     detailCommande.setCommande(savedCommande);

//     //     // Enregistrer le détail de la commande
//     //     detailCommandeRepository.save(detailCommande);

//     //     // Mise à jour de la quantité non livrée de la commande
         
//     //     savedCommande.setQuantiteDemande(quantiteDemandee);

//     //     //  Mise à jour de la quantité en stock
//     //     double quantiteRestante = stock.getQuantiteStock() - quantiteDemandee;
//     //     stock.setQuantiteStock(quantiteRestante);
//     //     stockRepository.save(stock);
//     // }
//     // Envoyer des notifications aux propriétaires des stocks
//     Map<Acteur, List<Stock>> proprietairesStocksDetails = new HashMap<>();

//     // Parcourir les stocks trouvés
//     for (Stock stock : stocksFound) {
//         Acteur proprietaire = stock.getActeur(); // Récupérer le propriétaire du stock
    
//         // Vérifier si le propriétaire est déjà présent dans la Map, sinon ajouter une nouvelle entrée
//         if (!proprietairesStocksDetails.containsKey(proprietaire)) {
//             proprietairesStocksDetails.put(proprietaire, new ArrayList<>());
//         }
    
//         // Ajouter le stock à la liste des stocks du propriétaire
//         proprietairesStocksDetails.get(proprietaire).add(stock);
//             // Faites quelque chose avec les stocks trouvés
//          double quantiteStock = stock.getQuantiteStock();
//                 savedCommande.setNomProduit(stock.getNomProduit());
//                 //  if(getQuantiteDemande() > commande.getQuantiteLivree()){
//                 //     throw new RuntimeException("La quantité à livrer ne doit pas être supérieur à la quantité demandé");
//                 //  }
//                  if(savedCommande.getQuantiteDemande() > stock.getQuantiteStock() ){
//                     throw new RuntimeException("La quantité de stock demandé n'est pas disponible");
//                  }
//         double quantiteRestant = quantiteStock - savedCommande.getQuantiteDemande();
//         stock.setQuantiteStock(quantiteRestant);
//         savedCommande.setStock(stocks);
//         stockRepository.save(stock);
//     }

//    // Parcourir les entrées de la Map pour envoyer les messages à chaque propriétaire avec les détails de ses stocks commandés
// for (Map.Entry<Acteur, List<Stock>> entry : proprietairesStocksDetails.entrySet()) {
//     Acteur proprietaire = entry.getKey();
//     List<Stock> stocksProprietaire = entry.getValue();

//     // Construire le message pour ce propriétaire avec les détails des stocks commandés
//     String message = "Les produits suivants ont été commandés par " + savedCommande.getActeur().getNomActeur() + " :\n";
//     for (Stock stock : stocksProprietaire) {
//         message += "- " + stock.getNomProduit() + " : " + " quantités " + savedCommande.getQuantiteDemande() + "\n";
//     }

//     // Envoyer un message par WhatsApp et e-mail uniquement si le propriétaire a un WhatsAppActeur ou un EmailActeur
//     if (proprietaire != null && proprietaire.getWhatsAppActeur() != null && proprietaire.getEmailActeur() != null) {
//         messageService.sendMessageAndSave(proprietaire.getWhatsAppActeur(), message, proprietaire);
//         Alerte al = new Alerte(proprietaire.getEmailActeur(), message, "Nouvelle commande de produits");
//         al.setId(idGenerator.genererCode());
//         al.setDateAjout(formattedDateTime);
//         al.setActeur(proprietaire);
//         alerteRepository.save(al);
//         emailService.sendSimpleMail(al);
//     } else {
//         System.out.println("Non trouvé");
//     }
// }
     
//         // for (Acteur proprietaire : proprietairesStocks) {
//         //     // Envoyer un message par WhatsApp
//         //     messageService.sendMessageAndSave(proprietaire.getWhatsAppActeur(), message, proprietaire);
            
//         //     // Envoyer un e-mail
//         //     Alerte al = new Alerte(proprietaire.getEmailActeur(), message, "Nouvelle commande de produits");
//         //     alerte.setId(idGenerator.genererCode());
//         //     alerte.setDateAjout(formattedDateTime);
//         //     alerte.setActeur(proprietaire);
//         //     alerteRepository.save(al);
//         //     emailService.sendSimpleMail(al);
//         //     System.out.println("Email proprio " + proprietaire.getEmailActeur() + "Num "+ proprietaire.getWhatsAppActeur());
//         // }
 
     
//         return savedCommande;
//     }
    

}
