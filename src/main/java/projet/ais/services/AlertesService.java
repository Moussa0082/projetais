package projet.ais.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Acteur;
import projet.ais.models.Alertes;

import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;
import java.util.*;

import projet.ais.repository.ActeurRepository;
import projet.ais.repository.AlertesRepository;

@Service
public class AlertesService {
    
      @Autowired
    private AlertesRepository AlertesRepository;

     @Autowired
    private IdGenerator idGenerator;
     @Autowired
    CodeGenerator codeGenerator;
    @Autowired
    FileUploade fileUploade;
    @Autowired
    ActeurRepository acteurRepository;
    @Autowired
    MessageService messageService;


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
                    String onlineImagePath =fileUploade.uploadImageToFTP(imagePath, imageName);

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
                    String onlineAudioPath =fileUploade.uploadAudioToFTP(audioPath, audioName);

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
                    String onlineVideoPath =fileUploade.uploadVideoToFTP(videoPath, videoName);

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
           Alertes savedAlertes = AlertesRepository.save(alertes);        
           sendMessageToAllActeur();
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
            String mes = "Bonjour une nouvelle conseil vient d'être ajouté";
                try {
                    messageService.sendMessageAndSave(acteur.getWhatsAppActeur(), mes,  acteur);
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());
                }
            }
        
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    //    //Liste des Alertes par acteur
    // public List<Alertes> getAllAlertesByActeur(String id){
    //     List<Alertes>  AlertesList = AlertesRepository.findAllByActeurIdActeur(id);

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
        
        
        Alertes c = AlertesRepository.findByIdAlerte(alertes.getIdAlerte());
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
                    String onlineImagePath =fileUploade.uploadImageToFTP(imagePath, imageName);

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
                    String onlineAudioPath =fileUploade.uploadAudioToFTP(audioPath, audioName);

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
                    String onlineVideoPath =fileUploade.uploadVideoToFTP(videoPath, videoName);

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
           Alertes updatedAlertes = AlertesRepository.save(c);
   
         return updatedAlertes;
        
   
    }
  
      //Liste des Alertess
       public List<Alertes> getAllAlertes(){
        List<Alertes> AlertesList = AlertesRepository.findAll();

        AlertesList = AlertesList
        .stream().sorted((v1,v2) -> v2.getDateAjout().compareTo(v1.getDateAjout()))
        .collect(Collectors.toList());

        return AlertesList;
    }

    public String deleteAlertes(String id){
        Alertes alertes = AlertesRepository.findById(id).orElseThrow(null);

        AlertesRepository.delete(alertes);
        return "Alertes supprimé avec success";
    }

    public Alertes active(String id) throws Exception{
        Alertes alertes = AlertesRepository.findById(id).orElseThrow(null);

        try {
            alertes.setStatutAlerte(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation du Alertes: " + e.getMessage());
        }
        return AlertesRepository.save(alertes);
    }

    public Alertes desactive(String id) throws Exception{
        Alertes alertes = AlertesRepository.findById(id).orElseThrow(null);

        try {
            alertes.setStatutAlerte(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la desactivation du Alertes : " + e.getMessage());
        }
        return AlertesRepository.save(alertes);
    }
    
    
}
