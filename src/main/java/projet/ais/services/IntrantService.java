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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Acteur;
import projet.ais.models.Alerte;
import projet.ais.models.Commande;
import projet.ais.models.DetailCommande;
import projet.ais.models.Forme;
import projet.ais.models.Intrant;
import projet.ais.models.Magasin;
import projet.ais.models.Materiels;
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

    @Autowired
    FileUploade fileUploade;

    @Autowired
    HistoriqueService historiqueService;
    
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

                    intrant.setPhotoIntrant(imageName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }

            intrant.setIdIntrant(idGenerator.genererCode());
            intrant.setCodeIntrant(codeGenerator.genererCode());
            intrant.setPays(acteur.getNiveau3PaysActeur());
             String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);
        intrant.setDateAjout(formattedDateTime);
           Intrant savedIntrant = intrantRepository.save(intrant);        
        //    sendMessageToAllActeur(intrant);

        // Création de l'historique
        historiqueService.createHistorique("Création" , savedIntrant.getNomIntrant() ,savedIntrant.getActeur().getNomActeur(), savedIntrant.getActeur().getLocaliteActeur(),savedIntrant.getActeur().getNiveau3PaysActeur(),"Création d'intrant " + savedIntrant.getNomIntrant());

         return savedIntrant;
   
    }

    public ResponseEntity<String> sendMessageToAllActeur(Intrant intrant) {
        List<Acteur> allActeurs = acteurRepository.findAll();
        Acteur ac = intrant.getActeur();

        // TypeActeur transporteur = typeActeurRepository.findByLibelle("Transporteur");
        // TypeActeur fournisseur = typeActeurRepository.findByLibelle("Fournisseur");
        for (Acteur acteur : allActeurs) {
            // Acteur admins = acteurRepository.findByTypeActeurLibelle("admin");
            
            if (ac != acteur) {
                
                // Envoyer le message uniquement aux autres acteurs, pas à celui qui a ajouté le stock et pas aux transporteurs
                String mes = "Bonjour " + acteur.getNomActeur().toUpperCase() + " Un nouveau produit de type intrant vient d'être ajouté " + " Nom : " + intrant.getNomIntrant() + "\n\n Lien vers le produit est : " + "https://koumi.ml/api-koumi/intrant/"+intrant.getIdIntrant()+"/image";;
                    try {
                        messageService.sendMessageAndSave(acteur.getWhatsAppActeur(), mes,  acteur);
                    } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());
                    }

            }
            
        
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
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

      public Intrant updateNbViev(String id) throws Exception {
        Optional<Intrant> vOpt = intrantRepository.findById(id);
        
        if (vOpt.isPresent()) {
            Intrant m = vOpt.get();
            int count = m.getNbreView() + 1;
            m.setNbreView(count);

            return intrantRepository.save(m);
        } else {
            throw new Exception("Une erreur s'est produite");
        }
    }

       //Liste des intrants par speculation
    // public List<Intrant> getAllIntrantBySpeculation(String id){
    //     List<Intrant>  intrantList = intrantRepository.findAllBySpeculationIdSpeculation(id);

    //     if(intrantList.isEmpty()){
    //         throw new EntityNotFoundException("Aucun intrant trouvé");
    //     }
    //     intrantList = intrantList
    //             .stream().sorted((d1, d2) -> d2.getNomIntrant().compareTo(d1.getNomIntrant()))
    //             .collect(Collectors.toList());
    //     return intrantList;
    // } 


      // recuperer les intrants par  categorie avec pagination
    public Page<Intrant> getIntrantByCategorieWithPagination(String idCategorieProduit,Pageable pageable) {
        return intrantRepository.findByCategorieProduit_IdCategorieProduitAndStatutIntrantAndActeurStatutActeurAndQuantiteIntrantGreaterThan(idCategorieProduit, true, true, pageable,0.0);
    }

    // recuperer les intrants par  acteur avec pagination
    public Page<Intrant> getIntrantByActeurWithPagination(String idActeur,Pageable pageable) {
        return intrantRepository.findByActeur_IdActeurAndQuantiteIntrantGreaterThan(idActeur, pageable,0.0);
    }


       //Liste des intrants par categorie
    public List<Intrant> getAllIntrantByCategorie(String id){
        List<Intrant>  intrantList = intrantRepository.findAllByCategorieProduit_IdCategorieProduitAndQuantiteIntrantGreaterThan(id,0.0);

        if(intrantList.isEmpty()){
            throw new EntityNotFoundException("Aucun intrant trouvé");
        }
        intrantList = intrantList
                .stream().sorted((d1, d2) -> d2.getNomIntrant().compareTo(d1.getNomIntrant()))
                .collect(Collectors.toList());
        return intrantList;
    } 

    
    //teste libelle
    public Page<Intrant> getAllIntrantByLibelleCategorie(String libelleFiliere, Pageable pageable) {
        return intrantRepository.findAllByCategorieProduit_filiere_LibelleFiliereAndStatutIntrantAndActeurStatutActeurAndQuantiteIntrantGreaterThan(
            libelleFiliere, true,true, pageable,0.0);
    }

  ///liste intrant par libelle filiere
