package projet.ais.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Acteur;
import projet.ais.models.Alerte;
import projet.ais.models.Commande;
import projet.ais.models.DetailCommande;
import projet.ais.models.Materiel;
import projet.ais.models.Stock;
import projet.ais.models.TypeActeur;
import projet.ais.repository.ActeurRepository;
import projet.ais.repository.AlerteRepository;
import projet.ais.repository.CommandeRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import projet.ais.repository.DetailCommandeRepository;
import projet.ais.repository.IntrantRepository;
import projet.ais.repository.MaterielRepository;
import projet.ais.repository.StockRepository;

@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private IntrantRepository intrantRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
     private ActeurRepository acteurRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private AlerteRepository alerteRepository;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private CodeGenerator codeGenerator;
     @Autowired
    MaterielRepository materielRepository;

    @Autowired
    DetailCommandeRepository detailCommandeRepository;

  

  
        public Commande ajouterStocksACommande(Commande commande, List<Stock> stocks, List<Double> quantitesDemandees) throws Exception {
        // Récupération des stocks correspondant aux identifiants fournis
        List<Stock> stocksFound = stockRepository.findByIdStockIn(
            stocks.stream().map(Stock::getIdStock).collect(Collectors.toList())
        );
    
        // Date et heure actuelles formatées
        String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    
        // Mise à jour des informations de la commande
        commande.setIdCommande(idGenerator.genererCode());
        commande.setCodeCommande(codeGenerator.genererCode());
        commande.setDateCommande(formattedDateTime);
        commande.setStatutCommande(true);
        Commande savedCommande = commandeRepository.save(commande);
    
        // Enregistrement des détails de la commande pour chaque produit
        for (int i = 0; i < stocksFound.size(); i++) {
            Stock stock = stocksFound.get(i);
            double quantiteDemandee = quantitesDemandees.get(i);
            DetailCommande detailCommande = new DetailCommande();
    
            // Création d'une nouvelle instance de DetailCommande
            detailCommande.setIdDetailCommande(idGenerator.genererCode());
            detailCommande.setCodeProduit(stock.getCodeStock());
            detailCommande.setQuantiteDemande(quantiteDemandee);
            detailCommande.setQuantiteLivree(0.0); // Initialement aucun n'a été livré
            detailCommande.setQuantiteNonLivree(0.0); // Initialement aucun n'a été livré
            detailCommande.setNomProduit(stock.getNomProduit());
            detailCommande.setDateAjout(formattedDateTime);
            detailCommande.setCommande(savedCommande);
    
            // Enregistrement du détail de la commande
            detailCommandeRepository.save(detailCommande);
    
            // Mise à jour de la quantité en stock
            double quantiteRestante = stock.getQuantiteStock() - quantiteDemandee;
            stock.setQuantiteStock(quantiteRestante);
            stockRepository.save(stock);
    
            // Mise à jour de la quantité demandée totale dans la commande
            savedCommande.setQuantiteDemande(savedCommande.getQuantiteDemande() + quantiteDemandee);
        }
    
        // Envoi de notifications aux propriétaires des stocks
        Map<Acteur, List<DetailCommande>> proprietairesStocksDetails = new HashMap<>();
    
        // Parcourir les stocks trouvés
        for (Stock stock : stocksFound) {
            Acteur proprietaire = stock.getActeur(); // Récupérer le propriétaire du stock
    
            // Vérifier si le propriétaire est déjà présent dans la Map, sinon ajouter une nouvelle entrée
            if (!proprietairesStocksDetails.containsKey(proprietaire)) {
                proprietairesStocksDetails.put(proprietaire, new ArrayList<>());
            }
    
            // Ajouter le détail de commande à la liste des détails de commande du propriétaire
            List<DetailCommande> details = detailCommandeRepository.findByNomProduit(stock.getNomProduit());
            proprietairesStocksDetails.get(proprietaire).addAll(details);
        }
    
        // Parcourir les entrées de la Map pour envoyer les messages à chaque propriétaire avec les détails de ses stocks commandés
        for (Map.Entry<Acteur, List<DetailCommande>> entry : proprietairesStocksDetails.entrySet()) {
            Acteur proprietaire = entry.getKey();
            List<DetailCommande> detailsCommande = entry.getValue();
    
            // Construire le message pour ce propriétaire avec les détails des stocks commandés
            String message = "Les produits suivants ont été commandés par " + savedCommande.getActeur().getNomActeur() + " :\n";
            Set<String> produitsDemandes = new HashSet<>();
            for (DetailCommande detail : detailsCommande) {
                String nomProduit = detail.getNomProduit();
                double quantiteDemandee = detail.getQuantiteDemande();
    
                // Vérifier si le produit a déjà été ajouté au message
                if (!produitsDemandes.contains(nomProduit)) {
                    produitsDemandes.add(nomProduit);
                    message += "- " + nomProduit + " : quantité demandée " + quantiteDemandee + "\n";
                }
            }
    
            // Envoyer un message par WhatsApp et e-mail uniquement si le propriétaire a un WhatsAppActeur ou un EmailActeur
            if (proprietaire != null && proprietaire.getWhatsAppActeur() != null && proprietaire.getEmailActeur() != null) {
                messageService.sendMessageAndSave(proprietaire.getWhatsAppActeur(), message, proprietaire);
                Alerte al = new Alerte(proprietaire.getEmailActeur(), message, "Nouvelle commande de produits");
                al.setId(idGenerator.genererCode());
                al.setDateAjout(formattedDateTime);
                al.setActeur(proprietaire);
                alerteRepository.save(al);
                emailService.sendSimpleMail(al);
            } else {
                System.out.println("Non trouvé");
            }
         }
    
        return savedCommande;
    }
    
    
    
    
   
   // validé une commande en tant qu'acheteur
    public ResponseEntity<String> enableCommande(String id) throws Exception {
    Commande commande = commandeRepository.findByIdCommande(id);

    if (commande != null) {
        // Mettre à jour le statut de la commande
        commande.setStatutCommande(true);
        commandeRepository.save(commande);

        // Récupérer les détails de commande de la commande
        List<DetailCommande> detailsCommande = commande.getDetailCommandeList();
         
        // Récupérer la liste des acteurs propriétaires des produits commandés
        List<Acteur> acteursProprietaires = detailsCommande.stream()
        .map(detail -> detail.getNomProduit()) // Récupérer le nom du produit de chaque détail
        .flatMap(nomProduit -> stockRepository.findByNomProduit(nomProduit).stream()) // Convertir la collection de Stock en un flux
        .map(stock -> stock.getActeur()) // Récupérer l'acteur associé à chaque stock
        .distinct()
        .collect(Collectors.toList());


        // Informer chaque acteur propriétaire
        for (Acteur acteurProprietaire : acteursProprietaires) {
            // Construire le message pour l'acteur propriétaire
            String message = "Commande validé , vos produits ont été commandés par " + commande.getActeur().getNomActeur() +
                    " (Commande n° " + commande.getCodeCommande() + ").Rendez - vous sur l'appli Koumi pour voir vos produits commandés ";

            // Envoyer le message ou l'alerte à l'acteur propriétaire
            // messageService.sendMessageAndSave(acteurProprietaire.getWhatsAppActeur(), message, acteurProprietaire);

            // Créer et sauvegarder une alerte
            String pattern = "yyyy-MM-dd HH:mm";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(formatter);
            Alerte alerte = new Alerte(acteurProprietaire.getEmailActeur(), message, "Commande de vos produits validés");
            alerte.setId(idGenerator.genererCode());
            alerte.setDateAjout(formattedDateTime);
            alerte.setActeur(acteurProprietaire);
            alerteRepository.save(alerte);

            // Envoyer un e-mail à l'acteur propriétaire
            // emailService.sendSimpleMail(alerte);
        }

        return new ResponseEntity<>("La commande a été validée avec succès, les acteurs propriétaires ont été informés.", HttpStatus.OK);
    } else {
        return new ResponseEntity<>("Commande non trouvée avec l'ID " + id, HttpStatus.BAD_REQUEST);
    }
}

      

    


   //Annuler commande en tant qu'acheteur
   public ResponseEntity<String> disableCommande(String id) throws Exception {
    Commande commande = commandeRepository.findByIdCommande(id);

    if (commande != null) {
        // Mettre à jour le statut de la commande
        if(commande.getStatutCommande()==false){
            throw new IllegalArgumentException("La commande est déjà annulé par default vous pouvez le valider.");
        }
        commande.setStatutCommande(false);
        commandeRepository.save(commande);

        // Récupérer les détails de commande de la commande
        List<DetailCommande> detailsCommande = commande.getDetailCommandeList();

        // Récupérer la liste des acteurs propriétaires des produits commandés
        List<Acteur> acteursProprietaires = detailsCommande.stream()
        .map(detail -> detail.getNomProduit()) // Récupérer le nom du produit de chaque détail
        .flatMap(nomProduit -> stockRepository.findByNomProduit(nomProduit).stream()) // Convertir la collection de Stock en un flux
        .map(stock -> stock.getActeur()) // Récupérer l'acteur associé à chaque stock
        .distinct()
        .collect(Collectors.toList());


        // Informer chaque acteur propriétaire
        for (Acteur acteurProprietaire : acteursProprietaires) {
            // Construire le message pour l'acteur propriétaire
            String message = "Commande annulé " + commande.getActeur().getNomActeur().toUpperCase() + " a annulé la " +
                    " (Commande n° " + commande.getCodeCommande() + ")  .  ";

            // Envoyer le message ou l'alerte à l'acteur propriétaire
            // messageService.sendMessageAndSave(acteurProprietaire.getWhatsAppActeur(), message, acteurProprietaire);

            // Créer et sauvegarder une alerte
            String pattern = "yyyy-MM-dd HH:mm";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(formatter);
            Alerte alerte = new Alerte(acteurProprietaire.getEmailActeur(), message, "Commande de vos produits annulé");
            alerte.setId(idGenerator.genererCode());
            alerte.setDateAjout(formattedDateTime);
            alerte.setActeur(acteurProprietaire);
            alerteRepository.save(alerte);

            // Envoyer un e-mail à l'acteur propriétaire
            // emailService.sendSimpleMail(alerte);
        }

        return new ResponseEntity<>("La commande a été annulé avec succès, les acteurs propriétaires ont été informés.", HttpStatus.OK);
    } else {
        return new ResponseEntity<>("Commande non trouvée avec l'ID " + id, HttpStatus.BAD_REQUEST);
    }
}


    
public ResponseEntity<String> confirmerLivraisonVendeur(String id, Map<String, Double> quantitesLivre) throws Exception {
    Commande commande = commandeRepository.findByIdCommande(id);

    if (commande != null) {
        // Mettre à jour le statut de la commande
        commande.setStatutCommandeLivrer(true);
        commandeRepository.save(commande);

        // Ajouter la quantité livrée pour chaque produit
        for (DetailCommande detail : commande.getDetailCommandeList()) {
            String nomProduit = detail.getNomProduit();
            Double quantiteDemandee = detail.getQuantiteDemande();
            Double quantiteLivre = quantitesLivre.getOrDefault(nomProduit, 0.0);

            if (quantiteLivre > quantiteDemandee) {
                // Gérer le cas où la quantité livrée dépasse la quantité demandée
                return new ResponseEntity<>("La quantité livrée de " + nomProduit + " dépasse la quantité demandée.", HttpStatus.BAD_REQUEST);
            }

            // Mettre à jour la quantité livrée dans le détail de la commande
            detail.setQuantiteLivree(quantiteLivre);
            detail.setQuantiteNonLivree(quantiteDemandee - quantiteLivre);
            detailCommandeRepository.save(detail);
        }

        // Vérifier si toutes les quantités ont été ajoutées
        boolean toutesQuantitesAjoutees = commande.getDetailCommandeList().stream()
                .allMatch(detail -> detail.getQuantiteLivree().equals(detail.getQuantiteDemande()));

        if (toutesQuantitesAjoutees) {
            // Informer le commanditaire par e-mail et WhatsApp
            String message = "Votre commande numéro " + commande.getCodeCommande() + " a été validée par le vendeur avec succès. Un livreur ira vous livrer votre commande bientôt.";
            Alerte al = new Alerte(commande.getActeur().getEmailActeur(), message, "Commande Validée");
            alerteRepository.save(al);
            emailService.sendSimpleMail(al);
            messageService.sendMessageAndSave(commande.getActeur().getWhatsAppActeur(), message, commande.getActeur());
        }

        return new ResponseEntity<>("La livraison de la commande a été validée avec succès.", HttpStatus.OK);
    } else {
        return new ResponseEntity<>("Commande non trouvée avec l'ID " + id + " ou aucun stock associé.", HttpStatus.BAD_REQUEST);
    }
}


