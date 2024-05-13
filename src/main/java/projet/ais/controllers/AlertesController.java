package projet.ais.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import projet.ais.models.Alertes;
import projet.ais.repository.AlerteRepository;
import projet.ais.repository.AlertesRepository;
import projet.ais.services.AlertesService;
import projet.ais.services.FileUploade;
import org.springframework.http.MediaType;
import java.io.IOException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("api-koumi/alertes")
public class AlertesController {
    
    @Autowired
    AlertesService alertesService;
    @Autowired
    FileUploade fileUploade;
    @Autowired
    AlertesRepository alertesRepository;
    

    @PostMapping("/create")
    @Operation(summary = "Ajout d'un alerte")
    public ResponseEntity<Alertes> createConeil(
            @Valid @RequestParam("alerte") String alerteString,
            @RequestParam(value = "audio", required = false) MultipartFile audio,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            @RequestParam(value = "video", required = false) MultipartFile video)
            throws Exception {
                
                Alertes alerte = new Alertes();
                try {
                    alerte = new JsonMapper().readValue(alerteString, Alertes.class);
                } catch (JsonProcessingException e) {
                    throw new Exception(e.getMessage());
                }
            
                // je le cree et le sauvegarde.
                Alertes savedalerte = alertesService.createAlertes(alerte, imageFile, audio, video);
            
                return new ResponseEntity<>(savedalerte, HttpStatus.CREATED);
            }

            @GetMapping("/{alerteId}/video")
    public ResponseEntity<byte[]> getVideo(@PathVariable String alerteId) {
        try {
            
            Alertes alertes = alertesRepository.findByIdAlerte(alerteId);
            if (alertes == null || alertes.getVideoAlerte() == null) {
                return ResponseEntity.notFound().build();
            }
            String videoName = alertes.getVideoAlerte(); // Example video name

            // Retrieve the video from the FTP server
            byte[] videoBytes = fileUploade.getVideoByName(videoName);

            // Detect the content type of the video based on its extension
            MediaType contentType = MediaType.valueOf("video/mp4");

            // Return the video with appropriate content type
            return ResponseEntity.ok()
                    .contentType(contentType)
                    .body(videoBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{alerteId}/audio")
    public ResponseEntity<byte[]> getAudio(@PathVariable String alerteId) {
        try {
            
            Alertes alertes = alertesRepository.findByIdAlerte(alerteId);
            if (alertes == null || alertes.getAudioAlerte() == null) {
                return ResponseEntity.notFound().build();
            }
            String audioName =  alertes.getAudioAlerte(); // Example video name

            // Retrieve the video from the FTP server
            byte[] audioBytes = fileUploade.getAudioByName(audioName);

            // Detect the content type of the video based on its extension
            MediaType contentType = MediaType.valueOf("audio/mpeg");

            // Return the video with appropriate content type
            return ResponseEntity.ok()
                    .contentType(contentType)
                    .body(audioBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

            @GetMapping("/{alerteId}/image")
            public ResponseEntity<byte[]> getImage(@PathVariable String alerteId) {
                try {
                    // Récupérer le nom de l'image associée au véhicule
                    Alertes alertes = alertesRepository.findByIdAlerte(alerteId);
                    if (alertes == null || alertes.getPhotoAlerte() == null) {
                        return ResponseEntity.notFound().build();
                    }
            
                    String imageName = alertes.getPhotoAlerte();
            
                    // Récupérer l'image à partir du serveur FTP
                    byte[] imageBytes = fileUploade.getImageByName(imageName);
            
                    // Détecter le type de contenu de l'image en fonction de son extension
                MediaType contentType = detectContentType(imageName);
            
                // Retourner l'image avec le type de contenu approprié
                return ResponseEntity.ok()
                        .contentType(contentType)
                        .body(imageBytes);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
            }
            
            private MediaType detectContentType(String imageName) {
                String[] parts = imageName.split("\\.");
                if (parts.length > 1) {
                    String extension = parts[parts.length - 1].toLowerCase();
                    switch (extension) {
                        case "jpg":
                        case "jpeg":
                            return MediaType.IMAGE_JPEG;
                        case "png":
                            return MediaType.IMAGE_PNG;
                        case "gif":
                            return MediaType.IMAGE_GIF;
                        // Ajoutez d'autres cas pour les types de contenu supplémentaires si nécessaire
                        default:
                            break;
                    }
                }
                // Par défaut, retourner MediaType.APPLICATION_OCTET_STREAM
                return MediaType.APPLICATION_OCTET_STREAM;
            }

             @PutMapping("/update/{id}")
      @Operation(summary = "Mise à jour d'un alerte ")
      public ResponseEntity<Alertes> updatealerte(
              @PathVariable String id,
              @Valid @RequestParam("alerte") String alerteString,
              @RequestParam(value = "image", required = false)  MultipartFile imageFile,
              @RequestParam(value = "audio", required = false)  MultipartFile audio,
              @RequestParam(value = "video", required = false)  MultipartFile video
              ){
                Alertes alerte = new Alertes();
          try {
               alerte = new JsonMapper().readValue(alerteString, Alertes.class);
          } catch (JsonProcessingException e) {
              return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
          }

          try {
            Alertes alerteMisAjour = alertesService.updateAlertes(alerte, imageFile, audio, video ,id);
            return new ResponseEntity<>(alerteMisAjour, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
  
      }


    //      //liste alerte
    // @GetMapping("/listealerteByActeur/{id}")
    // @Operation(summary = "affichage de la liste des alerte par acteur")
    // public ResponseEntity<List<Alertes>> listealerteByActeur(@PathVariable String id){
    //     return  new ResponseEntity<>(alertesService.getAllalerteByActeur(id), HttpStatus.OK);
    // }

                 // Get Liste des  alerte
      @GetMapping("/read")
      @Operation(summary = "Liste globale des alertes")
    public ResponseEntity<List<Alertes>> getAllVehicule() {
        return new ResponseEntity<>(alertesService.getAllAlertes(), HttpStatus.OK);
    }

    
    //Desactiver un alerte methode
    @PutMapping("/disable/{id}")
    @Operation(summary = "Désactiver un alerte ")
    public ResponseEntity <String> disablealerte(@PathVariable String id) throws Exception{
    
        alertesService.desactive(id);
        return new ResponseEntity<>("alerte desactiver avec succes", HttpStatus.ACCEPTED);
    }

    //Desactiver un alerte methode
    @PutMapping("/enable/{id}")
    @Operation(summary = "Activer un alerte ")
    public ResponseEntity <String> enableVehicule(@PathVariable String id) throws Exception{
    
        alertesService.active(id);
        return new ResponseEntity<>("alerte activer avec succes", HttpStatus.ACCEPTED);
    }


             //Supprimer un alerte
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression d'un alerte")
    public ResponseEntity<String> deletealerte(@PathVariable String id){
        return new ResponseEntity<>(alertesService.deleteAlertes(id), HttpStatus.OK);
    }
}
