package projet.ais.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;

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
import projet.ais.models.Intrant;
import projet.ais.repository.IntrantRepository;
import projet.ais.services.FileUploade;
import projet.ais.services.IntrantService;
import org.springframework.http.MediaType;
import java.io.IOException;

@RestController
@RequestMapping("api-koumi/intrant")
public class IntrantController {

    @Autowired
    private IntrantService intrantService;
    @Autowired
    FileUploade fileUploade;
    @Autowired
    IntrantRepository intrantRepository;


    
    
    @PostMapping("/create")
    @Operation(summary = "création d'un intrant")
     public ResponseEntity<Intrant> createIntrant(
            @Valid @RequestParam("intrant") String intrantString,
            @RequestParam(value = "image", required = false) MultipartFile imageFile)
            throws Exception {
                
                Intrant intrant = new Intrant();
                try {
                    intrant = new JsonMapper().readValue(intrantString, Intrant.class);
                } catch (JsonProcessingException e) {
                    throw new Exception(e.getMessage());
                }
            
                // je le cree et le sauvegarde.
                Intrant savedIntrant = intrantService.createIntrant(intrant, imageFile);
            
                return new ResponseEntity<>(savedIntrant, HttpStatus.CREATED);
            }

            @PutMapping("/updateView/{id}")
            @Operation(summary = "Update view")
            public ResponseEntity<Intrant> updateViews(@PathVariable String id) throws Exception{
                return new ResponseEntity<>(intrantService.updateNbViev(id), HttpStatus.OK);
            }