//    public ResponseEntity<String> confirmerLivraisonVendeur(String id, Map<String, Double> quantitesLivre) throws Exception {
//     Commande commande = commandeRepository.findByIdCommande(id);

//     if (commande != null) {
//         // Mettre à jour le statut de la commande
//         commande.setStatutCommandeLivrer(true);
//         commandeRepository.save(commande);

//         // Ajouter la quantité livrée pour chaque produit
//         for (DetailCommande detail : commande.getDetailCommandeList()) {
//             String nomProduit = detail.getNomProduit();
//             Double quantiteDemandee = detail.getQuantiteDemande();
//             Double quantiteLivre = quantitesLivre.getOrDefault(nomProduit, 0.0);

//             if (quantiteLivre > quantiteDemandee) {
//                 // Gérer le cas où la quantité livrée dépasse la quantité demandée
//                 return new ResponseEntity<>("La quantité livrée de " + nomProduit + " dépasse la quantité demandée.", HttpStatus.BAD_REQUEST);
//             }

//             // Mettre à jour la quantité livrée dans le détail de la commande
//             // detail.setQuantiteLivree(quantiteLivre);
//             if (quantitesLivre.containsKey(nomProduit)) {
//                 quantitesLivre.get(nomProduit);
//                 // ...
//               } else {
//                 // Gérer le cas où le produit n'est pas présent dans le map
//                 // ...
//               }
//             detail.setQuantiteNonLivree(detail.getQuantiteDemande() - quantiteLivre );
//             detailCommandeRepository.save(detail);
//         }

