package projet.ais.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.data.domain.Sort;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;


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
import projet.ais.Exception.NoAlertsFoundException;
import projet.ais.models.Acteur;
import projet.ais.models.Alertes;
import projet.ais.models.AlertesOffLine;
import projet.ais.models.Pays;

import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;
import java.util.*;

import projet.ais.repository.ActeurRepository;
import projet.ais.repository.AlertesRepository;
import projet.ais.repository.PaysRepository;

@Service
public class AlertesService {
    
    @Autowired
    private AlertesRepository alertesRepository;

    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    CodeGenerator codeGenerator;
    // @Autowired
    // FileUploade fileUploade;

    @Autowired
    UploadeAlerte uploadeAlerte;

    @Autowired
    ActeurRepository acteurRepository;
    @Autowired
    MessageService messageService;

    @Autowired
    PaysRepository paysRepository;


     //Ajouter un Alertes
      public Alertes createAlertes(Alertes alertes, MultipartFile imageFile, MultipartFile audio, MultipartFile video) throws Exception {
        
            // Traitement du fichier image 
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
                    String onlineImagePath =uploadeAlerte.uploadImageToFTP(imagePath, imageName);

                    alertes.setPhotoAlerte(imageName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }

            // Traitement du fichier audio
            if (audio != null) {
                String audioLocation = "/ais";
                try {
                    Path audioRootLocation = Paths.get(audioLocation);
                    if (!Files.exists(audioRootLocation)) {
                        Files.createDirectories(audioRootLocation);
                    }
    
                    String audioName = UUID.randomUUID().toString() + "_" + audio.getOriginalFilename();
                    Path audioPath = audioRootLocation.resolve(audioName);
                    Files.copy(audio.getInputStream(), audioPath, StandardCopyOption.REPLACE_EXISTING);
                    String onlineAudioPath =uploadeAlerte.uploadAudioToFTP(audioPath, audioName);

                    alertes.setAudioAlerte(audioName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier audio : " + e.getMessage());
                }
            }

            // Traitement du fichier audio
            if (video != null) {
                String videoLocation = "ais";
                try {
                    Path videoRootLocation = Paths.get(videoLocation);
                    if (!Files.exists(videoRootLocation)) {
                        Files.createDirectories(videoRootLocation);
                    }
    
                    String videoName = UUID.randomUUID().toString() + "_" + video.getOriginalFilename();
                    Path videoPath = videoRootLocation.resolve(videoName);
                    Files.copy(video.getInputStream(), videoPath, StandardCopyOption.REPLACE_EXISTING);
                    String onlineVideoPath =uploadeAlerte.uploadVideoToFTP(videoPath, videoName);

                    alertes.setVideoAlerte(videoName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier video : " + e.getMessage());
                }
            }

            alertes.setIdAlerte(idGenerator.genererCode());
            String codes = codeGenerator.genererCode();
            alertes.setCodeAlerte(codes);
        
            String pattern = "yyyy-MM-dd HH:mm";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(formatter);
            alertes.setDateAjout(formattedDateTime);
           Alertes savedAlertes = alertesRepository.save(alertes);        
        //    sendMessageToAllActeur();
         return savedAlertes;
   
    }


    public ResponseEntity<String> sendMessageToAllActeur() {
        List<Acteur> allActeurs = acteurRepository.findAll();
       

        // TypeActeur transporteur = typeActeurRepository.findByLibelle("Transporteur");
        // TypeActeur fournisseur = typeActeurRepository.findByLibelle("Fournisseur");
        for (Acteur acteur : allActeurs) {
            Acteur admins = acteurRepository.findByTypeActeurLibelle("admin");
            
            if (acteur != admins) {
            
            // Envoyer le message uniquement aux autres acteurs, pas à celui qui a ajouté le stock et pas aux transporteurs
            String mes = "Bonjour une nouvelle alerte vient d'être ajouté";
                try {
                    messageService.sendMessageAndSave(acteur.getWhatsAppActeur(), mes,  acteur);
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());
                }
            }
        
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


     public Page<Alertes> getAllAlertesPageable(Pageable pageable) {
        return alertesRepository.findByPhotoAlerteIsNotNullAndStatutAlerte(true,pageable);
    }

    //   public Page<Alertes> getAlertesByPaysForActeur(String idActeur, Alertes al, int page, int size) {
    //     Acteur acteur = acteurRepository.findById(idActeur)
    //         .orElseThrow(() -> new RuntimeException("Acteur non trouvé"));

    //     String niveau3PaysNom = acteur.getNiveau3PaysActeur().toLowerCase();

    //     // Create a pageable object
    //     Pageable pageable = PageRequest.of(page, size);

    //     // Get the paginated list of countries
    //     Page<Pays> paysPage = paysRepository.findAll(pageable);

    //     // Find the country that matches the actor's country
    //     Pays paysCorrespondant = paysPage.get()
    //         .filter(pays -> pays.getNomPays().toLowerCase().equals(niveau3PaysNom))
    //         .findFirst()
    //         .orElseThrow(() -> new RuntimeException("Pays correspondant non trouvé"));

    //     // Check if the country's name matches the alert's country
    //     if (paysCorrespondant.getNomPays().toLowerCase().equals(al.getPays().toLowerCase())) {
    //         return new PageImpl<>(List.of(al), pageable, 1);
    //     }

    //     return Page.empty(pageable);
    // }


    public Page<Alertes> getAlertesByPaysForActeur(String idActeur, int page, int size) {
        Acteur acteur = acteurRepository.findById(idActeur)
            .orElseThrow(() -> new RuntimeException("Acteur non trouvé"));

        String niveau3PaysNom = acteur.getNiveau3PaysActeur().toLowerCase();

        Pageable pageable = PageRequest.of(page, size);

        // Retrieve alerts where the country matches the actor's country, photo is not null, and status matches
        return alertesRepository.findByPhotoAlerteIsNotNullAndStatutAlerteAndPays(true, niveau3PaysNom, pageable);
    }


    //  public Page<Alertes> getAllAlertesPageable(Pageable pageable) {
    //     return alertesRepository.findAll(pageable);
    // }

    //    //Liste des Alertes par acteur
    // public List<Alertes> getAllAlertesByActeur(String id){
    //     List<Alertes>  AlertesList = alertesRepository.findAllByActeurIdActeur(id);

    //     if(AlertesList.isEmpty()){
    //         throw new EntityNotFoundException("Aucun Alertes trouvé");
    //     }
    //     AlertesList = AlertesList
    //             .stream().sorted((d1, d2) -> d2.getTitreAlertes().compareTo(d1.getTitreAlertes()))
    //             .collect(Collectors.toList());
    //     return AlertesList;
    // } 


      //Modifier Alertes
      public Alertes updateAlertes(Alertes alertes, MultipartFile imageFile, MultipartFile audio, MultipartFile video, String id) throws Exception {
        
        
        Alertes c = alertesRepository.findByIdAlerte(alertes.getIdAlerte());
        if(c == null){

            throw new IllegalArgumentException("Le Alertes avec l'id " + c + " n'existe déjà");
        }

            // Traitement du fichier image 
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
                    String onlineImagePath =uploadeAlerte.uploadImageToFTP(imagePath, imageName);

                    c.setPhotoAlerte(imageName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }

            // Traitement du fichier audio
            if (audio != null) {
                String audioLocation = "/ais";
                try {
                    Path audioRootLocation = Paths.get(audioLocation);
                    if (!Files.exists(audioRootLocation)) {
                        Files.createDirectories(audioRootLocation);
                    }
    
                    String audioName = UUID.randomUUID().toString() + "_" + audio.getOriginalFilename();
                    Path audioPath = audioRootLocation.resolve(audioName);
                    Files.copy(audio.getInputStream(), audioPath, StandardCopyOption.REPLACE_EXISTING);
                    String onlineAudioPath =uploadeAlerte.uploadAudioToFTP(audioPath, audioName);

                    c.setAudioAlerte(audioName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier audio : " + e.getMessage());
                }
            }

            // Traitement du fichier audio
            if (video != null) {
                String videoLocation = "/ais";
                try {
                    Path videoRootLocation = Paths.get(videoLocation);
                    if (!Files.exists(videoRootLocation)) {
                        Files.createDirectories(videoRootLocation);
                    }
    
                    String videoName = UUID.randomUUID().toString() + "_" + video.getOriginalFilename();
                    Path videoPath = videoRootLocation.resolve(videoName);
                    Files.copy(video.getInputStream(), videoPath, StandardCopyOption.REPLACE_EXISTING);
                    String onlineVideoPath =uploadeAlerte.uploadVideoToFTP(videoPath, videoName);

                    c.setVideoAlerte(videoName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier video : " + e.getMessage());
                }
            }

            String pattern = "yyyy-MM-dd HH:mm";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(formatter);
            c.setDateModif(formattedDateTime);
            c.setDescriptionAlerte(alertes.getDescriptionAlerte());
            c.setTitreAlerte(alertes.getTitreAlerte());
            c.setPays(alertes.getPays());
            c.setCodePays(alertes.getCodePays());
           Alertes updatedAlertes = alertesRepository.save(c);
   
         return updatedAlertes;
        
   
    }

     public Page<Alertes> getAlertesByPaysSortedByDate(String pays, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateAjout"));
        Page<Alertes> alertesPage = alertesRepository.findByPhotoAlerteIsNotNullAndStatutAlerteAndPays(true, pays, pageable);

        if (alertesPage.isEmpty()) {
            throw new NoAlertsFoundException("Aucune alerte trouvée pour le pays: " + pays);
        }

        return alertesPage;
    }
  
      //Liste des Alertess
       public List<Alertes> getAllAlertes(){
        List<Alertes> AlertesList = alertesRepository.findAll();

        AlertesList = AlertesList
        .stream().sorted((v1,v2) -> v2.getDateAjout().compareTo(v1.getDateAjout()))
        .collect(Collectors.toList());

        return AlertesList;
    }

    public String deleteAlertes(String id){
        Alertes alertes = alertesRepository.findById(id).orElseThrow(null);

        alertesRepository.delete(alertes);
        return "Alertes supprimé avec success";
    }

    public Alertes active(String id) throws Exception{
        Alertes alertes = alertesRepository.findById(id).orElseThrow(null);

        try {
            alertes.setStatutAlerte(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation du Alertes: " + e.getMessage());
        }
        return alertesRepository.save(alertes);
    }

    public Alertes desactive(String id) throws Exception{
        Alertes alertes = alertesRepository.findById(id).orElseThrow(null);

        try {
            alertes.setStatutAlerte(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la desactivation du Alertes : " + e.getMessage());
        }
        return alertesRepository.save(alertes);
    }

//fecth alerte par pays bane 
           @Transactional
    public Page<Alertes> getAllAlertesPageableByPays(String niveau3PaysActeur, Pageable pageable) {
        
        Page<Alertes> alertesByPays = alertesRepository.findByPhotoAlerteIsNotNullAndStatutAlerteTrueAndPays( 
            niveau3PaysActeur.trim().toLowerCase(), pageable);

        List<Alertes> alertesList = new ArrayList<>(alertesByPays.getContent());

         
        if (alertesList.size() < pageable.getPageSize()) {
            Pageable complementPageable = PageRequest.of(0, pageable.getPageSize() - alertesList.size());
            Page<Alertes> alertesComplement = alertesRepository.findByPhotoAlerteIsNotNullAndStatutAlerteTrueAndPaysNot(
                 niveau3PaysActeur.trim().toLowerCase(), complementPageable);
            alertesList.addAll(alertesComplement.getContent());
        }

        return new PageImpl<>(alertesList, pageable, alertesByPays.getTotalElements() + alertesList.size());
    }

//fecth alerte par pays sy 
    @Transactional
    public Page<Alertes> getAllAlertesByPays(String pays, Pageable pageable) {
        // Utiliser la méthode du repository pour récupérer les alertes
        Page<Alertes> alertesByPays = alertesRepository.findByPaysAndStatutAlerte(
            pays.trim().toLowerCase(), true, pageable);

        // Créer une nouvelle liste contenant les alertes récupérées
        List<Alertes> alertesList = new ArrayList<>(alertesByPays.getContent());
        
        alertesList.sort(Comparator.comparing(Alertes::getDateAjout).reversed());
        // Retourner une nouvelle page avec les alertes récupérées
        return new PageImpl<>(alertesList, pageable, alertesByPays.getTotalElements());
    }

}