//     public Page<Intrant> getAllIntrantByLibelleCategorie(String libelleFiliere,String pays, Pageable pageable) {
    
//     String paysNormalise = pays.trim().toLowerCase();
    
//     // Récupérer les stocks pour le pays spécifié
//     Page<Intrant> intrantByPays = intrantRepository.findAllByCategorieProduit_filiere_LibelleFiliereAndStatutIntrantAndActeurStatutActeurAndPaysAndQuantiteIntrantGreaterThan(
//         libelleFiliere, true,true,paysNormalise, pageable,0.0);

//     List<Intrant> intrantList = new ArrayList<>(intrantByPays.getContent());
//     long totalElements = intrantByPays.getTotalElements();

//     // Si le nombre de stocks est inférieur à la taille de la page, compléter avec des stocks d'autres pays
//     if (intrantList.size() < pageable.getPageSize()) {
//         Pageable complementPageable = PageRequest.of(0, pageable.getPageSize() - intrantList.size());
//         Page<Intrant> intrantComplement = intrantRepository.findAllByCategorieProduit_filiere_LibelleFiliereAndStatutIntrantAndActeurStatutActeurAndPaysNotAndQuantiteIntrantGreaterThan(
//             libelleFiliere,true,true, pays.trim().toLowerCase(), complementPageable,0.0);
//         intrantList.addAll(intrantComplement.getContent());
//         totalElements += intrantComplement.getTotalElements();
//     }