//         // Vérifier si toutes les quantités ont été ajoutées
//         // boolean toutesQuantitesAjoutees = commande.getDetailCommandeList().stream()
//         //         .allMatch(detail -> detail.getQuantiteLivree().equals(detail.getQuantiteDemande()));

    
//         // if (toutesQuantitesAjoutees) {
//             // Informer le commanditaire par e-mail et WhatsApp
//             // informerCommanditaire(commande.getActeur());
//             // Envoyer une notification par e-mail et WhatsApp à l'utilisateur
//     // String message = "Votre commande numéro "+ commande.getCodeCommande() +" a été validée par le vendeur avec succès un livreur ira vous livrer votre commande bientôt .";
//     // Alerte al = new Alerte(commande.getActeur().getEmailActeur(), message,  "Commande Validée");
//     //  al.setId(idGenerator.genererCode());
//     // alerteRepository.save(al);
//     // emailService.sendSimpleMail(al);
//     // messageService.sendMessageAndSave(commande.getActeur().getWhatsAppActeur(), message, commande.getActeur());
//         // }

//         return new ResponseEntity<>("La livraison de la commande a été validée avec succès.", HttpStatus.OK);
//     } else {
//         return new ResponseEntity<>("Commande non trouvée avec l'ID " + id + " ou aucun stock associé.", HttpStatus.BAD_REQUEST);
//     }
// }

