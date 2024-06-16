package projet.ais.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Acteur;
import projet.ais.models.Alertes;
import projet.ais.models.AlertesOffLine;
import projet.ais.models.Pays;
import projet.ais.repository.ActeurRepository;
import projet.ais.repository.AlertesOffLineRepository;
import projet.ais.repository.AlertesRepository;
import projet.ais.repository.PaysRepository;

@Service
public class AlertesOffLineService {
     @Autowired
    private AlertesOffLineRepository alertesOffLineRepository;

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

    @Autowired
    PaysRepository paysRepository;


     //Ajouter un Alertes
      public AlertesOffLine createAlertes(AlertesOffLine alertes, MultipartFile imageFile, MultipartFile audio, MultipartFile video) throws Exception {
        
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

                    alertes.setPhotoAlerteOffLine(imageName);
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

                    alertes.setAudioAlerteOffLine(audioName);
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

                    alertes.setVideoAlerteOffLine(videoName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier video : " + e.getMessage());
                }
            }

            alertes.setIdAlerteOffLine(idGenerator.genererCode());
            String codes = codeGenerator.genererCode();
            alertes.setCodeAlerteOffLine(codes);
        
            String pattern = "yyyy-MM-dd HH:mm";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(formatter);
            alertes.setDateAjout(formattedDateTime);
            AlertesOffLine savedAlertes = alertesOffLineRepository.save(alertes);        
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


     public Page<AlertesOffLine> getAllAlertesPageable(Pageable pageable) {
        return alertesOffLineRepository.findByPhotoAlerteOffLineIsNotNullAndStatutAlerteOffLine(true,pageable);
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


    public Page<AlertesOffLine> getAlertesOffByPaysForActeur(String idActeur, int page, int size) {
        Acteur acteur = acteurRepository.findById(idActeur)
            .orElseThrow(() -> new RuntimeException("Acteur non trouvé"));

        String niveau3PaysNom = acteur.getNiveau3PaysActeur().toLowerCase();

        Pageable pageable = PageRequest.of(page, size);

        // Retrieve alerts where the country matches the actor's country, photo is not null, and status matches
        return alertesOffLineRepository.findByPhotoAlerteOffLineIsNotNullAndStatutAlerteOffLineAndPays(true, niveau3PaysNom, pageable);
    }

    public Page<AlertesOffLine> getAlertesOffLineByPays(String pays, int page, int size) {
        AlertesOffLine al = alertesOffLineRepository.findByPays(pays);

        Pageable pageable = PageRequest.of(page, size);
        // Retrieve alerts where the country matches the actor's country, photo is not null, and status matches
        return alertesOffLineRepository.findByPhotoAlerteOffLineIsNotNullAndStatutAlerteOffLineAndPays(true, al.getPays(), pageable);
    }


    //  public Page<Alertes> getAllAlertesPageable(Pageable pageable) {
    //     return AlertesRepository.findAll(pageable);
    // }

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
      public AlertesOffLine updateAlertes(AlertesOffLine alertes, MultipartFile imageFile, MultipartFile audio, MultipartFile video, String id) throws Exception {
        
        
        AlertesOffLine c = alertesOffLineRepository.findByIdAlerteOffLine(alertes.getIdAlerteOffLine());
        if(c == null){

            throw new IllegalArgumentException("L'alertes offline  avec l'id " + c + " n'existe déjà");
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

                    c.setPhotoAlerteOffLine(imageName);
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

                    c.setAudioAlerteOffLine(audioName);
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

                    c.setVideoAlerteOffLine(videoName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier video : " + e.getMessage());
                }
            }

            String pattern = "yyyy-MM-dd HH:mm";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(formatter);
            c.setDateModif(formattedDateTime);
            c.setDescriptionAlerteOffLine(alertes.getDescriptionAlerteOffLine());
            c.setTitreAlerteOffLine(alertes.getTitreAlerteOffLine());
            c.setPays(alertes.getPays());
            c.setCodePays(alertes.getCodePays());
           AlertesOffLine updatedAlertes = alertesOffLineRepository.save(c);
   
         return updatedAlertes;
        
   
    }
  

     //Liste des Alertess offline
     public List<AlertesOffLine> getAllAlertesOffLine(){
        List<AlertesOffLine> AlertesList = alertesOffLineRepository.findAll();

        AlertesList = AlertesList
        .stream().sorted((v1,v2) -> v2.getDateAjout().compareTo(v1.getDateAjout()))
        .collect(Collectors.toList());

        return AlertesList;
    }
      

    public String deleteAlertesOffLine(String id){
        AlertesOffLine alertes = alertesOffLineRepository.findById(id).orElseThrow(null);

        alertesOffLineRepository.delete(alertes);
        return "Alertes OffLine supprimé avec success";
    }

    public AlertesOffLine active(String id) throws Exception{
        AlertesOffLine alertes = alertesOffLineRepository.findById(id).orElseThrow(null);

        try {
            alertes.setStatutAlerteOffLine(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation du Alertes Offline: " + e.getMessage());
        }
        return alertesOffLineRepository.save(alertes);
    }

    public AlertesOffLine desactive(String id) throws Exception{
        AlertesOffLine alertes = alertesOffLineRepository.findById(id).orElseThrow(null);

        try {
            alertes.setStatutAlerteOffLine(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la desactivation du Alertes OffLine : " + e.getMessage());
        }
        return alertesOffLineRepository.save(alertes);
    }
    
}
