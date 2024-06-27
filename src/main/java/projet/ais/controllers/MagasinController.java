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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import projet.ais.models.Conseil;
import projet.ais.models.Magasin;
import projet.ais.repository.MagasinRepository;
import projet.ais.services.FileUploade;
import projet.ais.services.MagasinService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.http.MediaType;
import java.io.IOException;



@RestController
// @CrossOrigin(origins = "*")
@RequestMapping("api-koumi/Magasin")
public class MagasinController {
    
    @Autowired
    MagasinService magasinService;
    @Autowired
    FileUploade fileUploade;
    @Autowired
    MagasinRepository magasinRepository;

    // @PostMapping("/addMagasin")
    // public ResponseEntity<Magasin> saveMagasin(
    //     @Valid @RequestParam("magasin") String magasins,
    //     @Valid @RequestParam(value = "image",required = false) MultipartFile imageFile) throws Exception{

    //         Magasin magasin1 = new Magasin();
    //         try {
    //             magasin1 = new JsonMapper().readValue(magasins,Magasin.class);
    //         }  catch (JsonProcessingException e) {
    //             throw new Exception(e.getMessage());
    //         }
    //     Magasin saveMag = magasinService.createMagasin(magasin1, imageFile);
    //     return new ResponseEntity<>(saveMag, HttpStatus.CREATED);
    // }
    @PostMapping("/addMagasin")
    public ResponseEntity<Magasin> saveMagasin(
    @Valid @RequestParam("magasin") String magasins,
    @RequestParam(value = "image", required = false) MultipartFile imageFile) throws Exception {

    Magasin magasin1;
    try {
        ObjectMapper objectMapper = new ObjectMapper();
        magasin1 = objectMapper.readValue(magasins, Magasin.class);
    } catch (JsonProcessingException e) {
        throw new Exception(e.getMessage());
    }

    Magasin saveMag = magasinService.createMagasin(magasin1, imageFile);
    return new ResponseEntity<>(saveMag, HttpStatus.CREATED);
    }

    
    @GetMapping("/{magasinId}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable String magasinId) {
    try {
        // Récupérer le nom de l'image associée au véhicule
        Magasin magasin = magasinRepository.findByIdMagasin(magasinId);
        if (magasin == null || magasin.getPhoto() == null) {
            return ResponseEntity.notFound().build();
        }

        String imageName = magasin.getPhoto();

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
    @PutMapping("/update/{idMagasin}")
    public ResponseEntity<Magasin> updatedMagasin(
        @Valid @RequestParam("magasin") String magasin,
        @Valid @RequestParam(value = "image",required = false) MultipartFile imageFile, @PathVariable String idMagasin) throws Exception{

            Magasin magasin1 = new Magasin();
            try {
                magasin1 = new JsonMapper().readValue(magasin,Magasin.class);
            }  catch (JsonProcessingException e) {
                throw new Exception(e.getMessage());
            }
        Magasin updateMag = magasinService.updateMagasin(magasin1, imageFile,idMagasin);
        return new ResponseEntity<>(updateMag, HttpStatus.CREATED);
    }

    @PutMapping("/activer/{id}")
    @Operation(summary="Activation de magasin fonction de l'id de filiere")
    public ResponseEntity<Magasin> activeMagasin(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(magasinService.active(id), HttpStatus.OK);
    }

    @PutMapping("/desactiver/{id}")
    @Operation(summary="Desactivation de magasin fonction de l'id de filiere")
    public ResponseEntity<Magasin> desactiveMagasin(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(magasinService.desactive(id), HttpStatus.OK);
    }
    
    @GetMapping("/getAllMagagin")
    @Operation(summary = "Liste des magasins")
    public ResponseEntity<List<Magasin>> listeMagasin(){
        return new ResponseEntity<>(magasinService.getMagasin(), HttpStatus.OK);
    }

    @GetMapping("/getMagasinByIdMagasin/{id}")
    public ResponseEntity<Magasin> listeMagasinById(@PathVariable String id){
        return new ResponseEntity<>(magasinService.getMagasinById(id), HttpStatus.OK);
    }

    @GetMapping("/getAllMagasinByActeur/{id}")
    @Operation(summary = "Liste des magasins par acteur")
    public ResponseEntity<List<Magasin>> listeMagasinByActeur(@PathVariable String id){
        return new ResponseEntity<>(magasinService.getMagasinByActeur(id), HttpStatus.OK);
    }
   
    @GetMapping("/getAllMagasinByActeurAndNiveau1Pays/{idActeur}/{idNiveau1Pays}")
    @Operation(summary = "Liste des magasins par region et par acteur")
    public ResponseEntity<List<Magasin>> listeMagasinByNiveau1PaysAndActeur(@PathVariable String idActeur, @PathVariable String idNiveau1Pays) throws Exception{
        return new ResponseEntity<>(magasinService.listeMagasinByNiveau1PaysAndActeur(idActeur, idNiveau1Pays), HttpStatus.OK);
    }

     @GetMapping("/getAllMagasinWithPagination")
    public ResponseEntity<Page<Magasin>> getMagasins(
        @RequestParam() String niveau3PaysActeur,
        @RequestParam() int page,
                                                  @RequestParam() int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Magasin> magasins = magasinService.getAllMagasinPageableByPays(niveau3PaysActeur,pageable);
        return ResponseEntity.ok().body(magasins);
    }



    @PutMapping("/update-pays")
    public String updatePaysForMagasins() {
        magasinService.updatePaysForMagasins();
        return "Mise à jour de la colonne pays pour tous les magasins réussie";
    }




     @GetMapping("/getAllMagasinByNiveau1PaysWithPagination")
    public ResponseEntity<Page<Magasin>> getMagasinsByNiveau1Pays(
        @RequestParam() String niveau3PaysActeur,
        @RequestParam() String idNiveau1Pays,
        @RequestParam() int page,
                                                  @RequestParam() int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Magasin> magasins = magasinService.getMagasinByNiveau1PaysWithPagination(niveau3PaysActeur,idNiveau1Pays,pageable);
        return ResponseEntity.ok().body(magasins);
    }

     @GetMapping("/getAllMagasinsByActeurWithPagination")
    public ResponseEntity<Page<Magasin>> getMagasinsByActeur(
        @RequestParam() String idActeur,
        @RequestParam() int page,
                                                  @RequestParam() int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Magasin> magasins = magasinService.getMagasinByActeurWithPagination(idActeur,pageable);
        return ResponseEntity.ok().body(magasins);
    }



    @GetMapping("/getAllMagasinByPays/{id}")
    @Operation(summary = "Liste des magasins par niveau 1 pays")
    public ResponseEntity<List<Magasin>> listeMagasinByNiveau1Pays(@PathVariable String id){
        return new ResponseEntity<>(magasinService.getMagasinByNiveau1Pays(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression du magasin")
    public String supprimer(@PathVariable String id){
        return magasinService.supprimerMagagin(id);
    }
}