// private void informerCommanditaire(Acteur acteur) throws Exception {
//     // Envoyer une notification par e-mail et WhatsApp à l'utilisateur
//     String message = "Votre commande a été validée avec succès.";
//     Alerte al = new Alerte(acteur.getEmailActeur(), message,  "Commande Validée");
//     emailService.sendSimpleMail(al);
//     messageService.sendMessageAndSave(acteur.getWhatsAppActeur(), message, acteur);
// }

    
     public String commandeMateriel(String idMateriel, String idActeur) throws Exception{
        Acteur ac = acteurRepository.findByIdActeur(idActeur);
        Materiel mat = materielRepository.findByIdMateriel(idMateriel);

        if(ac == null)
            throw new EntityNotFoundException("Aucun acteur trouvé");
        
        if(mat == null)
            throw new EntityNotFoundException("Aucun materiel trouvé");

            mat.setStatutCommande(true);
            
            try {
            String codes  = codeGenerator.genererCode();
            String idCode = idGenerator.genererCode();
  
            String pattern = "yyyy-MM-dd HH:mm";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(formatter);

            // Création de la commande
            Commande commande = new Commande();
            commande.setIdCommande(idCode);
            commande.setCodeCommande(codes);
            commande.setActeur(ac);
            commande.setCodeAcheteur(mat.getCodeMateriel());
            commande.setCodeCommande(mat.getCodeMateriel());
            commande.setNomProduit(mat.getNom());
            commande.setDescriptionCommande("Allocation de materiel");
            commande.setDateCommande(formattedDateTime);
            // Vous pouvez ajouter le matériel commandé à la liste des matériels de la commande
            commande.setMaterielList(Arrays.asList(mat));
            // Enregistrement de la commande
            // http://localhost:9000//commande/addCommandeMateriel
            commandeRepository.save(commande);
            // Envoi du message pour la commande
            String msg = "Bonjour  " + mat.getActeur().getNomActeur().toUpperCase() + " vous avez une nouvelle commande pour le matériel : "
                    + mat.getNom() + " de la part de M. " + ac.getNomActeur() + " Numéro de téléphone : "
                    + ac.getWhatsAppActeur() + " Adresse : " + ac.getAdresseActeur();
            // messageService.sendMessageAndSave(mat.getActeur().getWhatsAppActeur(), msg, ac.getNomActeur());
        } catch (Exception e) {
            throw new Exception("Erreur lors de la commande : " + e.getMessage());
        }
        return "Commande ajoutée avec succès";
    }

    public String annulerCommande(String idMateriel, String idActeur) throws Exception{
        Acteur ac = acteurRepository.findByIdActeur(idActeur);
        Materiel mat = materielRepository.findByIdMateriel(idMateriel);

        if(ac == null)
            throw new EntityNotFoundException("Aucun acteur trouvé");
        
        if(mat == null)
            throw new EntityNotFoundException("Aucun materiel trouvé");

            mat.setStatutCommande(false);
            try { 
            // Envoi du message pour la commande
            String msg = "Bonjour  " + ac.getNomActeur().toUpperCase() + " Votre commande de materiel " + mat.getNom().toUpperCase() + " a été annuler ";
            messageService.sendMessageAndSave(mat.getActeur().getWhatsAppActeur(), msg, ac);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la commande : " + e.getMessage());
        }
        return "Commande annuler avec succèss";
    }

    
    public Commande confirmerLivraison(String id){
        Commande commande = commandeRepository.findByIdCommande(id);

        commande.setStatutCommandeLivrer(true);

        return commandeRepository.save(commande);
        
    }



    public String confirmerCommande(String idCommande) throws Exception {

        Commande commande = commandeRepository.findByIdCommande(idCommande);

        commande.setStatutConfirmation(true);
        
        commandeRepository.save(commande);

        String msg = "Bonjour  " + commande.getActeur().getNomActeur().toUpperCase() + " Votre commande de materiel  a été confirmer ";
        try {
                messageService.sendMessageAndSave(commande.getActeur().getWhatsAppActeur(), msg, commande.getActeur());
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }

        return "Commande confirmée avec succès";
    }

    public List<Commande> getAllCommandeByActeur(String idActeur) {
        // Récupérer tout les commandes de l'utilisateur depuis la base de données
        List<Commande> commande = commandeRepository.findByActeurIdActeur(idActeur);

        commande.sort(Comparator.comparing(Commande::getDateCommande).reversed());

        return commande;
    }

    public List<Commande> getCommandeByActeur(String id){
        List<Commande> commandeList =commandeRepository.findAll();

        if(commandeList.isEmpty())
            throw new EntityNotFoundException("Aucune commande trouvé");

            commandeList.sort(Comparator.comparing(Commande::getDateCommande).reversed());
        return commandeList;
    }
}
