package projet.ais.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Acteur;
import projet.ais.models.Alerte;
import projet.ais.models.Commande;
import projet.ais.models.DetailCommande;
import projet.ais.models.Intrant;
import projet.ais.models.Materiels;
import projet.ais.models.Stock;
import projet.ais.repository.ActeurRepository;
import projet.ais.repository.AlerteRepository;
import projet.ais.repository.CommandeRepository;

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

    public Commande ajouterStocksACommande(Acteur acteur, Optional<List<Stock>> stocks, Optional<List<Intrant>> intrants, Optional<List<Double>>  quantitesDemandees, Optional<List<Double>>  quantitesIntrants) throws Exception {
    
        Commande commande = new Commande();

    // Extraire les listes des Optionals
    List<Stock> stockss = stocks.orElse(Collections.emptyList());
    List<Intrant> intrantss = intrants.orElse(Collections.emptyList());

    // Récupération des stocks correspondant aux identifiants fournis
    List<Stock> stocksFound = stockRepository.findByIdStockIn(
        stocks.map(stockList -> stockList.stream().map(Stock::getIdStock).collect(Collectors.toList())).orElse(Collections.emptyList())
    );


    // Récupération des intrants correspondant aux identifiants fournis
    List<Intrant> intrantsFound = intrantRepository.findByIdIntrantIn(
        intrants.map(intrantList -> intrantList.stream().map(Intrant::getIdIntrant).collect(Collectors.toList())).orElse(Collections.emptyList())
    );
    

    // Date et heure actuelles formatées
    String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        for (int i = 0; i < stocksFound.size(); i++) {
            Stock stock = stocksFound.get(i);
            // double quantiteDemandee = quantitesDemandees.get(i);
            double quantiteDemandee = quantitesDemandees.orElse(Collections.emptyList()).get(i);
        // Vérifier si la quantité est égale à 1 ou si la quantité demandée est supérieure à celle restante
        if (quantiteDemandee >= stock.getQuantiteStock()) {
            throw new Exception("La quantité rentant pour le produit " + stock.getNomProduit() + "est insuffisant");
        }
    }
    for (int i = 0; i < intrantsFound.size(); i++) {
        Intrant intrant = intrantsFound.get(i);
        // double quantiteInt = quantitesIntrants.get(i);
        double quantiteInt = quantitesIntrants.orElse(Collections.emptyList()).get(i);
        
        if ( quantiteInt >= intrant.getQuantiteIntrant()) {
            throw new Exception("La quantité restant pour l'intrant " + intrant.getNomIntrant() + "est insuffisant");
        }
    }

    // Mise à jour des informations de la commande
    commande.setIdCommande(idGenerator.genererCode());
    commande.setCodeCommande(codeGenerator.genererCode());
    commande.setDateCommande(formattedDateTime);
    commande.setStatutCommande(true);
    commande.setActeur(acteur);
    Acteur acteurProprietaire = new Acteur();
    if (!stocksFound.isEmpty()) {
        acteurProprietaire = stocksFound.get(0).getActeur();
    } else if (!intrantsFound.isEmpty()) {
        acteurProprietaire = intrantsFound.get(0).getActeur();
    }
    
    // Utiliser l'acteur propriétaire trouvé pour définir l'acteur de la commande
    if (acteurProprietaire != null) {
    commande.setActeurProprietaire(acteurProprietaire);
    } else {
        // Si aucun acteur propriétaire n'est trouvé, utilisez l'acteur passé en paramètre
        System.out.println("aucun acteur trouver");
    }
   Commande savedCommande = commandeRepository.save(commande);
   // Enregistrement des détails de la commande pour chaque produit
   // Récupérer l'acteur propriétaire à partir des stocks ou des intrants
    for (int i = 0; i < stocksFound.size(); i++) {
        Stock stock = stocksFound.get(i);
        // double quantiteDemandee = quantitesDemandees.get(i);
        double quantiteDemandee = quantitesDemandees.orElse(Collections.emptyList()).get(i);
  

   
        // Création d'une nouvelle instance de DetailCommande
        DetailCommande detailCommande = new DetailCommande();
        detailCommande.setIdDetailCommande(idGenerator.genererCode());
        detailCommande.setCodeProduit(stock.getCodeStock());
        detailCommande.setQuantiteDemande(quantiteDemandee);
        detailCommande.setQuantiteLivree(0.0); // Initialement aucun n'a été livré
        detailCommande.setQuantiteNonLivree(quantiteDemandee); // Initialement aucun n'a été livré
        detailCommande.setNomProduit(stock.getNomProduit());
        detailCommande.setDateAjout(formattedDateTime);
        detailCommande.setCommande(savedCommande);
        detailCommande.setStock(stock);
        detailCommande.setIsStock(true);


        // Enregistrement du détail de la commande
         detailCommandeRepository.save(detailCommande);

        // Mise à jour de la quantité en stock
        double quantiteRestante = stock.getQuantiteStock() - quantiteDemandee;
        stock.setQuantiteStock(quantiteRestante);
        stockRepository.save(stock);

        // Mise à jour de la quantité demandée totale dans la commande
        savedCommande.setQuantiteDemande(savedCommande.getQuantiteDemande() + quantiteDemandee);
    }
    // Enregistrement des détails de la commande pour chaque intrant
    for (int i = 0; i < intrantsFound.size(); i++) {
        Intrant intrant = intrantsFound.get(i);
        // double quantiteInt = quantitesIntrants.get(i);
        double quantiteInt = quantitesIntrants.orElse(Collections.emptyList()).get(i);
       
        // Création d'une nouvelle instance de DetailCommande
        DetailCommande detailCommande = new DetailCommande();
        detailCommande.setIdDetailCommande(idGenerator.genererCode());
        detailCommande.setCodeProduit(intrant.getCodeIntrant());
        detailCommande.setQuantiteDemande(quantiteInt);
        detailCommande.setQuantiteLivree(0.0); // Initialement aucun n'a été livré
        detailCommande.setQuantiteNonLivree(quantiteInt); // Initialement aucun n'a été livré
        detailCommande.setNomProduit(intrant.getNomIntrant());
        detailCommande.setDateAjout(formattedDateTime);
        detailCommande.setCommande(savedCommande);
        detailCommande.setIntrant(intrant);
        detailCommande.setIsStock(false);

        // Enregistrement du détail de la commande
       detailCommandeRepository.save(detailCommande);

        // Mise à jour de la quantité en intrant
        double quantiteRestante = intrant.getQuantiteIntrant() - quantiteInt;
        intrant.setQuantiteIntrant(quantiteRestante);
        intrantRepository.save(intrant);

        // Mise à jour de la quantité demandée totale dans la commande
        savedCommande.setQuantiteDemande(savedCommande.getQuantiteDemande() + quantiteInt);
    }

    // Envoi de notifications aux propriétaires des stocks
    for (Stock stock : stocksFound) {
        Acteur proprietaire = stock.getActeur();
        String message = "Une commande a été  passé par " + savedCommande.getActeur().getNomActeur() + " code commande " + savedCommande.getCodeCommande().toUpperCase() + "rendez vous sur l'appli Koumi pour voir les details et confirmer la commande";
        // String message = "Les produits suivants ont été commandés par " + savedCommande.getActeur().getNomActeur() + " :\n" +
        //                  "- " + stock.getNomProduit() + " : quantités " + savedDetailCommande.getQuantiteDemande() + "\n" +
        //                  "Veuillez lui livrer sa commande dans les plus brefs délais";
        System.out.println( "Message : " + message);
        // Envoi d'un e-mail uniquement si le propriétaire a une adresse e-mail
        if (proprietaire != null && proprietaire.getEmailActeur() != null) {
            Alerte al = new Alerte(proprietaire.getEmailActeur(), message, "Nouvelle commande de produits");
            al.setId(idGenerator.genererCode());
            al.setDateAjout(formattedDateTime);
            al.setActeur(proprietaire);
            alerteRepository.save(al);
            emailService.sendSimpleMail(al);
            messageService.sendMessageAndSave(proprietaire.getWhatsAppActeur(),message, proprietaire);
        } else {
            System.out.println("Adresse e-mail introuvable pour le propriétaire du stock : " + proprietaire);
        }
    }
    for (Intrant intrant : intrantsFound) {
        Acteur proprietaire = intrant.getActeur();
        String message = "Une commande a été  passé par " + savedCommande.getActeur().getNomActeur() + " code commande " + savedCommande.getCodeCommande().toUpperCase() + "rendez vous sur l'appli Koumi pour voir les details et confirmer la commande";
        // String message = "Les produits suivants ont été commandés par " + savedCommande.getActeur().getNomActeur() + " :\n" +
        //                  "- " + stock.getNomProduit() + " : quantités " + savedDetailCommande.getQuantiteDemande() + "\n" +
        //                  "Veuillez lui livrer sa commande dans les plus brefs délais";
        System.out.println( "Message : " + message);
        // Envoi d'un e-mail uniquement si le propriétaire a une adresse e-mail
        if (proprietaire != null && proprietaire.getEmailActeur() != null) {
            Alerte al = new Alerte(proprietaire.getEmailActeur(), message, "Nouvelle commande de produits");
            al.setId(idGenerator.genererCode());
            al.setDateAjout(formattedDateTime);
            al.setActeur(proprietaire);
            alerteRepository.save(al);
            emailService.sendSimpleMail(al);
            messageService.sendMessageAndSave(proprietaire.getWhatsAppActeur(),message, proprietaire);
        } else {
            System.out.println("Adresse e-mail introuvable pour le propriétaire du stock : " + proprietaire);
        }
    }

    return savedCommande;
   }


    
   public void confirmerCommande(String idDetailCommande, double quantiteLivree) throws Exception {
    // Rechercher le détail de la commande par l'ID
    Optional<DetailCommande> optionalDetailCommande = detailCommandeRepository.findById(idDetailCommande);

    if (optionalDetailCommande.isPresent()) {
        DetailCommande detailCommande = optionalDetailCommande.get();

        // Mettre à jour la quantité livrée
        double quantiteNonLivree = detailCommande.getQuantiteDemande() - quantiteLivree;
        if (quantiteNonLivree < 0) {
            throw new Exception("La quantité livrée ne peut dépasser la quantité demandée");
        }

        detailCommande.setQuantiteLivree(quantiteLivree);
        detailCommande.setQuantiteNonLivree(quantiteNonLivree);

        String msg = "La livraison de votre commande de " + optionalDetailCommande.get().getNomProduit().toUpperCase() + " passé le " + optionalDetailCommande.get().getCommande().getDateCommande()  + " a été confirmer avec succès par le proprietaire en cas de retard de livraison vous pouvez le contacter à son numéro " + optionalDetailCommande.get().getCommande().getActeurProprietaire().getWhatsAppActeur() ;
        // Enregistrer les modifications dans la base de données
        detailCommandeRepository.save(detailCommande);
        // messageService.sendMessageAndSave(optionalDetailCommande.get().getCommande().getActeur().getWhatsAppActeur(), msg, optionalDetailCommande.get().getCommande().getActeur());

        // Récupérer tous les détails de commande liés à la même commande
        List<DetailCommande> allDetailsForCommande = detailCommandeRepository.findByCommandeIdCommande(detailCommande.getCommande().getIdCommande());

        // Vérifier si la somme des quantités livrées pour tous les détails de commande est égale à la quantité demandée
        double quantiteTotaleLivree = allDetailsForCommande.stream().mapToDouble(DetailCommande::getQuantiteLivree).sum();
        double quantiteDemandee = allDetailsForCommande.stream().mapToDouble(DetailCommande::getQuantiteDemande).sum();

        if (quantiteTotaleLivree == quantiteDemandee) {
            // Mettre à jour le statut de la commande à "confirmé"
            Commande commande = detailCommande.getCommande();
            commande.setStatutCommandeLivrer(true);
            commande.setStatutConfirmation(true);
            commandeRepository.save(commande);
            String msgg = "La livraison de tous les produits de votre commande avec le code " + optionalDetailCommande.get().getCommande().getCodeCommande() + " a été confirmé par " + "  proprietaire en cas de retard de livraison vous pouvez le contacter à son numéro " + optionalDetailCommande.get().getCommande().getActeurProprietaire().getWhatsAppActeur() ;
            messageService.sendMessageAndSave(optionalDetailCommande.get().getCommande().getActeur().getWhatsAppActeur(), msgg, optionalDetailCommande.get().getCommande().getActeur());

        }
    } else {
        // Lever une exception si le détail de la commande n'est pas trouvé
        throw new Exception("Détail de la commande non trouvé");
    }
}

   //Annuler
   public void annulerCommandeParProduit(String idDetailCommande, String description) throws Exception {
    // Rechercher le détail de la commande par l'ID
    Optional<DetailCommande> optionalDetailCommande = detailCommandeRepository.findById(idDetailCommande);

    if (optionalDetailCommande.isPresent()) {
        DetailCommande detailCommande = optionalDetailCommande.get();

        // Mettre à jour la quantité livrée
        

        detailCommande.setDescription(description);
        detailCommande.setQuantiteLivree(0.0);

        String msg = "La livraison de votre commande de " + optionalDetailCommande.get().getNomProduit().toUpperCase() + " passé le " + optionalDetailCommande.get().getCommande().getDateCommande()  + " a été annulée  par le proprietaire  vous pouvez le contacter à son numéro " + optionalDetailCommande.get().getCommande().getActeurProprietaire().getWhatsAppActeur() ;
        // Enregistrer les modifications dans la base de données
        detailCommandeRepository.save(detailCommande);
        // messageService.sendMessageAndSave(optionalDetailCommande.get().getCommande().getActeur().getWhatsAppActeur(), msg, optionalDetailCommande.get().getCommande().getActeur());

        // Récupérer tous les détails de commande liés à la même commande
        // List<DetailCommande> allDetailsForCommande = detailCommandeRepository.findByCommandeIdCommande(detailCommande.getCommande().getIdCommande());

        // Vérifier si la somme des quantités livrées pour tous les détails de commande est égale à la quantité demandée
        // double quantiteTotaleLivree = allDetailsForCommande.stream().mapToDouble(DetailCommande::getQuantiteLivree).sum();
        // double quantiteDemandee = allDetailsForCommande.stream().mapToDouble(DetailCommande::getQuantiteDemande).sum();

        // if (quantiteTotaleLivree == quantiteDemandee) {
        //     // Mettre à jour le statut de la commande à "annulé"
        //     Commande commande = detailCommande.getCommande();
        //     commande.setStatutCommandeLivrer(true);
        //     commandeRepository.save(commande);
        //     String msgg = "L'annulation de la livraison de tous les produits de votre commande avec le code " + optionalDetailCommande.get().getCommande().getCodeCommande() + " a été confirmé par " + "  proprietaire vous pouvez le contacter à son numéro " + optionalDetailCommande.get().getCommande().getActeurProprietaire().getWhatsAppActeur() ;
        //     messageService.sendMessageAndSave(optionalDetailCommande.get().getCommande().getActeur().getWhatsAppActeur(), msgg, optionalDetailCommande.get().getCommande().getActeur());

        // }
    } else {
        // Lever une exception si le détail de la commande n'est pas trouvé
        throw new Exception("Détail de la commande non trouvé");
    }
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
        // Acteur acteursProprietaires = detailsCommande.stream()
        // .map(detail -> detail.getNomProduit()) // Récupérer le nom du produit de chaque détail
        // .flatMap(nomProduit -> stockRepository.findByNomProduit(nomProduit).stream()) // Convertir la collection de Stock en un flux
        // .map(stock -> stock.getActeur()) // Récupérer l'acteur associé à chaque stock
        // .distinct()
        // .collect(Collectors.toList());

        Acteur acteurProprietaire = commande.getActeurProprietaire();
        // Informer chaque acteur propriétaire
            // Construire le message pour l'acteur propriétaire
            String message = "Commande validé , vos produits ont été commandés par " + commande.getActeur().getNomActeur() +
                    " (Commande n° " + commande.getCodeCommande() + ").Rendez - vous sur l'appli Koumi pour voir vos produits commandés ";

            // Envoyer le message ou l'alerte à l'acteur propriétaire
            messageService.sendMessageAndSave(acteurProprietaire.getWhatsAppActeur(), message, acteurProprietaire);

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

        return new ResponseEntity<>("La commande a été validée avec succès, le propriétaire a été informés.", HttpStatus.OK);
    } else {
        return new ResponseEntity<>("Commande non trouvée avec l'ID " + id, HttpStatus.BAD_REQUEST);
    }
}

      
        public List<DetailCommande> getDetailsByCommandeId(String idCommande) {
            Optional<Commande> commandes = commandeRepository.findById(idCommande);
            return detailCommandeRepository.findByCommande(commandes);
        }


        public String getDetailCountByCommandeId(String commandeId) {
            Commande commande = commandeRepository.findById(commandeId).orElseThrow(() -> new RuntimeException("Commande non trouvée"));
            return detailCommandeRepository.countByCommande(commande);
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

     
        Acteur acteurProprietaire = commande.getActeurProprietaire();
        // Informer chaque acteur propriétaire
        // for (Acteur acteurProprietaire : acteursProprietaires) {
            // Construire le message pour l'acteur propriétaire
            String message = "Commande annulé " + commande.getActeur().getNomActeur().toUpperCase() + " a annulé la " +
                    " (Commande n° " + commande.getCodeCommande() + ")  .  ";

            // Envoyer le message ou l'alerte à l'acteur propriétaire
            messageService.sendMessageAndSave(acteurProprietaire.getWhatsAppActeur(), message, acteurProprietaire);

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
        // }

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



   public Page<Commande> getAllCommandePageable(Pageable pageable) {
        return commandeRepository.findAll(pageable);
    }


    
     public String commandeMateriel(String idMateriel, String idActeur) throws Exception{
        Acteur ac = acteurRepository.findByIdActeur(idActeur);
        Materiels mat = materielRepository.findByIdMateriel(idMateriel);

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
        Materiels mat = materielRepository.findByIdMateriel(idMateriel);

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

    //confirmer commande 
    public Commande confirmationCommande(String id){
        Commande commande = commandeRepository.findByIdCommande(id);

        commande.setStatutConfirmation(true);

        return commandeRepository.save(commande);
        
    }

    public List<Commande> getAllCommandes(){
        List<Commande> commandeList = commandeRepository.findAll();

        if(commandeList.isEmpty())
             throw new EntityNotFoundException("Liste commande vide");

             commandeList = commandeList
        .stream().sorted((u1,u2) -> u2.getDateCommande().compareTo(u1.getDateCommande()))
        .collect(Collectors.toList());

        return commandeList;
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

    public List<Commande> getAllCommandeByActeurProprietaire(String acteurProprietaire) {
        // Récupérer tout les commandes de l'acteur proprietaire depuis la base de données
        List<Commande> commande = commandeRepository.findByActeurProprietaireIdActeur(acteurProprietaire);

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