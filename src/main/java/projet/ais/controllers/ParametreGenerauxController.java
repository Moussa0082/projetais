package projet.ais.controllers;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import projet.ais.models.ParametreGeneraux;
import projet.ais.repository.ParametreGenerauxRepository;
import projet.ais.services.FileUploade;
import projet.ais.services.ParametreGenerauxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("api-koumi/parametreGeneraux")
public class ParametreGenerauxController {



    @Autowired
    private ParametreGenerauxService parametreGenerauxService;
    @Autowired
    private ParametreGenerauxRepository parametreGenerauxRepository;
    @Autowired
    FileUploade fileUploade;
    private static final Logger logger = LoggerFactory.getLogger(ParametreGenerauxController.class);
     //Create user
          @PostMapping("/create")
    @Operation(summary = "Création d'un paramètre général")
    public ResponseEntity<String> createParametregeneraux(
            @Valid @RequestParam("parametreGeneral") String parametreGeneralString,
            @RequestParam(value = "image", required = false) MultipartFile imageFile)
            throws Exception {
        ParametreGeneraux parametreGeneraux = new ParametreGeneraux();
        try {
            parametreGeneraux = new JsonMapper().readValue(parametreGeneralString, ParametreGeneraux.class);
        } catch (JsonProcessingException e) {
            throw new Exception(e.getMessage());
        }

        ResponseEntity<String> response = parametreGenerauxService.createParametreGeneral(parametreGeneraux, imageFile);
        // Check for successful creation (HTTP status code 201)
    if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
        return new ResponseEntity<>("Paramètre général ajouté avec succès", HttpStatus.CREATED);
    } else 
    {
          return new ResponseEntity<>("Paramètre général non ajouté ", HttpStatus.BAD_REQUEST);
         // Return the original response with error information
    }
  
    }

    @GetMapping("/{paramId}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable String paramId) {
        try {
            // Récupérer le nom de l'image associée au véhicule
            ParametreGeneraux param = parametreGenerauxRepository.findByIdParametreGeneraux(paramId);
            if (param == null || param.getLogoSysteme() == null) {
                return ResponseEntity.notFound().build();
            }
    
            String imageName = param.getLogoSysteme() ;
    
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
     
     @PutMapping("/updateParam/{id}")
    @Operation(summary = "Mise à jour d'un paramètre général par son Id")
    public ResponseEntity<ParametreGeneraux> updateParametreG(
            @PathVariable String id,
            @RequestParam("parametreGeneral") String parametreGeneralString,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        ObjectMapper objectMapper = new ObjectMapper();
        ParametreGeneraux parametreGeneraux;
        try {
            parametreGeneraux = objectMapper.readValue(parametreGeneralString, ParametreGeneraux.class);
        } catch (JsonMappingException e) {
            logger.error("Erreur de mappage JSON : ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (JsonProcessingException e) {
            logger.error("Erreur de traitement JSON : ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            ParametreGeneraux parametreGeneralMisAjour = parametreGenerauxService.updateParametreGene(parametreGeneraux, id, imageFile);
            return new ResponseEntity<>(parametreGeneralMisAjour, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de ParametreGeneraux : ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Mettre à jour 
      @PutMapping("/update/{id}")
    @Operation(summary = "Mise à jour d'un paramètre général par son Id ")
    public ResponseEntity<ParametreGeneraux> updateParametreGeneraux(
            @PathVariable String id,
            @Valid @RequestParam("parametreGeneral") String parametreGeneralString,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        ParametreGeneraux parametreGeneraux = new ParametreGeneraux();
        try {
            parametreGeneraux = new JsonMapper().readValue(parametreGeneralString, ParametreGeneraux.class);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            ParametreGeneraux parametreGeneralMisAjour = parametreGenerauxService.updateParametreGeneraux(parametreGeneraux, id, imageFile);
            return new ResponseEntity<>(parametreGeneralMisAjour, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
     


         @GetMapping("/read")
     @Operation(summary = "Affichage de la  liste des paramètres généraux")
    public ResponseEntity<List<ParametreGeneraux>> getAllParametreGeneraux() throws Exception{
        return new ResponseEntity<>(parametreGenerauxService.getAllParametreGeneraux(),HttpStatus.OK);}


       
    //Lire un user spécifique
    @GetMapping("/read/{id}")
    public ResponseEntity<?> getParametreGeneralById(@PathVariable String id) {
        return parametreGenerauxService.findById(id);
    }

    

        //Supprimer un paramètre général
           @DeleteMapping("/delete/{id}")
    @Operation(summary = "Supprimer un paramètre général")
    public String delete(@Valid @PathVariable String id) {
        return parametreGenerauxService.deleteByIdParametreGeneraux(id);
    }
    
}
