package projet.ais.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.IdGenerator;
import projet.ais.models.Acteur;
import projet.ais.models.Conseil;
import projet.ais.models.Vehicule;
import projet.ais.repository.ConseilRepository;

@Service
public class ConseilService {

    @Autowired
    private ConseilRepository conseilRepository;

     @Autowired
    private IdGenerator idGenerator;


     //Ajouter un conseil
      public Conseil createConseil(Conseil conseil, MultipartFile imageFile, MultipartFile audio, MultipartFile video) throws Exception {
        
        Conseil c = conseilRepository.findByIdConseil(conseil.getIdConseil());
        if(c != null){

            throw new IllegalArgumentException("Un conseil avec l'id " + c + " existe déjà");
        }

            // Traitement du fichier image 
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
                    conseil.setPhotoConseil(imageLocation);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }

            // Traitement du fichier audio
            if (audio != null) {
                String audioLocation = "C:\\xampp\\htdocs\\ais";
                try {
                    Path audioRootLocation = Paths.get(audioLocation);
                    if (!Files.exists(audioRootLocation)) {
                        Files.createDirectories(audioRootLocation);
                    }
    
                    String audioName = UUID.randomUUID().toString() + "_" + audio.getOriginalFilename();
                    Path imagePath = audioRootLocation.resolve(audioName);
                    Files.copy(audio.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                    conseil.setAudioConseil(audioLocation);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier audio : " + e.getMessage());
                }
            }

            // Traitement du fichier audio
            if (video != null) {
                String videoLocation = "C:\\xampp\\htdocs\\ais";
                try {
                    Path videoRootLocation = Paths.get(videoLocation);
                    if (!Files.exists(videoRootLocation)) {
                        Files.createDirectories(videoRootLocation);
                    }
    
                    String videoName = UUID.randomUUID().toString() + "_" + video.getOriginalFilename();
                    Path imagePath = videoRootLocation.resolve(videoName);
                    Files.copy(video.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                    conseil.setVideoConseil(videoLocation);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier video : " + e.getMessage());
                }
            }

            conseil.setIdConseil(idGenerator.genererCode());
           Conseil savedConseil = conseilRepository.save(conseil);        
   
         return savedConseil;
   
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
                String imageLocation = "C:\\xampp\\htdocs\\ais";
                try {
                    Path imageRootLocation = Paths.get(imageLocation);
                    if (!Files.exists(imageRootLocation)) {
                        Files.createDirectories(imageRootLocation);
                    }
    
                    String imageName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                    Path imagePath = imageRootLocation.resolve(imageName);
                    Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                    c.setPhotoConseil(imageLocation);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }

            // Traitement du fichier audio
            if (audio != null) {
                String audioLocation = "C:\\xampp\\htdocs\\ais";
                try {
                    Path audioRootLocation = Paths.get(audioLocation);
                    if (!Files.exists(audioRootLocation)) {
                        Files.createDirectories(audioRootLocation);
                    }
    
                    String audioName = UUID.randomUUID().toString() + "_" + audio.getOriginalFilename();
                    Path imagePath = audioRootLocation.resolve(audioName);
                    Files.copy(audio.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                    c.setAudioConseil(audioLocation);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier audio : " + e.getMessage());
                }
            }

            // Traitement du fichier audio
            if (video != null) {
                String videoLocation = "C:\\xampp\\htdocs\\ais";
                try {
                    Path videoRootLocation = Paths.get(videoLocation);
                    if (!Files.exists(videoRootLocation)) {
                        Files.createDirectories(videoRootLocation);
                    }
    
                    String videoName = UUID.randomUUID().toString() + "_" + video.getOriginalFilename();
                    Path imagePath = videoRootLocation.resolve(videoName);
                    Files.copy(video.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                    c.setVideoConseil(videoLocation);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier video : " + e.getMessage());
                }
            }

            c.setDateModif(LocalDateTime.now());
            c.setDescriptionConseil(conseil.getDescriptionConseil());
            c.setTitreConseil(conseil.getTitreConseil());
           Conseil updatedConseil = conseilRepository.save(c);        
   
         return updatedConseil;
        
   
    }
  
      //Liste des conseils
       public List<Conseil> getAllConseil(){
        List<Conseil> conseilList = conseilRepository.findAll();

        conseilList = conseilList
        .stream().sorted((v1,v2) -> v2.getTitreConseil().compareTo(v1.getTitreConseil()))
        .collect(Collectors.toList());

        return conseilList;
    }

    public String deleteConseil(String id){
        Conseil conseil = conseilRepository.findById(id).orElseThrow(null);

        conseilRepository.delete(conseil);
        return "Conseil supprimé avec success";
    }

    public Conseil active(String id) throws Exception{
        Conseil conseil = conseilRepository.findById(id).orElseThrow(null);

        try {
            conseil.setStatutConseil(true);
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
