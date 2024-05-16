package projet.ais.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import java.util.*;
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
import org.springframework.http.MediaType;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import projet.ais.models.Niveau1Pays;
import projet.ais.models.Vehicule;
import projet.ais.repository.VehiculeRepository;
import projet.ais.services.VehiculeService;

@RestController
// @CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("api-koumi/vehicule")
public class VehiculeController {


    @Autowired
    private VehiculeService vehiculeService;
    @Autowired
    VehiculeRepository vehiculeRepository;


    @PostMapping("/create")
    @Operation(summary = "création d'un vehicule")
     public ResponseEntity<Vehicule> createVehicule(
            @Valid @RequestParam("vehicule") String vehiculeString,
            @RequestParam(value = "image", required = false) MultipartFile imageFile)
            throws Exception {
                

                Vehicule vehicule = new Vehicule();
                try {
                    vehicule = new JsonMapper().readValue(vehiculeString, Vehicule.class);
                } catch (JsonProcessingException e) {
                    throw new Exception(e.getMessage());
                }
            
                // je le cree et le sauvegarde.
                Vehicule savedVehicule = vehiculeService.createVehicule(vehicule, imageFile);
            
                return new ResponseEntity<>(savedVehicule, HttpStatus.CREATED);
            }

 // Endpoint pour récupérer une image à partir de son nom
 @GetMapping("/{vehiculeId}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable String vehiculeId) {
        try {
            // Récupérer le nom de l'image associée au véhicule
            // Vehicule vehicule = vehiculeService.findByIdVehicule(vehiculeId);
            Vehicule vehicule = vehiculeRepository.findByIdVehicule(vehiculeId);
            if (vehicule == null || vehicule.getPhotoVehicule() == null) {
                return ResponseEntity.notFound().build();
            }

            String imageName = vehicule.getPhotoVehicule();

            // Récupérer l'image à partir du serveur FTP
            byte[] imageBytes = vehiculeService.getImageByName(imageName);

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
      @Operation(summary = "Mise à jour d'un vehicule ")
      public ResponseEntity<Vehicule> updateVehicule(
              @PathVariable String id,
              @Valid @RequestParam("vehicule") String vehiculeString,
              @RequestParam(value = "image", required = false)  MultipartFile imageFile){
              Vehicule vehicule = new Vehicule();
          try {
               vehicule = new JsonMapper().readValue(vehiculeString, Vehicule.class);
          } catch (JsonProcessingException e) {
              return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
          }

          try {
            Vehicule vehiculeMisAjour = vehiculeService.updateVehicule(vehicule, imageFile, id);
            return new ResponseEntity<>(vehiculeMisAjour, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
  
      }


       @GetMapping("/getAllVehiculesWithPagination")
    public ResponseEntity<Page<Vehicule>> getVehicules(@RequestParam() int page,
                                                  @RequestParam() int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Vehicule> vehicules = vehiculeService.getAllVehiculePageable(pageable);
        return ResponseEntity.ok().body(vehicules);
    }


         //liste vehicule
    @GetMapping("/listeVehiculeByActeur/{id}")
    @Operation(summary = "affichage de la liste des vehicules par acteur")
    public ResponseEntity<List<Vehicule>> listeVehiculeByActeur(@PathVariable String id){
        return  new ResponseEntity<>(vehiculeService.getAllVehiculeByActeur(id), HttpStatus.OK);
    }

    @GetMapping("/listeVehiculeByType/{id}")
    @Operation(summary = "affichage de la liste des vehicules par type")
    public ResponseEntity<List<Vehicule>> listeVehiculeByTypes(@PathVariable String id){
        return  new ResponseEntity<>(vehiculeService.getVehiculesByTypeVoiture(id), HttpStatus.OK);
    }
                 // Get Liste des  vehicules
      @GetMapping("/read")
      @Operation(summary = "Liste globale des vehicules")
    public ResponseEntity<List<Vehicule>> getAllVehicule() {
        return new ResponseEntity<>(vehiculeService.getAllVehicules(), HttpStatus.OK);
    }


    @PutMapping("/disable/{id}")
    //Desactiver un vehicule methode
    @Operation(summary = "Désactiver un vehicule ")
    public ResponseEntity <String> disableVehicule(@PathVariable String id) throws Exception{
    
        vehiculeService.desactive(id);
        return new ResponseEntity<>("Vehicule desactiver avec succes", HttpStatus.ACCEPTED);
    }

    //Aciver admin
      @PutMapping("/enable/{id}")
    //Desactiver un admin methode
    @Operation(summary = "Activer acteur ")
    public ResponseEntity <String> enableVehicule(@PathVariable String id) throws Exception{
    
        vehiculeService.active(id);
        return new ResponseEntity<>("Vehicule activer avec succes", HttpStatus.ACCEPTED);
    }


             //Supprimer un acteur
           @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression d'un vehicule")
    public ResponseEntity<String> deleteVehicule(@PathVariable String id){
        return new ResponseEntity<>(vehiculeService.deleteVehicule(id), HttpStatus.OK);
    }


    
}
