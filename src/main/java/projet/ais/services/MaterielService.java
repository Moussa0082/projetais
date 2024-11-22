package projet.ais.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;
import jakarta.persistence.EntityNotFoundException;
import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Abonnement;
import projet.ais.models.Acteur;
import projet.ais.models.Intrant;
import projet.ais.models.Materiels;
import projet.ais.models.Stock;
import projet.ais.repository.AbonnementRepository;
import projet.ais.repository.ActeurRepository;
import projet.ais.repository.MaterielRepository;
import java.util.*;
import java.time.LocalDateTime;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.nio.file.Path;

@Service
public class MaterielService {
    
    @Autowired
    MaterielRepository materielRepository;
    @Autowired
    IdGenerator idGenerator;
    @Autowired
    ActeurRepository acteurRepository;
    @Autowired
    CodeGenerator codeGenerator;
    @Autowired
    MessageService messageService;
    @Autowired
    FileUploade fileUploade;
    @Autowired
    HistoriqueService historiqueService;
    @Autowired
    AbonnementRepository aRepository;

    public Materiels createMateriel(Materiels materiel, MultipartFile imageFile) throws Exception{
        Acteur acteur = acteurRepository.findByIdActeur(materiel.getActeur().getIdActeur());
        
        if(acteur == null)
            throw new EntityNotFoundException("Acteur non disponible");
        
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

                    materiel.setPhotoMateriel(imageName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }
            
        String codes  = codeGenerator.genererCode();
        String idCode = idGenerator.genererCode();

        materiel.setPays((acteur.getNiveau3PaysActeur()));
        materiel.setCodeMateriel(codes);
        materiel.setIdMateriel(idCode);
        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);
        materiel.setDateAjout(formattedDateTime);
        Materiels saveMateriel = materielRepository.save(materiel);
        System.out.println("nom "+saveMateriel.getNom());
        // sendMessageToAllActeur(saveMateriel);

            // Création de l'historique
            historiqueService.createHistorique("Création" , saveMateriel.getNom() , saveMateriel.getActeur().getNomActeur(), saveMateriel.getActeur().getLocaliteActeur(),saveMateriel.getActeur().getNiveau3PaysActeur(),"Création de matériel " + saveMateriel.getNom());