            @GetMapping("/{intrantId}/image")
            public ResponseEntity<byte[]> getImage(@PathVariable String intrantId) {
                try {
                    // Récupérer le nom de l'image associée au véhicule
                    Intrant intrant = intrantRepository.findByIdIntrant(intrantId);
                    if (intrant == null || intrant.getPhotoIntrant() == null) {
                        return ResponseEntity.notFound().build();
                    }
            
                    String imageName = intrant.getPhotoIntrant();
            
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
        @Operation(summary = "Mise à jour d'un intrant ")
        public ResponseEntity<Intrant> updateIntrant(
              @PathVariable String id,
              @Valid @RequestParam("intrant") String intrantString,
              @RequestParam(value = "image", required = false)  MultipartFile imageFile){
              Intrant intrant = new Intrant();
          try {
            intrant = new JsonMapper().readValue(intrantString, Intrant.class);
          } catch (JsonProcessingException e) {
              return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
          }

          try {
            Intrant intrantMisAjour = intrantService.updateIntrant(intrant, imageFile, id);
            return new ResponseEntity<>(intrantMisAjour, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
  
      }

      @GetMapping("/getIntrantByCritereWithPagination")
      @Operation(summary = "Recuperer  un intrant par critères")
      public ResponseEntity<Page<Intrant>> getIntrantWithCritere(
              @RequestParam(required = false) String nomIntrant,
              @RequestParam(required = false) List<String> categories,
              @RequestParam(required = false) Double quantiteIntrant,
              @RequestParam(required = false) Integer prixMin,
              @RequestParam(required = false) Integer prixMax,
              @RequestParam int page,
              @RequestParam int size
      ) {
          Pageable pageable = PageRequest.of(page, size);
          Page<Intrant> i = intrantService.getIntrantByCritere(nomIntrant,categories,quantiteIntrant, prixMin, prixMax, pageable);
          return ResponseEntity.ok().body(i);
      }

    @GetMapping("/getAllIntrantsWithPagination")
    public ResponseEntity<Page<Intrant>> getIntrants(@RequestParam() int page,
                                                  @RequestParam() int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Intrant> intrants = intrantService.getAllIntrantPageable(pageable);
        return ResponseEntity.ok().body(intrants);
    }

    @GetMapping("getIntrantById/{idIntrant}")
    public Intrant getIntrantById(@PathVariable String idIntrant) {
        return intrantService.getIntrantById(idIntrant);
    }

    @PutMapping("/{id}/quantite")
    public Intrant updateQuantiteIntrants(@PathVariable String id, @RequestParam double quantite) throws Exception {
        return intrantService.updateQuantiteIntrant(id, quantite);
    }

    @PutMapping("/update-pays/{id}")
    public String updatePaysFor(@PathVariable String id) {
        intrantService.updatePaysForIntrantsss(id);
        return "Mise à jour de la colonne pays réussie";
    }

    @GetMapping("/getIntrantsByPaysWithPagination")
    public Page<Intrant> getAllIntrantsPageableByPays( Pageable pageable) {
        return intrantService.getAllIntrantPageableByPays(pageable);
    }

    // @GetMapping("/getIntrantsByPaysWithPagination")
    // public Page<Intrant> getAllIntrantsPageableByPays(@RequestParam String niveau3PaysActeur, Pageable pageable) {
    //     return intrantService.getAllIntrantPageableByPays(niveau3PaysActeur, pageable);
    // }

    @GetMapping("/getAllIntrantByPaysWithPagination")
    public Page<Intrant> getAllIntrantsByPays(@RequestParam String nomPays, Pageable pageable) {
        return intrantService.getAllByPaysWithPagination(nomPays, pageable);
    }

    @GetMapping("/getIntrantsByPaysAndCategorieWithPagination")
    public Page<Intrant> getAllIntrantsPageableByPaysAndCategorie(@RequestParam String idCategorieProduit, @RequestParam String niveau3PaysActeur,  Pageable pageable) {
        return intrantService.getAllIntrantPageableByPaysByCategorie(idCategorieProduit, niveau3PaysActeur , pageable);
    }

    @GetMapping("/getIntrantsByPaysAndCategorieLibelleCategorieWithPagination")
    public Page<Intrant> getAllIntrantsPageableByPaysAndCategorieLibelleCategorie(@RequestParam String libelle, @RequestParam String niveau3PaysActeur,  Pageable pageable) {
        return intrantService.getAllIntrantPageableByPaysByLibelleCategorie(libelle, niveau3PaysActeur, pageable);
    }


      @GetMapping("/getAllIntrantsByPaysWithPagination")
        public ResponseEntity<Page<Intrant>> getAllIntrantPageableByPays(
                @RequestParam int page,
                @RequestParam int size) {
    
            Pageable pageable = PageRequest.of(page, size);
            Page<Intrant> intrants = intrantService.getAllIntrantPageableByPays( pageable);
    
            return ResponseEntity.ok(intrants);
        }
    //   @GetMapping("/getAllIntrantsByPaysWithPagination")
    //     public ResponseEntity<Page<Intrant>> getAllIntrantPageableByPays(
    //             @RequestParam String niveau3PaysActeur,
    //             @RequestParam int page,
    //             @RequestParam int size) {
    
    //         Pageable pageable = PageRequest.of(page, size);
    //         Page<Intrant> intrants = intrantService.getAllIntrantPageableByPays(niveau3PaysActeur, pageable);
    
    //         return ResponseEntity.ok(intrants);
    //     }

       @GetMapping("/getAllIntrantsByActeurWithPagination")
    public ResponseEntity<Page<Intrant>> getIntrantsByActeur(
        @RequestParam() String idActeur,
        @RequestParam() int page,
        @RequestParam() int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Intrant> intrants = intrantService.getIntrantByActeurWithPagination(idActeur,pageable);
        return ResponseEntity.ok().body(intrants);
    }

    @PutMapping("/update-pays")
        public String updatePaysForIntrants() {
        intrantService.updatePaysForIntrant();
            return "Mise à jour de la colonne pays pour tous les intrants réussie";
        }

    @GetMapping("/getAllIntrantsByCategorieWithPagination")
    public ResponseEntity<Page<Intrant>> getIntrantsByCategorieWithPagination(
        @RequestParam() String idCategorie,
        @RequestParam() int page,
        @RequestParam() int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Intrant> intrants = intrantService.getIntrantByCategorieWithPagination(idCategorie,pageable);
        return ResponseEntity.ok().body(intrants);
    }

    @GetMapping("/listeIntrantByLibelleFiliereAndIcategorie")
    public ResponseEntity<Page<Intrant>> getIntrantsByLibelleFiAndCat(
        @RequestParam() String idCategorie,
        @RequestParam() String libelle,
        @RequestParam() String pays,
        @RequestParam() int page,
        @RequestParam() int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Intrant> intrants = intrantService.getAllIntrantByLibelleFiliereAndIdCategorie(idCategorie,libelle,pays, pageable);
        return ResponseEntity.ok().body(intrants);
    }
    @GetMapping("/listeIntrantByLibelleCategorie")
    public ResponseEntity<Page<Intrant>> getIntrantsByLibelleCategorie(
        @RequestParam() String libelle,
        @RequestParam() int page,
        @RequestParam() int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Intrant> intrants = intrantService.getAllIntrantByLibelleCategorie(libelle, pageable);
        return ResponseEntity.ok().body(intrants);
    }
   
    // @GetMapping("/listeIntrantByLibelleCategorie")
    // public ResponseEntity<Page<Intrant>> getIntrantsByLibelleCategorie(
    //     @RequestParam() String libelle,
    //     @RequestParam() String pays,
    //     @RequestParam() int page,
    //     @RequestParam() int size) {
    //     Pageable pageable = PageRequest.of(page, size);
    //     Page<Intrant> intrants = intrantService.getAllIntrantByLibelleCategorie(libelle,pays, pageable);
    //     return ResponseEntity.ok().body(intrants);
    // }
    @GetMapping("/listeIntrantByLibelleAndPays")
    public ResponseEntity<Page<Intrant>> getIntrantsByLibelle(
        @RequestParam() String libelle,
        @RequestParam() String pays,
        @RequestParam() int page,
        @RequestParam() int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Intrant> intrants = intrantService.getByLibelleAndPaysWithPagination(libelle,pays, pageable);
        return ResponseEntity.ok().body(intrants);
    }

      
         //liste intrant pas acteur
    @GetMapping("/listeIntrantByActeur/{id}")
    @Operation(summary = "affichage de la liste des intrants par acteur")
    public ResponseEntity<List<Intrant>> listeIntrantByActeur(@PathVariable String id){
        return  new ResponseEntity<>(intrantService.getAllIntrantByActeur(id), HttpStatus.OK);
    }

         //liste intrant pas acteur
    // @GetMapping("/listeIntrantBySpeculation/{id}")
    // @Operation(summary = "affichage de la liste des intrants par Speculation")
    // public ResponseEntity<List<Intrant>> listeIntrantBySpeculation(@PathVariable String id){
    //     return  new ResponseEntity<>(intrantService.getAllIntrantBySpeculation(id), HttpStatus.OK);
    // }

         //liste intrant pas id categorie
    @GetMapping("/listeIntrantByCategorie/{id}")
    @Operation(summary = "affichage de la liste des intrants par Categorie")
    public ResponseEntity<List<Intrant>> listeIntrantByCategorie(@PathVariable String id){
        return  new ResponseEntity<>(intrantService.getAllIntrantByCategorie(id), HttpStatus.OK);
    }

    // @GetMapping("/listeIntrantBySuperficie/{id}")
    // @Operation(summary = "affichage de la liste des intrants par superficie")
    // public ResponseEntity<List<Intrant>> listeIntrantBySuperficie(@PathVariable String id){
    //     return  new ResponseEntity<>(intrantService.getAllIntrantBySuperficie(id), HttpStatus.OK);
    // }

                 // Get Liste des  intrants
      @GetMapping("/read")
      @Operation(summary = "Liste globale des intrants")
    public ResponseEntity<List<Intrant>> getAllIntrant() {
        return new ResponseEntity<>(intrantService.getAllIntrant(), HttpStatus.OK);
    }


    @PutMapping("/disable/{id}")
    //Desactiver un intrant methode
    @Operation(summary = "Désactiver un intrant ")
    public ResponseEntity<String> disableIntrant(@PathVariable String id) throws Exception{
    
        intrantService.desactive(id);
        return new ResponseEntity<>("Intrant desactiver avec succes", HttpStatus.ACCEPTED);
    }

    //Aciver intrant
      @PutMapping("/enable/{id}")
      @Operation(summary = "Activer intrant ")
    public ResponseEntity <String> enableIntrant(@PathVariable String id) throws Exception{
    
        intrantService.active(id);
        return new ResponseEntity<>("Intrant activer avec succes", HttpStatus.ACCEPTED);
    }


             //Supprimer un intrant
           @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression d'un intrant")
    public ResponseEntity<String> deleteIntrant(@PathVariable String id){
        return new ResponseEntity<>(intrantService.deleteIntrant(id), HttpStatus.OK);
    }


    
}
