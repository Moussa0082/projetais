package projet.ais.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
import projet.ais.models.Intrant;
import projet.ais.models.Stock;
import projet.ais.models.Vehicule;
import projet.ais.repository.ActeurRepository;
import projet.ais.repository.AlerteRepository;
import projet.ais.repository.CommandeRepository;
import projet.ais.repository.DetailCommandeRepository;
import projet.ais.repository.IntrantRepository;

@Service
public class IntrantService {

    @Autowired
    private IntrantRepository intrantRepository;

    @Autowired
    ActeurRepository acteurRepository;

    @Autowired
    CommandeRepository commandeRepository;

    @Autowired
    AlerteRepository alerteRepository;

    @Autowired
    MessageService messageService;

    @Autowired
    EmailService emailService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    CodeGenerator codeGenerator;

     @Autowired
     DetailCommandeRepository detailCommandeRepository;



     //créer un intrant
      public Intrant createIntrant(Intrant intrant, MultipartFile imageFile) throws Exception {
        
        Intrant it = intrantRepository.findByIdIntrant(intrant.getIdIntrant());
        if(it != null){
            throw new IllegalArgumentException("Un intrant avec l'id " + it + " existe déjà");
        }

        Acteur acteur = acteurRepository.findByIdActeur(intrant.getActeur().getIdActeur());

        if(acteur == null)
            throw new EntityNotFoundException("Aucun acteur trouvé");
            
            // Traitement du fichier image siege acteur
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
                    intrant.setPhotoIntrant("ais/" + imageName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }

            intrant.setIdIntrant(idGenerator.genererCode());
            intrant.setCondeIntrant(codeGenerator.genererCode());
           Intrant savedIntrant = intrantRepository.save(intrant);        
   
         return savedIntrant;
   
    }


       //Liste des intrants par acteur
    public List<Intrant> getAllIntrantByActeur(String id){
        List<Intrant>  intrantList = intrantRepository.findAllByActeurIdActeur(id);

        if(intrantList.isEmpty()){
            throw new EntityNotFoundException("Aucun intrant trouvé");
        }
        intrantList = intrantList
                .stream().sorted((d1, d2) -> d2.getNomIntrant().compareTo(d1.getNomIntrant()))
                .collect(Collectors.toList());
        return intrantList;
    } 

    public List<Intrant> getAllIntrantBySuperficie(String id){
        List<Intrant>  intrantList = intrantRepository.findBySuperficieIdSuperficie(id);

        if(intrantList.isEmpty()){
            throw new EntityNotFoundException("Aucun intrant trouvé avec id :" +id);
        }
        intrantList = intrantList
                .stream().sorted((d1, d2) -> d2.getNomIntrant().compareTo(d1.getNomIntrant()))
                .collect(Collectors.toList());
        return intrantList;
    } 

      //Modifier intrant
      public Intrant updateIntrant(Intrant intrant, MultipartFile imageFile , String id) throws Exception {
        
        Intrant it = intrantRepository.findByIdIntrant(id);
        if(it == null){

            throw new IllegalArgumentException("L'intrant avec l'id " + it + " n'existe pas");
        }
        

            // Traitement du fichier image siege acteur
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
                    it.setPhotoIntrant("ais/" + imageName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }
            it.setNomIntrant(intrant.getNomIntrant());
            it.setQuantiteIntrant(intrant.getQuantiteIntrant());
            it.setDescriptionIntrant(intrant.getDescriptionIntrant());
            it.setDateModif(LocalDateTime.now());
            Intrant savedIntrant = intrantRepository.save(it);        
   
           return savedIntrant;
   
    }
  
      //Liste des intrant
       public List<Intrant> getAllIntrant(){
        List<Intrant> intrantList = intrantRepository.findAll();

        intrantList = intrantList
        .stream().sorted((v1,v2) -> v2.getNomIntrant().compareTo(v1.getNomIntrant()))
        .collect(Collectors.toList());

        return intrantList;
    }

