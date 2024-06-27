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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import projet.ais.models.Conseil;
import projet.ais.models.Intrant;
import projet.ais.models.Materiel;
import projet.ais.repository.MaterielRepository;
import projet.ais.services.FileUploade;
import projet.ais.services.MaterielService;

import org.springframework.http.MediaType;
import java.io.IOException;

@RestController
// @CrossOrigin(origins = "*")
@RequestMapping("api-koumi/Materiel")
public class MaterielController {
    
    @Autowired
    MaterielService materielService;
    @Autowired
    FileUploade fileUploade;
    @Autowired
    MaterielRepository materielRepository;

    @PostMapping("/addMateriel")
    @Operation(summary = "Ajout du materiel")
    public ResponseEntity<Materiel> saveMateriel(
        @Valid @RequestParam("materiel") String addMateriel,
        @Valid @RequestParam(value = "image", required = false)  MultipartFile imageFile) throws Exception
    {
        Materiel materiel = new Materiel();

       try {
        materiel =  new JsonMapper().readValue(addMateriel, Materiel.class);
       } catch (JsonProcessingException e) {
           throw new Exception(e.getMessage());
       }
       Materiel savedMateriel = materielService.createMateriel(materiel, imageFile);

       return new ResponseEntity<>(savedMateriel, HttpStatus.CREATED);
    }

    @GetMapping("/{materielId}/image")
public ResponseEntity<byte[]> getImage(@PathVariable String materielId) {
    try {
        // Récupérer le nom de l'image associée au véhicule
        Materiel materiel =  materielRepository.findByIdMateriel(materielId);
        if (materiel == null || materiel.getPhotoMateriel() == null) {
            return ResponseEntity.notFound().build();
        }

        String imageName = materiel.getPhotoMateriel();

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

  @GetMapping("/getAllMaterielsWithPagination")
    public ResponseEntity<Page<Materiel>> getMateriels(@RequestParam() int page,
                                                  @RequestParam() int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Materiel> materiels = materielService.getAllMaterielPageable(pageable);
        return ResponseEntity.ok().body(materiels);
    }

  @GetMapping("/getAllMaterielsByTypeMaterielWithPagination")
    public ResponseEntity<Page<Materiel>> getMaterielByTypeMaterielWithPagination(
        @RequestParam() String idTypeMateriel,
        @RequestParam() int page,
        @RequestParam() int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Materiel> materiels = materielService.getMaterielByTypeMaterielWithPagination(idTypeMateriel,pageable);
        return ResponseEntity.ok().body(materiels);
    }

  @GetMapping("/getAllMaterielsByActeurWithPagination")
    public ResponseEntity<Page<Materiel>> getMaterielByActeurWithPagination(
        @RequestParam() String idActeur,
        @RequestParam() int page,
        @RequestParam() int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Materiel> materiels = materielService.getMaterielByActeurWithPagination(idActeur,pageable);
        return ResponseEntity.ok().body(materiels);
    }


    @GetMapping("/getMaterielsByFiliereWithPagination")
    public ResponseEntity<Page<Materiel>> getAllMaterielsByFiliere(

        @RequestParam String libelleFiliere,
        @RequestParam String pays,
        @RequestParam() int page,
        @RequestParam() int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Materiel> materiels = materielService.getAllMaterielByLibelleFiliere(libelleFiliere, pays, pageable);
        return ResponseEntity.ok().body(materiels);
    }
    
    @GetMapping("/getMaterielsByPaysWithPagination")
    public Page<Materiel> getAllMaterielsPageableByPays(@RequestParam String niveau3PaysActeur, Pageable pageable) {
        return materielService.getAllMaterielPageableByPays(niveau3PaysActeur, pageable);
    }

    @GetMapping("/getMaterielsByIdTypeAndFiliere")
    public Page<Materiel> getMaterielByTypeFiliereAndPays(
        @RequestParam String idTypeMateriel,
        @RequestParam String libelleFiliere,
        @RequestParam String pays,
        @RequestParam() int page,
        @RequestParam() int size) {
        Pageable pageable = PageRequest.of(page, size);
        
        return materielService.getAllMaterielByIdTypeMaterielAndFiliere(idTypeMateriel, libelleFiliere, pays, pageable);
    }

    @GetMapping("/getMaterielsByPaysAndTypeMaterielWithPagination")
    public Page<Materiel> getAllMaterielsPageableByPaysAndCategorie(@RequestParam String idTypeMateriel, @RequestParam String niveau3PaysActeur,  Pageable pageable) {
        return materielService.getAllMaterielPageableByPaysByCategorie(idTypeMateriel, niveau3PaysActeur , pageable);
    }


    @PutMapping("/update-pays")
    public String updatePaysForMateriel() {
    materielService.updatePaysForMateriel();
        return "Mise à jour de la colonne pays pour tous les materiels réussie";
    }

    
    @PutMapping("/update/{id}")
    @Operation(summary = "Modification du materiel")
    public ResponseEntity<Materiel> updatedMateriel(
        @Valid @RequestParam("materiel") String addMateriel,
        @Valid @RequestParam(value = "image", required = false)  MultipartFile imageFile, @PathVariable String id) throws Exception
    {
        Materiel materiel = new Materiel();

       try {
        materiel =  new JsonMapper().readValue(addMateriel, Materiel.class);
       } catch (JsonProcessingException e) {
           throw new Exception(e.getMessage());
       }
       Materiel savedMateriel = materielService.updateMateriel(materiel, id, imageFile);

       return new ResponseEntity<>(savedMateriel, HttpStatus.OK);
    }

    @PutMapping("/activer/{id}")
    public ResponseEntity<Materiel> activeMateriel(@PathVariable String id) throws Exception{
        return new ResponseEntity<>(materielService.active(id), HttpStatus.OK);
    }

    @PutMapping("/desactiver/{id}")
    public ResponseEntity<Materiel> desactiveMateriel(@PathVariable String id) throws Exception{
        return new ResponseEntity<>(materielService.desactive(id), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Materiel>> getAllMateriel(){
        return new ResponseEntity<>(materielService.getMateriels(), HttpStatus.OK);
    }

    @GetMapping("/readByActeur/{id}")
    public ResponseEntity<List<Materiel>> getAllByActeur(@PathVariable String id){
        return new ResponseEntity<>(materielService.getMaterielByActeur(id), HttpStatus.OK);
    }

    @GetMapping("/readByTypeMateriel/{id}")
    public ResponseEntity<List<Materiel>> getAllByMaterielByType(@PathVariable String id){
        return new ResponseEntity<>(materielService.getMaterielByTypeMateriel(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public String supprimer(@PathVariable String id){
        return materielService.deleteMateriel(id);
    }
}