        return saveMateriel;
    }

      public ResponseEntity<String> sendMessageToAllActeurWithAbonner(Materiels in) {
        Acteur ac = in.getActeur();
        Abonnement ab = aRepository.findTopByActeurIdActeurOrderByDateAjoutDesc(ac.getIdActeur());
    
        // Vérifiez si l'abonnement est actif
        if (ab != null && Boolean.TRUE.equals(ab.getStatutAbonnement())) {
            List<String> optionsList = ab.getOptions();
    
            // Pour chaque option dans l'abonnement
            for (String option : optionsList) {
                // Récupérer les acteurs par type
                List<Acteur> allActeurs = acteurRepository.findByTypeActeur_Libelle(option);
    
                // Filtrer les acteurs à notifier
                allActeurs.stream()
                    .filter(acteur -> !acteur.getIdActeur().equals(ac.getIdActeur()))
                    .forEach(acteur -> sendNotification(acteur, ac, in)); // Envoyer la notification
            }
        }
    
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    
    private void sendNotification(Acteur acteur, Acteur ac, Materiels i) {
        // Envoyer le message uniquement aux autres acteurs, pas à celui qui a ajouté le stock et pas aux transporteurs
         // Extraire les détails nécessaires du stock
    String nomProduit = i.getNom();
    int prix = i.getPrixParHeure();
    String etatMateriel = i.getEtatMateriel();
    String localisation = i.getLocalisation(); // Exemple pour extraire l'unité
    String zoneProduction = i.getPays(); // Exemple d'extraction de la localisation
    String contact = ac.getWhatsAppActeur();
    // Lien vers l'image ou la page du stock
    String lienProduit = "https://koumi.ml/api-koumi/Materiel/" + i.getIdMateriel() + "/image";
    
    // Message de notification à envoyer
    String message = String.format(
        "Bonjour M. %s,\n\n"
        + "M. %s habitant à %s vient d'ajouter un nouveau equipement :\n\n"
        + "Nom : %s\n"
        + "Prix : %d F CFA\n"
        + "Etat du matériel : %s\n"
        + "Localité : %s\n"
        + "Localisation : %s\n"
        + "Contact : %s\n\n"
        + "Lien vers le produit : %s",
        acteur.getNomActeur(),
        ac.getNomActeur(),
        ac.getAdresseActeur(),
        nomProduit,
        prix,
        etatMateriel,
        localisation,
        zoneProduction,
        contact,
        lienProduit
    );
    

    try {
        messageService.sendMessageAndSave(acteur.getWhatsAppActeur(), message, ac);
    } catch (Exception e) {
        System.err.println("Erreur lors de l'envoi de la notification : " + e.getMessage());
    }
    }

    public ResponseEntity<String> sendMessageToAllActeur(Materiels materiel) {
        List<Acteur> allActeurs = acteurRepository.findAll();
       

        // TypeActeur transporteur = typeActeurRepository.findByLibelle("Transporteur");
        // TypeActeur fournisseur = typeActeurRepository.findByLibelle("Fournisseur");
        for (Acteur acteur : allActeurs) {
            // Acteur admins = acteurRepository.findByTypeActeurLibelle("admin");
            
            // if (acteur != admins) {}
            
            // Envoyer le message uniquement aux autres acteurs, pas à celui qui a ajouté le stock et pas aux transporteurs
            String mes = "Bonjour " + acteur.getNomActeur().toUpperCase() + " Un nouveau materiel de type location vient d'être ajouté " + " Nom : " + materiel.getNom();
                try {
                    messageService.sendMessageAndSave(acteur.getWhatsAppActeur(), mes,  acteur);
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());
                }
            
        
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


     public Page<Materiels> getAllMaterielPageable(Pageable pageable) {
        return materielRepository.findAllByStatutAndActeurStatutActeurAndSpeculationIsNull(true,true,pageable);
    }

    // recuperer les materiels par  type materiel avec pagination
    public Page<Materiels> getMaterielByTypeMaterielWithPagination(String idTypeMateriel,Pageable pageable) {
        return materielRepository.findByTypeMateriel_IdTypeMateriel(idTypeMateriel, pageable);
    }

    // recuperer les magasins par  acteur avec pagination
    public Page<Materiels> getMaterielByActeurWithPagination(String idActeur,Pageable pageable) {
        return materielRepository.findByActeur_IdActeur(idActeur, pageable);
    }


    public Page<Materiels> getMaterielByPaysWithPagination(String nomPays,Pageable pageable) {
        return materielRepository.findAllByStatutTrueAndPaysAndActeurStatutActeurTrueAndSpeculationIsNull(nomPays,pageable);
    }
    public Page<Materiels> getAllMaterielPageableByPays(String pays, Pageable pageable) {
       
        String paysNormalise = pays.trim().toLowerCase();
        
        // Récupérer les stocks pour le pays spécifié
        Page<Materiels> materielByPays = materielRepository.findAllByStatutTrueAndPaysAndActeurStatutActeurTrueAndSpeculationIsNull(paysNormalise, pageable);
        
        List<Materiels> materielList = new ArrayList<>(materielByPays.getContent());
        long totalElements = materielByPays.getTotalElements();
    
        // Si le nombre de stocks est inférieur à la taille de la page, compléter avec des stocks d'autres pays
        if (materielList.size() < pageable.getPageSize()) {
            Pageable complementPageable = PageRequest.of(0, pageable.getPageSize() - materielList.size());
            Page<Materiels> stocksComplement =  materielRepository.findAllByStatutTrueAndActeurStatutActeurTrueAndPaysNotAndSpeculationIsNull(paysNormalise, complementPageable);
            materielList.addAll(stocksComplement.getContent());
            totalElements += stocksComplement.getTotalElements();
        }
    
        // Créer et retourner une nouvelle page avec la liste complète des stocks et le pageable original
        return new PageImpl<>(materielList, pageable, totalElements);
    }

    public Page<Materiels> getMatByPaysWithPagination(String nomPays,Pageable pageable) {
        return  materielRepository.findAllByStatutTrueAndPaysAndActeurStatutActeurTrueAndSpeculationIsNull(nomPays, pageable);
    }


    public Page<Materiels> getEquipementByPaysWithPagination(String libelleFiliere, String nomPays,Pageable pageable) {
        return materielRepository.findBySpeculation_CategorieProduit_Filiere_LibelleFiliereAndPays(
            libelleFiliere, nomPays, pageable);
    }

    //get materiel par filiere    
public Page<Materiels> getAllMaterielByLibelleFiliere(String libelleFiliere,String pays, Pageable pageable) {
       
    String paysNormalise = pays.trim().toLowerCase();
    
    // Récupérer les stocks pour le pays spécifié
    Page<Materiels> materielByPays = materielRepository.findBySpeculation_CategorieProduit_Filiere_LibelleFiliereAndPays(
        libelleFiliere, paysNormalise, pageable);

    List<Materiels> materielList = new ArrayList<>(materielByPays.getContent());
    long totalElements = materielByPays.getTotalElements();

    // Si le nombre de stocks est inférieur à la taille de la page, compléter avec des stocks d'autres pays
    if (materielList.size() < pageable.getPageSize()) {
        Pageable complementPageable = PageRequest.of(0, pageable.getPageSize() - materielList.size());
        Page<Materiels> stocksComplement = materielRepository.findAllBySpeculation_CategorieProduit_Filiere_LibelleFiliereAndPaysNot(
            libelleFiliere, paysNormalise, complementPageable);
        materielList.addAll(stocksComplement.getContent());
        totalElements += stocksComplement.getTotalElements();
    }

    // Créer et retourner une nouvelle page avec la liste complète des stocks et le pageable original
    return new PageImpl<>(materielList, pageable, totalElements);
}


    @Transactional
    public Page<Materiels> getAllMaterielPageableByPaysByCategorie(String idTypeMateriel, String niveau3PaysActeur, Pageable pageable) {
        // Fetch materiel by type materiel from the specified country
        Page<Materiels> materielByPays = materielRepository.findAllByTypeMaterielIdTypeMaterielAndStatutTrueAndPaysAndActeurStatutActeurTrue(
            idTypeMateriel, niveau3PaysActeur.trim().toLowerCase(), pageable);

        List<Materiels> materielsList = new ArrayList<>(materielByPays.getContent());

        // If no materiels are found for the specified country, fetch materiels from other countries
        if (materielsList.isEmpty()) {
            Page<Materiels> materielFromOtherCountries = materielRepository.findAllByTypeMateriel_IdTypeMaterielAndStatutTrueAndActeurStatutActeurTrue(
                idTypeMateriel, pageable);

            return new PageImpl<>(materielFromOtherCountries.getContent(), pageable, materielFromOtherCountries.getTotalElements());
        }

        // Fetch materiels from other countries if needed to fill the page
        if (materielsList.size() < pageable.getPageSize()) {
            Pageable complementPageable = PageRequest.of(0, pageable.getPageSize() - materielsList.size());
            Page<Materiels> materielComplement = materielRepository.findAllByTypeMateriel_IdTypeMaterielAndStatutTrueAndActeurStatutActeurTrueAndPaysNot(
                idTypeMateriel, niveau3PaysActeur.trim().toLowerCase(), complementPageable);
            materielsList.addAll(materielComplement.getContent());
        }

        return new PageImpl<>(materielsList, pageable, materielByPays.getTotalElements() + materielsList.size());
    }

    @Transactional
    public Page<Materiels> getAllMaterielByIdTypeMaterielAndFiliere(String idTypeMateriel, String libelleFiliere, String pays, Pageable pageable) {
        // Fetch materiel by type materiel from the specified country
        Page<Materiels> materielByPays = materielRepository.findByTypeMateriel_IdTypeMaterielAndSpeculation_CategorieProduit_Filiere_LibelleFiliereAndPays(
            idTypeMateriel, libelleFiliere, pays.trim().toLowerCase(), pageable);

        List<Materiels> materielsList = new ArrayList<>(materielByPays.getContent());

        // If no materiels are found for the specified country, fetch materiels from other countries
        if (materielsList.isEmpty()) {
            Page<Materiels> materielFromOtherCountries = materielRepository.findAllByTypeMateriel_IdTypeMaterielAndSpeculation_CategorieProduit_Filiere_LibelleFiliereAndPaysNot(
                idTypeMateriel, libelleFiliere, pays.trim().toLowerCase(), pageable
            );
            return new PageImpl<>(materielFromOtherCountries.getContent(), pageable, materielFromOtherCountries.getTotalElements());
        }

        // Fetch materiels from other countries if needed to fill the page
        if (materielsList.size() < pageable.getPageSize()) {
            Pageable complementPageable = PageRequest.of(0, pageable.getPageSize() - materielsList.size());
            Page<Materiels> materielComplement = materielRepository.findAllByTypeMateriel_IdTypeMaterielAndSpeculation_CategorieProduit_Filiere_LibelleFiliereAndPaysNot(
                idTypeMateriel, libelleFiliere, pays.trim().toLowerCase(), complementPageable
            );
            materielsList.addAll(materielComplement.getContent());
        }

        return new PageImpl<>(materielsList, pageable, materielByPays.getTotalElements() + materielsList.size());
    }


    
    @Transactional
    public void updatePaysForMateriel() {
        // Récupérer tous les stocks
        List<Materiels> materiels = materielRepository.findAll();

        // Parcourir chaque materiel
        for (Materiels materiel : materiels) {
            // Récupérer l'acteur lié au intrant
            Acteur acteur = materiel.getActeur();

            if (acteur != null) {
                // Récupérer le niveau3Pays de l'acteur lié au stock
                String niveau3Pays = acteur.getNiveau3PaysActeur();

                // Mettre à jour la colonne pays du stock
                materiel.setPays(niveau3Pays);
            } else {
                // Gérer le cas où l'acteur est null
                System.out.println("L'acteur lié au materiel ID " + materiel.getIdMateriel() + " est null.");
            }
        }
    }




    
    public Materiels updateMateriel(Materiels materiel, String id, MultipartFile imageFile) throws Exception{
        Materiels mat = materielRepository.findById(id).orElseThrow();

        mat.setDescription(materiel.getDescription());
        mat.setEtatMateriel(materiel.getEtatMateriel());
        mat.setLocalisation(materiel.getLocalisation());
        mat.setNom(materiel.getNom());
        mat.setPrixParHeure(materiel.getPrixParHeure());
        mat.setPersonneModif(materiel.getPersonneModif());

        if(materiel.getMonnaie() != null){
            mat.setMonnaie(materiel.getMonnaie());
        }
        
        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);
        mat.setDateModif(formattedDateTime);
        
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

                    mat.setPhotoMateriel(imageName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }
            
            Materiels saveMateriel = materielRepository.save(mat);
            // Création de l'historique
            historiqueService.createHistorique("Modification" , saveMateriel.getNom() , saveMateriel.getActeur().getNomActeur(), saveMateriel.getActeur().getLocaliteActeur(),saveMateriel.getActeur().getNiveau3PaysActeur(),"Modification de matériel " + saveMateriel.getNom());


        return saveMateriel;
    }

    public List<Materiels> getMateriels(){
        List<Materiels> materielList = materielRepository.findAll();

        if(materielList == null)
            throw new EntityNotFoundException("Aucune matériel trouvé");

        materielList = materielList
        .stream().sorted((m1,m2) -> m2.getNom().compareTo(m1.getNom()))
        .collect(Collectors.toList());
        return materielList;
    }

    public List<Materiels> getMaterielByActeur(String id){
        List<Materiels> materielList = materielRepository.findByActeurIdActeur(id);

        if(materielList.isEmpty())
            throw new EntityNotFoundException("Aucune matériel trouvé");

        materielList = materielList
        .stream().sorted((m1,m2) -> m2.getNom().compareTo(m1.getNom()))
        .collect(Collectors.toList());
        return materielList;
    }

    public List<Materiels> getMaterielByTypeMateriel(String id){
        List<Materiels> materielList = materielRepository.findAllByTypeMaterielIdTypeMateriel(id);

        if(materielList.isEmpty())
            throw new EntityNotFoundException("Aucune matériel trouvé");

        materielList = materielList
        .stream().sorted((m1,m2) -> m2.getNom().compareTo(m1.getNom()))
        .collect(Collectors.toList());
        return materielList;
    }

    public String deleteMateriel(String id){
        Materiels materiel = materielRepository.findById(id).orElseThrow(null);

        materielRepository.delete(materiel);

        return "Supprimé avec succèss";
    }

    public Materiels active(String id) throws Exception{
        Materiels mat = materielRepository.findById(id).orElseThrow(null);

       try {
        mat.setStatut(true);
       } catch (Exception e) {
        throw new Exception("Erreur lors de l'activation : " + e.getMessage());

       }
       return materielRepository.save(mat);
    }

      public Materiels updateNbViev(String id) throws Exception {
        Optional<Materiels> vOpt = materielRepository.findById(id);
        
        if (vOpt.isPresent()) {
            Materiels m = vOpt.get();
            int count = m.getNbreView() + 1;
            m.setNbreView(count);

            return materielRepository.save(m);
        } else {
            throw new Exception("Une erreur s'est produite");
        }
    }

    public Materiels desactive(String id) throws Exception{
        Materiels mat = materielRepository.findById(id).orElseThrow(null);

       try {
        mat.setStatut(false);
       } catch (Exception e) {
        throw new Exception("Erreur lors de la desactivation : " + e.getMessage());

       }
       return materielRepository.save(mat);
    }
}
