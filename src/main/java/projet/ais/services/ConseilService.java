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
import projet.ais.models.Conseil;
import projet.ais.models.TypeActeur;
import projet.ais.models.Vehicule;
import projet.ais.repository.ActeurRepository;
import projet.ais.repository.ConseilRepository;

@Service
public class ConseilService {

    @Autowired
    private ConseilRepository conseilRepository;

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


     //Ajouter un conseil
      public Conseil createConseil(Conseil conseil, MultipartFile imageFile, MultipartFile audio, MultipartFile video) throws Exception {
        
        // Conseil c = conseilRepository.findByIdConseil(conseil.getIdConseil());
        // if(c != null){

        //     throw new IllegalArgumentException("Un conseil avec l'id " + c + " existe déjà");
        // }

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

                    conseil.setPhotoConseil(imageName );
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

                    conseil.setAudioConseil(audioName);
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

                    conseil.setVideoConseil(videoName );
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier video : " + e.getMessage());
                }
            }

            conseil.setIdConseil(idGenerator.genererCode());
            String codes = codeGenerator.genererCode();
            conseil.setCodeConseil(codes);
        
            String pattern = "yyyy-MM-dd HH:mm";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(formatter);
            conseil.setDateAjout(formattedDateTime);
           Conseil savedConseil = conseilRepository.save(conseil);        
           sendMessageToAdmin(savedConseil.getActeur());
         return savedConseil;
   
    }
    public ResponseEntity<String> sendMessageToAdmin(Acteur acteur) throws Exception {

        Acteur admins = acteurRepository.findByTypeActeurLibelle("admin");
    
        if (admins != null) { // Vérifiez si des administrateurs ont été trouvés
            for (TypeActeur adminType : admins.getTypeActeur()) {
                if (adminType.getLibelle().equals("Admin")) {
                    // Si un administrateur est trouvé, envoyez un e-mail
               String msg = acteur.getNomActeur().toUpperCase() + " vient d'ajouté un nouveau conseil !";
               try {
                messageService.sendMessageAndSave(admins.getWhatsAppActeur(), msg,admins);
               } catch (Exception e) {
                 throw new Exception("Erreur lors de l'envoie de message wathsapp : " +e.getMessage());
               }
                    break; // Sortez de la boucle dès qu'un administrateur est trouvé
                }
            }
        } else {
            System.out.println("Aucun administrateur trouvé"); // Gérez le cas où aucun administrateur n'est trouvé
        }
            
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }


       //Liste des conseil par acteur
    public List<Conseil> getAllConseilByActeur(String id){
        List<Conseil>  conseilList = conseilRepository.findAllByActeurIdActeur(id);

        if(conseilList.isEmpty()){
            throw new EntityNotFoundException("Aucun conseil trouvé");
        }
        conseilList = conseilList
                .stream().sorted((d1, d2) -> d2.getTitreConseil().compareTo(d1.getTitreConseil()))
                .collect(Collectors.toList());
        return conseilList;
    } 


      //Modifier conseil
      public Conseil updateConseil(Conseil conseil, MultipartFile imageFile, MultipartFile audio, MultipartFile video, String id) throws Exception {
        
        
        Conseil c = conseilRepository.findByIdConseil(conseil.getIdConseil());
        if(c == null){

            throw new IllegalArgumentException("Le conseil avec l'id " + c + " n'existe déjà");
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

                    c.setPhotoConseil(imageName );
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

                    c.setAudioConseil(audioName );
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

                    c.setVideoConseil(videoName );
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier video : " + e.getMessage());
                }
            }

            String pattern = "yyyy-MM-dd HH:mm";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(formatter);
            c.setDateModif(formattedDateTime);
            c.setDescriptionConseil(conseil.getDescriptionConseil());
            c.setTitreConseil(conseil.getTitreConseil());
           Conseil updatedConseil = conseilRepository.save(c);        
   
         return updatedConseil;
        
   
    }
  
      //Liste des conseils
       public List<Conseil> getAllConseil(){
        List<Conseil> conseilList = conseilRepository.findAll();

        conseilList = conseilList
        .stream().sorted((v1,v2) -> v2.getDateAjout().compareTo(v1.getDateAjout()))
        .collect(Collectors.toList());

        return conseilList;
    }

    public String deleteConseil(String id){
        Conseil conseil = conseilRepository.findById(id).orElseThrow(null);

        conseilRepository.delete(conseil);
        return "Conseil supprimé avec success";
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

    public Conseil active(String id) throws Exception{
        Conseil conseil = conseilRepository.findById(id).orElseThrow(null);

        try {
            conseil.setStatutConseil(true);
            sendMessageToAllActeur();
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation du conseil: " + e.getMessage());
        }
        return conseilRepository.save(conseil);
    }

    public Conseil desactive(String id) throws Exception{
        Conseil conseil = conseilRepository.findById(id).orElseThrow(null);

        try {
            conseil.setStatutConseil(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la desactivation du conseil : " + e.getMessage());
        }
        return conseilRepository.save(conseil);
    }
    
    
}
