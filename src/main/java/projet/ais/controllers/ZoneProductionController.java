package projet.ais.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import projet.ais.models.Niveau1Pays;
import projet.ais.models.ZoneProduction;
import projet.ais.repository.ZoneProductionRepository;
import projet.ais.services.FileUploade;
import projet.ais.services.ZoneProductionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.http.MediaType;
import java.io.IOException;



@RestController
// @CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("api-koumi/ZoneProduction")
public class ZoneProductionController {
    
    @Autowired
    ZoneProductionService  zoneProductionService;
    @Autowired
    ZoneProductionRepository zoneProductionRepository;
    @Autowired
    FileUploade fileUploade;

    @PostMapping("/addZoneProduction")
    @Operation(summary = "Création de zone de production")
    public ResponseEntity<ZoneProduction> createZone(
         @Valid @RequestParam("zone") String zoneProduction,
         @Valid @RequestParam(value = "image", required = false)  MultipartFile imageFile) throws Exception{


            ZoneProduction zoneProductions = new  ZoneProduction();
            try {
                zoneProductions = new JsonMapper().readValue(zoneProduction,ZoneProduction.class);
            }  catch (JsonProcessingException e) {
                throw new Exception(e.getMessage());
            }

            ZoneProduction saveZone = zoneProductionService.createZoneProduction(zoneProductions, imageFile);
            return new ResponseEntity<>(saveZone, HttpStatus.OK);
    }

    @PutMapping("/updateZoneProduction/{id}")
    @Operation(summary = "Modification de zone de production")
    public ResponseEntity<ZoneProduction> updatezone(
         @Valid @RequestParam("zone") String zoneProduction,
         @Valid @RequestParam(value = "image" ,required = false) MultipartFile imageFile, @PathVariable String id) throws Exception{

            ZoneProduction zoneProductions = new  ZoneProduction();
            try {
                zoneProductions = new JsonMapper().readValue(zoneProduction,ZoneProduction.class);
            }  catch (JsonProcessingException e) {
                throw new Exception(e.getMessage());
            }

            ZoneProduction updatedZone = zoneProductionService.updateZoneProduction(zoneProductions,id, imageFile);
            return new ResponseEntity<>(updatedZone, HttpStatus.OK);
        }
// Endpoint pour récupérer une image à partir de son nom
@GetMapping("/{zoneId}/image")
public ResponseEntity<byte[]> getImage(@PathVariable String zoneId) {
    try {
        // Récupérer le nom de l'image associée au véhicule
        ZoneProduction zoneProduction = zoneProductionRepository.findByidZoneProduction(zoneId);
        if (zoneProduction == null || zoneProduction.getPhotoZone() == null) {
            return ResponseEntity.notFound().build();
        }

        String imageName = zoneProduction.getPhotoZone();

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
    @PutMapping("/activer/{id}")
    @Operation(summary = "activation de la zone de production")
    public ResponseEntity<ZoneProduction> activeZone(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(zoneProductionService.active(id), HttpStatus.CREATED);
    }

    @PutMapping("/desactiver/{id}")
    @Operation(summary = "desactivation de la zone  de production" )
    public ResponseEntity<ZoneProduction> desactiveZone(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(zoneProductionService.desactive(id), HttpStatus.CREATED);
    }

        @GetMapping("/getAllZone")
        @Operation(summary = "Liste des zones de production")
        public ResponseEntity<List<ZoneProduction>> getAllZones() {
            return new ResponseEntity<>(zoneProductionService.getZoneProduction(), HttpStatus.OK);
        }


        @GetMapping("/getAllZoneProductionsWithPagination")
    public ResponseEntity<Page<ZoneProduction>> getZoneProductions(@RequestParam() int page,
                                                  @RequestParam() int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ZoneProduction> zoneProductions = zoneProductionService.getAllZoneProductionPageable(pageable);
        return ResponseEntity.ok().body(zoneProductions);
    }
        

        
        @GetMapping("/getAllZonesByActeurs/{id}")
    public ResponseEntity<List<ZoneProduction>> listeZoneByActeurs(@PathVariable String id) {
        return new ResponseEntity<>(zoneProductionService.getAllZoneByActeur(id), HttpStatus.OK);
    }

        @DeleteMapping("/deleteZones/{id}")
        @Operation(summary = "Suppresion d'une zone production")
        public String supprimerZone(@PathVariable String id){
            return zoneProductionService.deleteZoneProduction(id);
        }
}
