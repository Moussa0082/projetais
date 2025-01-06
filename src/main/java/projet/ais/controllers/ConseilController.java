package projet.ais.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
// import projet.ais.models.conseil;
import projet.ais.models.Commande;
import projet.ais.models.Conseil;
import projet.ais.repository.ConseilRepository;
import projet.ais.services.ConseilService;
import projet.ais.services.FileUploade;
import projet.ais.services.UploadeAlerte;

import org.springframework.http.MediaType;
import java.io.IOException;

@RestController
// @CrossOrigin(origins = "*")
@RequestMapping("api-koumi/conseil")
public class ConseilController {


    @Autowired
    private ConseilService conseilService;
    // @Autowired
    // FileUploade fileUploade;
     @Autowired
    UploadeAlerte uploadeAlerte;
    @Autowired
    ConseilRepository conseilRepository;

    @PostMapping("/create")
    @Operation(summary = "Ajout d'un conseil")
     public ResponseEntity<Conseil> createConeil(
            @Valid @RequestParam("conseil") String conseilString,
            @RequestParam(value = "audio", required = false) MultipartFile audio,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            @RequestParam(value = "video", required = false) MultipartFile video)
            throws Exception {
                
                Conseil conseil = new Conseil();
                try {
                    conseil = new JsonMapper().readValue(conseilString, Conseil.class);
                } catch (JsonProcessingException e) {
                    throw new Exception(e.getMessage());
                }
            
                // je le cree et le sauvegarde.
                Conseil savedConseil = conseilService.createConseil(conseil, imageFile, audio, video);
            
                return new ResponseEntity<>(savedConseil, HttpStatus.CREATED);
            }

            // @GetMapping("/{conseilId}/video")
            // public ResponseEntity<byte[]> getVideo(@PathVariable String conseilId) {
            //     try {
                    
            //         Conseil conseil = conseilRepository.findByIdConseil(conseilId);
            //         if (conseil == null || conseil.getVideoConseil() == null) {
            //             return ResponseEntity.notFound().build();
            //         }
            //         String videoName = conseil.getVideoConseil(); // Example video name
        
            //         // Retrieve the video from the FTP server
            //         byte[] videoBytes = fileUploade.getVideoByName(videoName);
        
            //         // Detect the content type of the video based on its extension
            //         MediaType contentType = MediaType.valueOf("video/mp4");
        
            //         // Return the video with appropriate content type
            //         return ResponseEntity.ok()
            //                 .contentType(contentType)
            //                 .body(videoBytes);
            //     } catch (IOException e) {
            //         e.printStackTrace();
            //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            //     }
            // }