    public String deleteIntrant(String id){
        Intrant intrant = intrantRepository.findById(id).orElseThrow(null);

        intrantRepository.delete(intrant);
        return "Intrant supprimé avec success";
    }

    public Intrant active(String id) throws Exception{
        Intrant intrant = intrantRepository.findById(id).orElseThrow(null);

        try {
            intrant.setStatutIntrant(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation de l'intrant: " + e.getMessage());
        }
        return intrantRepository.save(intrant);
    }

    public Intrant desactive(String id) throws Exception{
        Intrant intrant = intrantRepository.findById(id).orElseThrow(null);

        try {
            intrant.setStatutIntrant(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la desactivation de l'intrant : " + e.getMessage());
        }
        return intrantRepository.save(intrant);
    }










































 
  
          public Commande ajouterIntrantACommande(Commande commande, List<Intrant> intrant, List<Double> quantitesDemandees) throws Exception {
        // Récupération des stocks correspondant aux identifiants fournis
        List<Intrant> intrantsFound = intrantRepository.findByIdIntrantIn(
            intrant.stream().map(Intrant::getIdIntrant).collect(Collectors.toList())
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
        for (int i = 0; i < intrantsFound.size(); i++) {
            Intrant intrants = intrantsFound.get(i);
            double quantiteDemandee = quantitesDemandees.get(i);
            DetailCommande detailCommande = new DetailCommande();
    
            // Création d'une nouvelle instance de DetailCommande
            detailCommande.setIdDetailCommande(idGenerator.genererCode());
            detailCommande.setCodeProduit(intrants.getCondeIntrant());
            detailCommande.setQuantiteDemande(quantiteDemandee);
            detailCommande.setQuantiteLivree(0.0); // Initialement aucun n'a été livré
            detailCommande.setQuantiteNonLivree(0.0); // Initialement aucun n'a été livré
            detailCommande.setNomProduit(intrants.getNomIntrant());
            detailCommande.setDateAjout(formattedDateTime);
            detailCommande.setCommande(savedCommande);
    
            // Enregistrement du détail de la commande
            detailCommandeRepository.save(detailCommande);
    
            // Mise à jour de la quantité en stock
            double quantiteRestante = intrants.getQuantiteIntrant() - quantiteDemandee;
            intrants.setQuantiteIntrant(quantiteRestante);
            intrantRepository.save(intrants);
    
            // Mise à jour de la quantité demandée totale dans la commande
            savedCommande.setQuantiteDemande(savedCommande.getQuantiteDemande() + quantiteDemandee);
        }
    
        // Envoi de notifications aux propriétaires des stocks
        Map<Acteur, List<DetailCommande>> proprietairesStocksDetails = new HashMap<>();
    
        // Parcourir les stocks trouvés
        for (Intrant intrant2 : intrantsFound) {
            Acteur proprietaire = intrant2.getActeur(); // Récupérer le propriétaire du stock
    
            // Vérifier si le propriétaire est déjà présent dans la Map, sinon ajouter une nouvelle entrée
            if (!proprietairesStocksDetails.containsKey(proprietaire)) {
                proprietairesStocksDetails.put(proprietaire, new ArrayList<>());
            }
    
            // Ajouter le détail de commande à la liste des détails de commande du propriétaire
            List<DetailCommande> details = detailCommandeRepository.findByNomProduit(intrant2.getNomIntrant());
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
                Alerte al = new Alerte(proprietaire.getEmailActeur(), message, "Nouvelle commande d'intrant");
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
        .flatMap(nomIntrant -> intrantRepository.findByNomIntrant(nomIntrant).stream()) // Convertir la collection de Stock en un flux
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

        return new ResponseEntity<>("La commande a été validée avec succès, le propriétaire a été informés.", HttpStatus.OK);
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
        .flatMap(nomIntrant -> intrantRepository.findByNomIntrant(nomIntrant).stream()) // Convertir la collection de Stock en un flux
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




    
}