//     // Créer et retourner une nouvelle page avec la liste complète des stocks et le pageable original
//     return new PageImpl<>(intrantList, pageable, totalElements);
// }

    public Page<Intrant> getByLibelleAndPaysWithPagination(String libelleFiliere,String nomPays,Pageable pageable) {
        return intrantRepository.findAllByCategorieProduit_filiere_LibelleFiliereAndStatutIntrantAndActeurStatutActeurAndPaysAndQuantiteIntrantGreaterThan(
            libelleFiliere, true,true,nomPays, pageable,0.0);
    }

  ///liste intrant par libelle filiere et id categorie
    public Page<Intrant> getAllIntrantByLibelleFiliereAndIdCategorie(String idCategorie , String libelleFiliere,String pays, Pageable pageable) {

    String paysNormalise = pays.trim().toLowerCase();
    
    // Récupérer les stocks pour le pays spécifié
    Page<Intrant> intrantByPays = intrantRepository.findAllByCategorieProduit_idCategorieProduitAndCategorieProduit_filiere_LibelleFiliereAndStatutIntrantAndActeurStatutActeurAndPaysAndQuantiteIntrantGreaterThan(
        idCategorie ,libelleFiliere, true,true,paysNormalise, pageable,0.0);

    List<Intrant> intrantList = new ArrayList<>(intrantByPays.getContent());
    long totalElements = intrantByPays.getTotalElements();

    // Si le nombre de stocks est inférieur à la taille de la page, compléter avec des stocks d'autres pays
    if (intrantList.size() < pageable.getPageSize()) {
        Pageable complementPageable = PageRequest.of(0, pageable.getPageSize() - intrantList.size());
        Page<Intrant> intrantComplement = intrantRepository.findAllByCategorieProduit_idCategorieProduitAndCategorieProduit_filiere_LibelleFiliereAndStatutIntrantAndActeurStatutActeurAndPaysNotAndQuantiteIntrantGreaterThan(
           idCategorie, libelleFiliere,true,true, pays.trim().toLowerCase(), complementPageable,0.0);
        intrantList.addAll(intrantComplement.getContent());
        totalElements += intrantComplement.getTotalElements();
    }

    // Créer et retourner une nouvelle page avec la liste complète des stocks et le pageable original
    return new PageImpl<>(intrantList, pageable, totalElements);
}

    // public Page<Intrant> getAllIntrantByLibelleCategorie(String libelleFiliere, Pageable pageable) {
    //     return intrantRepository.findAllByCategorieProduit_filiere_libelleFiliere(libelleFiliere, pageable);
    // }
    // public Page<Intrant> getAllIntrantByLibelleCategorie(String libelleFiliere, String pays, Pageable pageable) {
    //     return intrantRepository.findAllByCategorieProduit_filiere_LibelleFiliereAndPays(libelleFiliere, pays, pageable);
    // }

    public Page<Intrant> getAllIntrantPageable(Pageable pageable) {
        return intrantRepository.findAllByStatutIntrantAndActeurStatutActeurAndQuantiteIntrantGreaterThan(true,true,pageable,0.0);
    }    

    @Transactional
    public void updatePaysForIntrant() {
        // Récupérer tous les stocks
        List<Intrant> intrants = intrantRepository.findAll();

        // Parcourir chaque intrant
        for (Intrant intrant : intrants) {
            // Récupérer l'acteur lié au intrant
            Acteur acteur = intrant.getActeur();

            if (acteur != null) {
                // Récupérer le niveau3Pays de l'acteur lié au stock
                String niveau3Pays = acteur.getNiveau3PaysActeur();

                // Mettre à jour la colonne pays du stock
                intrant.setPays(niveau3Pays);
            } else {
                // Gérer le cas où l'acteur est null
                System.out.println("L'acteur lié a l'intrant ID " + intrant.getIdIntrant() + " est null.");
            }
        }
    }

    @Transactional
    public Page<Intrant> getAllIntrantPageableByPaysByCategorie(String idCategorieProduit, String niveau3PaysActeur, Pageable pageable) {
        // Fetch intrants from the specified country
        Page<Intrant> intrantByPays = intrantRepository.findAllByCategorieProduit_IdCategorieProduitAndStatutIntrantTrueAndPaysAndActeurStatutActeurTrueAndQuantiteIntrantGreaterThan(
            idCategorieProduit, niveau3PaysActeur.trim().toLowerCase(), pageable,0.0);

        List<Intrant> intrantsList = new ArrayList<>(intrantByPays.getContent());

        // If no intrants are found for the specified country, fetch intrants from other countries
        if (intrantsList.isEmpty()) {
            Page<Intrant> intrantFromOtherCountries = intrantRepository.findAllByCategorieProduit_IdCategorieProduitAndStatutIntrantTrueAndActeurStatutActeurTrueAndQuantiteIntrantGreaterThan(
                idCategorieProduit, pageable,0.0);

            return new PageImpl<>(intrantFromOtherCountries.getContent(), pageable, intrantFromOtherCountries.getTotalElements());
        }

        // Fetch intrants from other countries if needed to fill the page
        if (intrantsList.size() < pageable.getPageSize()) {
            Pageable complementPageable = PageRequest.of(0, pageable.getPageSize() - intrantsList.size());
            Page<Intrant> intrantComplement = intrantRepository.findAllByCategorieProduit_IdCategorieProduitAndStatutIntrantTrueAndActeurStatutActeurTrueAndPaysNotAndQuantiteIntrantGreaterThan(
                idCategorieProduit, niveau3PaysActeur.trim().toLowerCase(), complementPageable,0.0);
            intrantsList.addAll(intrantComplement.getContent());
        }

        return new PageImpl<>(intrantsList, pageable, intrantByPays.getTotalElements() + intrantsList.size());
    }


     @Transactional
    public Page<Intrant> getAllIntrantPageableByPaysByLibelleCategorie(String libelle, String niveau3PaysActeur, Pageable pageable) {
        Page<Intrant> intrantByPays = intrantRepository.findAllByCategorieProduit_filiere_libelleFiliereAndPaysAndStatutIntrantTrueAndActeurStatutActeurTrueAndQuantiteIntrantGreaterThan(niveau3PaysActeur.trim().toLowerCase(), libelle.trim().toLowerCase(), pageable,0.0);
        
        List<Intrant> intrantsList = new ArrayList<>(intrantByPays.getContent());
       
            // Si le nombre d'intrants est inférieur au nombre requis, compléter avec des intrants d'autres pays
            if (intrantsList.size() < pageable.getPageSize()) {
                Pageable complementPageable = PageRequest.of(0, pageable.getPageSize() - intrantsList.size());
                Page<Intrant> intrantComplement = intrantRepository.findAllByCategorieProduit_filiere_libelleFiliereAndStatutIntrantTrueAndActeurStatutActeurTrueAndPaysNotAndQuantiteIntrantGreaterThan(niveau3PaysActeur.trim().toLowerCase(), libelle.trim().toLowerCase(), complementPageable,0.0);
                intrantsList.addAll(intrantComplement.getContent());
            }

            return new PageImpl<>(intrantsList, pageable, intrantByPays.getTotalElements() + intrantsList.size());
    
    }



    public Page<Intrant> getAllByPaysWithPagination(String nomPays,Pageable pageable) {
        return intrantRepository.findAllByStatutIntrantTrueAndPaysAndActeurStatutActeurTrueAndQuantiteIntrantGreaterThan(nomPays, pageable,0.0);
    }

    //test get all 
    public Page<Intrant> getAllIntrantPageableByPays(Pageable pageable) {
        return intrantRepository.findAllByStatutIntrantTrueAndActeurStatutActeurTrueAndQuantiteIntrantGreaterThan(pageable,0.0);
    }

    // public Page<Intrant> getAllIntrantPageableByPays(String pays, Pageable pageable) {
    
    //     String paysNormalise = pays.trim().toLowerCase();
        
    //     // Récupérer les stocks pour le pays spécifié
    //     Page<Intrant> intrantByPays = intrantRepository.findAllByStatutIntrantTrueAndPaysAndActeurStatutActeurTrueAndQuantiteIntrantGreaterThan(paysNormalise, pageable,0.0);
        
    //     List<Intrant> intrantList = new ArrayList<>(intrantByPays.getContent());
    //     long totalElements = intrantByPays.getTotalElements();
    
    //     // Si le nombre de stocks est inférieur à la taille de la page, compléter avec des stocks d'autres pays
    //     if (intrantList.size() < pageable.getPageSize()) {
    //         Pageable complementPageable = PageRequest.of(0, pageable.getPageSize() - intrantList.size());
    //         Page<Intrant> stocksComplement = intrantRepository.findAllByStatutIntrantTrueAndActeurStatutActeurTrueAndPaysNotAndQuantiteIntrantGreaterThan(paysNormalise, complementPageable,0.0);
    //         intrantList.addAll(stocksComplement.getContent());
    //         totalElements += stocksComplement.getTotalElements();
    //     }
    
    //     // Créer et retourner une nouvelle page avec la liste complète des stocks et le pageable original
    //     return new PageImpl<>(intrantList, pageable, totalElements);
    // }

    
    @Transactional
    public void updatePaysForIntrantsss(String id) {
        // Récupérer l' intrant
        Intrant intrants = intrantRepository.findById(id).orElseThrow(null);

        // Parcourir chaque intrant
        if (intrants != null) {
            // Récupérer l'acteur lié au intrant
            Acteur acteur = intrants.getActeur();

            if (acteur != null) {
                // Récupérer le niveau3Pays de l'acteur lié au stock
                // String niveau3Pays = acteur.getNiveau3PaysActeur();

                // Mettre à jour la colonne pays du stock
                intrants.setPays("Cameroon");
            } else {
                // Gérer le cas où l'acteur est null
                System.out.println("L'acteur lié a l'intrant ID " + intrants.getIdIntrant() + " est null.");
            }
        }
    }

      //Modifier intrant
      public Intrant updateIntrant(Intrant intrant, MultipartFile imageFile , String id) throws Exception {
        
        Intrant it = intrantRepository.findByIdIntrant(id);
        if(it == null){

            throw new IllegalArgumentException("L'intrant avec l'id " + it + " n'existe pas");
        }
        

            // Traitement du fichier image siege acteur
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

                    it.setPhotoIntrant(imageName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }
            
            it.setNomIntrant(intrant.getNomIntrant());
            it.setQuantiteIntrant(intrant.getQuantiteIntrant());
            it.setDescriptionIntrant(intrant.getDescriptionIntrant());
            it.setPrixIntrant(intrant.getPrixIntrant());
            it.setDateExpiration(intrant.getDateExpiration());
            it.setUnite(intrant.getUnite());
            it.setPersonneModif(intrant.getActeur().getNomActeur());
            
            if(intrant.getCategorieProduit() != null){
                it.setCategorieProduit(intrant.getCategorieProduit());
            }

            if(intrant.getForme() != null){
                it.setForme(intrant.getForme());
            }
 
            
            if(intrant.getMonnaie() != null){
                it.setMonnaie(intrant.getMonnaie());
            }

            String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);
        it.setDateModif(formattedDateTime);
            Intrant savedIntrant = intrantRepository.save(it);        
   
         // Création de l'historique
        historiqueService.createHistorique("Modification" , savedIntrant.getNomIntrant() ,savedIntrant.getActeur().getNomActeur(), savedIntrant.getActeur().getLocaliteActeur(),savedIntrant.getActeur().getNiveau3PaysActeur(),"Modification d'intrant " + it.getNomIntrant());

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

    public Intrant updateQuantiteIntrant(String id, double nouvelleQuantite) throws Exception {
        Optional<Intrant> intrantOpt = intrantRepository.findById(id);

        if (intrantOpt.isPresent()) {
            Intrant intrant = intrantOpt.get();
            intrant.setQuantiteIntrant(nouvelleQuantite);

            // Mettre à jour la date de modification
            intrant.setDateModif(LocalDateTime.now().toString());
            intrant.setPersonneModif(intrant.getActeur().getNomActeur());
            return intrantRepository.save(intrant);
        } else {
            throw new Exception("Intrant non trouvé avec l'ID : " + id);
        }
    }

    public String deleteIntrant(String id){
        Intrant intrant = intrantRepository.findById(id).orElseThrow(null);

        intrantRepository.delete(intrant);
          // Création de l'historique
          historiqueService.createHistorique("Suppression" , intrant.getNomIntrant() ,intrant.getActeur().getNomActeur(), intrant.getActeur().getLocaliteActeur(),intrant.getActeur().getNiveau3PaysActeur(),"Suppression d'intrant " + intrant.getNomIntrant());

        return "Intrant supprimé avec success";
    }

    public Intrant active(String id) throws Exception{
        Intrant intrant = intrantRepository.findById(id).orElseThrow(null);

        try {
            intrant.setStatutIntrant(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation de l'intrant: " + e.getMessage());
        }

        Intrant intrants =  intrantRepository.save(intrant);
        // Création de l'historique
        historiqueService.createHistorique("Activation" , intrants.getNomIntrant() ,intrants.getActeur().getNomActeur(), intrants.getActeur().getLocaliteActeur(),intrants.getActeur().getNiveau3PaysActeur(),"Activation d'intrant " + intrants.getNomIntrant());

        return intrants;
    }

    public Intrant desactive(String id) throws Exception{
        Intrant intrants = intrantRepository.findById(id).orElseThrow(null);

        try {
            intrants.setStatutIntrant(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la desactivation de l'intrant : " + e.getMessage());
        }
        Intrant intrant =  intrantRepository.save(intrants);
        // Création de l'historique
        historiqueService.createHistorique("Désactivation" , intrant.getNomIntrant() ,intrant.getActeur().getNomActeur(), intrant.getActeur().getLocaliteActeur(),intrant.getActeur().getNiveau3PaysActeur(),"Désactivation d'intrant " + intrant.getNomIntrant());

        return intrant;
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