            @GetMapping("/{conseilId}/video")
            public ResponseEntity<byte[]> getVideo(@PathVariable String conseilId) {
                try {
                    Conseil conseil = conseilRepository.findByIdConseil(conseilId);
                    if (conseil == null || conseil.getVideoConseil() == null) {
                        return ResponseEntity.notFound().build();
                    }
        
                    String videoName = conseil.getVideoConseil();
                    byte[] videoBytes = uploadeAlerte.getVideoByName(videoName);
        
                    // Determine the content type based on file extension
                    MediaType contentType = determineContentType(videoName);
        
                    return ResponseEntity.ok()
                            .contentType(contentType)
                            .body(videoBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
                }
            }
        
            private MediaType determineContentType(String fileName) {
                String lowerCaseFileName = fileName.toLowerCase();
                if (lowerCaseFileName.endsWith(".mp4")) {
                    return MediaType.valueOf("video/mp4");
                } else if (lowerCaseFileName.endsWith(".avi")) {
                    return MediaType.valueOf("video/x-msvideo");
                } else if (lowerCaseFileName.endsWith(".mkv")) {
                    return MediaType.valueOf("video/x-matroska");
                }
                // Add other video formats if needed
                return MediaType.APPLICATION_OCTET_STREAM;
            }
        
            
            // @GetMapping("/{conseilId}/audio")
            // public ResponseEntity<byte[]> getAudio(@PathVariable String conseilId) {
            //     try {
                    
                    
            //         Conseil conseil = conseilRepository.findByIdConseil(conseilId);
            //         if (conseil == null || conseil.getAudioConseil() == null) {
            //             return ResponseEntity.notFound().build();
            //         }

            //         String audioName =  conseil.getAudioConseil(); // Example video name
        
            //         // Retrieve the video from the FTP server
            //         byte[] audioBytes = fileUploade.getAudioByName(audioName);
        
            //         // Detect the content type of the video based on its extension
            //         MediaType contentType = MediaType.valueOf("audio/mpeg");
        
            //         // Return the video with appropriate content type
            //         return ResponseEntity.ok()
            //                 .contentType(contentType)
            //                 .body(audioBytes);
            //     } catch (IOException e) {
            //         e.printStackTrace();
            //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            //     }
            // }
        
              @GetMapping("/{conseilId}/audio")
public ResponseEntity<byte[]> getAudio(@PathVariable String conseilId) {
    try {
        Conseil conseil = conseilRepository.findByIdConseil(conseilId);
        if (conseil == null || conseil.getAudioConseil() == null) {
            return ResponseEntity.notFound().build();
        }

        String audioName = conseil.getAudioConseil();
        byte[] audioBytes = uploadeAlerte.getAudioByName(audioName);

        // Determine the content type based on file extension
        MediaType contentType = determineContentTypeAudio(audioName);

        return ResponseEntity.ok()
                .contentType(contentType)
                .body(audioBytes);
    } catch (IOException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}

private MediaType determineContentTypeAudio(String fileName) {
    String lowerCaseFileName = fileName.toLowerCase();
    if (lowerCaseFileName.endsWith(".mp3")) {
        return MediaType.valueOf("audio/mpeg");
    } else if (lowerCaseFileName.endsWith(".wav")) {
        return MediaType.valueOf("audio/wav");
    } else if (lowerCaseFileName.endsWith(".ogg")) {
        return MediaType.valueOf("audio/ogg");
    }
    // Add other audio formats if needed
    return MediaType.APPLICATION_OCTET_STREAM;
}
                    @GetMapping("/{conseilId}/image")
                    public ResponseEntity<byte[]> getImage(@PathVariable String conseilId) {
                        try {
                            // Récupérer le nom de l'image associée au véhicule
                            Conseil conseil = conseilRepository.findByIdConseil(conseilId);
                            if (conseil == null || conseil.getPhotoConseil() == null) {
                                return ResponseEntity.notFound().build();
                            }
                            String imageName = conseil.getPhotoConseil() ;
                    
                            // Récupérer l'image à partir du serveur FTP
                            byte[] imageBytes = uploadeAlerte.getImageByName(imageName);
                    
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
      @Operation(summary = "Mise à jour d'un conseil ")
      public ResponseEntity<Conseil> updateConseil(
              @PathVariable String id,
              @Valid @RequestParam("conseil") String conseilString,
              @RequestParam(value = "image", required = false)  MultipartFile imageFile,
              @RequestParam(value = "audio", required = false)  MultipartFile audio,
              @RequestParam(value = "video", required = false)  MultipartFile video
              ){
              Conseil conseil = new Conseil();
          try {
               conseil = new JsonMapper().readValue(conseilString, Conseil.class);
          } catch (JsonProcessingException e) {
              return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
          }

          try {
            Conseil conseilMisAjour = conseilService.updateConseil(conseil, imageFile, audio, video ,id);
            return new ResponseEntity<>(conseilMisAjour, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
  
      }


       @GetMapping("/getAllConseilsWithPagination")
    public ResponseEntity<Page<Conseil>> getConseils(@RequestParam() int page,
                                                  @RequestParam() int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Conseil> conseils = conseilService.getAllConseilPageable(pageable);
        return ResponseEntity.ok().body(conseils);
    }

         //liste conseil
    @GetMapping("/listeConseilByActeur/{id}")
    @Operation(summary = "affichage de la liste des conseil par acteur")
    public ResponseEntity<List<Conseil>> listeConseilByActeur(@PathVariable String id){
        return  new ResponseEntity<>(conseilService.getAllConseilByActeur(id), HttpStatus.OK);
    }

                 // Get Liste des  conseil
      @GetMapping("/read")
      @Operation(summary = "Liste globale des conseils")
    public ResponseEntity<List<Conseil>> getAllVehicule() {
        return new ResponseEntity<>(conseilService.getAllConseil(), HttpStatus.OK);
    }


    //Desactiver un conseil methode
    @PutMapping("/disable/{id}")
    @Operation(summary = "Désactiver un conseil ")
    public ResponseEntity <String> disableConseil(@PathVariable String id) throws Exception{
    
        conseilService.desactive(id);
        return new ResponseEntity<>("Conseil desactiver avec succes", HttpStatus.ACCEPTED);
    }

    //Desactiver un conseil methode
      @PutMapping("/enable/{id}")
    @Operation(summary = "Activer un conseil ")
    public ResponseEntity <String> enableConseil(@PathVariable String id) throws Exception{
    
        conseilService.active(id);
        return new ResponseEntity<>("Conseil activer avec succes", HttpStatus.ACCEPTED);
    }


             //Supprimer un conseil
           @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression d'un conseil")
    public ResponseEntity<String> deleteConseil(@PathVariable String id){
        return new ResponseEntity<>(conseilService.deleteConseil(id), HttpStatus.OK);
    }



    
}
