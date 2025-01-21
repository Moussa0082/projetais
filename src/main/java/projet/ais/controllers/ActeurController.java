package projet.ais.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.*;

import projet.ais.config.ResponseHandler;
import projet.ais.models.Acteur;
import projet.ais.models.Alerte;
import projet.ais.models.Intrant;
import projet.ais.models.Stock;
import projet.ais.models.TypeActeur;
import projet.ais.repository.ActeurRepository;
import projet.ais.repository.TypeActeurRepository;
import projet.ais.services.ActeurService;
import projet.ais.services.EmailService;
import projet.ais.services.FileUploade;

import org.springframework.http.MediaType;
import java.io.IOException;

@RestController
@RequestMapping("api-koumi/acteur")
public class ActeurController {


    @Autowired
    private ActeurService acteurService;

    @Autowired
    private ActeurRepository acteurRepository;

    @Autowired
    private TypeActeurRepository typeActeurRepository;

    @Autowired
    private EmailService emailService;
    @Autowired
    FileUploade fileUploade;

    

    @PostMapping("/create")
    @Operation(summary = "création d'un acteur")
    public ResponseEntity<Acteur> createActeur(
            @Valid @RequestParam("acteur") String acteurString,
            @RequestParam(value = "image1", required = false) MultipartFile imageFile1,
            @RequestParam(value = "image2", required = false) MultipartFile imageFile2)
            throws Exception {
                

                Acteur acteur = new Acteur();
                try {
                    acteur = new JsonMapper().readValue(acteurString, Acteur.class);
                } catch (JsonProcessingException e) {
                    throw new Exception(e.getMessage());
                }
            
                // je le cree et le sauvegarde.

                Acteur savedActeur = acteurService.createActeur(acteur, imageFile1, imageFile2);
                System.out.println("Acteur controller :" + savedActeur);

                return new ResponseEntity<>(savedActeur, HttpStatus.CREATED);
            }

    
            @PutMapping("/{id}/password")
            public Acteur updateActeurs(@PathVariable String id, @RequestParam String password) throws Exception {
                return acteurService.updatePassWord(id, password);
            }
           
            @GetMapping("/{acteurId}/image")
            public ResponseEntity<byte[]> getImage(@PathVariable String acteurId) {
                try {
                    // Récupérer le nom de l'image associée au véhicule
                    Acteur acteur = acteurRepository.findByIdActeur(acteurId);
                    if (acteur == null || acteur.getLogoActeur() == null) {
                        return ResponseEntity.notFound().build();
                    }
            
                    String imageName = acteur.getLogoActeur();
            
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
            
            @GetMapping("/{acteurId}/siege")
            public ResponseEntity<byte[]> getLogoSiege(@PathVariable String acteurId) {
                try {
                    // Retrieve the actor based on the given ID
                    Acteur acteur = acteurRepository.findByIdActeur(acteurId);
                    if (acteur == null || acteur.getPhotoSiegeActeur() == null) {
                        return ResponseEntity.notFound().build();
                    }

                    // Get the logo name
                    String logoName = acteur.getPhotoSiegeActeur();

                    // Retrieve the image bytes from the FTP server
                    byte[] imageBytes = fileUploade.getImageByName(logoName);

                    // Detect the content type based on the file extension
                    MediaType contentType = detectContentType(logoName);

                    // Return the image with the appropriate content type
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

            
    @GetMapping("/libelleNiveau1Pays/{id}")
    public ResponseEntity<String> getLibelleNiveau1Pays(@PathVariable String id) {
        String libelleNiveau1Pays = acteurService.getLibelleNiveau1PaysForActeur(id);
        return ResponseEntity.ok(libelleNiveau1Pays);
    }

    @GetMapping("/libelleNiveau2Pays/{id}")
    public ResponseEntity<String> getLibelleNiveau2Pays(@PathVariable String id) {
        String libelleNiveau2Pays = acteurService.getLibelleNiveau2PaysForActeur(id);
        return ResponseEntity.ok(libelleNiveau2Pays);
    }

    @GetMapping("/libelleNiveau3Pays/{id}")
    public ResponseEntity<String> getLibelleNiveau3Pays(@PathVariable String id) {
        String libelleNiveau3Pays = acteurService.getLibelleNiveau3PaysForActeur(id);
        return ResponseEntity.ok(libelleNiveau3Pays);
    }

    @GetMapping("/monnaie/{id}")
    public ResponseEntity<String> getMonnaie(@PathVariable String id) {
        String monnaie = acteurService.getMonnaiePaysForActeur(id);
        return ResponseEntity.ok(monnaie);
    }
    @GetMapping("/getActeurByCritereWithPagination")
    @Operation(summary = "Recuperer les acteurs par critères")
    public ResponseEntity<Page<Acteur>> getStocksWithCritere(
            @RequestParam(required = false) List<String> typeActeurs,
            @RequestParam(required = false) List<String> speculations,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Acteur> acteur = acteurService.getActeurWithCritere(typeActeurs,speculations, pageable);
        return ResponseEntity.ok().body(acteur);
    }

             @GetMapping("/getAllActeurWithPagination")
    public ResponseEntity<Page<Acteur>> getActeurs(@RequestParam() int page,
                                                  @RequestParam() int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Acteur> acteur = acteurService.getAllActeurPageable(pageable);
        return ResponseEntity.ok().body(acteur);
    }


            // @PostMapping("/{idActeur}/types")
            // public ResponseEntity<Acteur> addTypesToActeur(@PathVariable String idActeur,
            //                                                 @RequestBody Map<String, Object> requestBody) throws Exception {
            //     // Récupérer la liste des typeActeurs de la requête
            //     List<Map<String, String>> typeActeursMapList = (List<Map<String, String>>) requestBody.get("typeActeurs");
                
            //     // Convertir la liste des typeActeurs de la requête en une liste de TypeActeur
            //     List<TypeActeur> typeActeurs = new ArrayList<>();
            //     for (Map<String, String> typeActeurMap : typeActeursMapList) {
            //         TypeActeur typeActeur = new TypeActeur();
            //         typeActeur.setIdTypeActeur(typeActeurMap.get("idTypeActeur"));
            //         typeActeurs.add(typeActeur);
            //     }
            
            //     // Appeler la méthode de service pour ajouter les types d'acteur à l'acteur
            //     Acteur acteur = acteurService.addTypesToActeur(idActeur, typeActeurs);
                
            //     // Retourner la réponse appropriée
            //     if (acteur == null) {
            //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            //     }
            //     return new ResponseEntity<>(acteur, HttpStatus.OK);
            // }
            @PostMapping("/{idActeur}/types")
            public ResponseEntity<Acteur> addTypesToActeur(@PathVariable String idActeur,
                                                            @RequestBody Map<String, Object> requestBody) throws Exception {
                // Récupérer la liste des typeActeurs de la requête
                List<Map<String, String>> typeActeursMapList = (List<Map<String, String>>) requestBody.get("typeActeur");
                
                // Convertir la liste des typeActeurs de la requête en une liste de TypeActeur
                List<TypeActeur> typeActeurs = new ArrayList<>();
                for (Map<String, String> typeActeurMap : typeActeursMapList) {
                    String typeId = typeActeurMap.get("idTypeActeur");
                    TypeActeur typeActeur = typeActeurRepository.findByIdTypeActeur(typeId);
                    if (typeActeur != null) {
                        typeActeurs.add(typeActeur);
                    }
                }
            
                // Récupérer l'acteur
                Acteur acteur = acteurRepository.findByIdActeur(idActeur);
                if (acteur == null) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                
                // Associer les types d'acteur à l'acteur existant
                acteur.getTypeActeur().addAll(typeActeurs);
                
                // Enregistrer les modifications
                acteurRepository.save(acteur);
            
                return new ResponseEntity<>(acteur, HttpStatus.OK);
            }
            

            


    @GetMapping("/sendOtpCodeEmail")
    @Operation(summary = "Verifier l'email de l'utilisateur en lui envoyant un code de verification à son adresse email pour la procedure de changement de son mot de pass")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "L'email exist et le code a été envoyer avec succès", content = {
                    @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))
            }),
            @ApiResponse(responseCode = "500",description = "Erreur serveur", content = @Content),
    })
    public ResponseEntity<Object> sendOtpCodeEmail(@RequestParam("emailActeur") String emailActeur) throws Exception {
        return ResponseHandler.generateResponse(acteurService.sendOtpCodeEmail(emailActeur), HttpStatus.OK,null);
    }

    @GetMapping("/sendOtpCodeWhatsApp")
    @Operation(summary = "Verifier le numéro de l'utilisateur en lui envoyant un code de verification à numéro whatsApp pour la procedure de changement de son mot de pass")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Le numéro exist et le code a été envoyer avec succès", content = {
                    @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))
            }),
            @ApiResponse(responseCode = "500",description = "Erreur serveur", content = @Content),
    })
    public ResponseEntity<Object> sendOtpCodeWhatsApp(@RequestParam("whatsAppActeur") String whatsAppActeur) throws Exception {
        return ResponseHandler.generateResponse(acteurService.sendOtpCodeWhatsApp(whatsAppActeur), HttpStatus.OK,null);
    }

    //Envoyer un email à un utilisateur spécifique
    @GetMapping("/sendMailToUser")
    @Operation(summary = "Envoyer un mail à un utilisateur")
    public ResponseEntity<Object> sendMailToUser(@RequestParam ("email") String email, @RequestParam("sujet")String sujet, @RequestParam("message")String message) throws Exception {
        acteurService.sendToUser(email, sujet, message);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/resetPasswordEmail")
    @Operation(summary = "Réinitialise le mot de passe de l'utilisateur via email et nouveau mot de passe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mot de passe réinitialisé avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    public ResponseEntity<String> resetPasswordEmail(@RequestParam("emailActeur") String emailActeur, @RequestParam("password") String password) {
        try {
            acteurService.resetPasswordEmail(emailActeur, password);
            return ResponseEntity.ok("Mot de passe réinitialisé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue : " + e.getMessage());
        }
    }
    @PutMapping("/resetPasswordWhatsApp")
    @Operation(summary = "Réinitialise le mot de passe de l'utilisateur via whats app numéro et nouveau mot de passe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mot de passe réinitialisé avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    public ResponseEntity<String> resetPasswordWhatsApp(@RequestParam("whatsAppActeur") String whatsAppActeur, @RequestParam("password") String password) {
        try {
            acteurService.resetPasswordWhatsApp(whatsAppActeur, password);
            return ResponseEntity.ok("Mot de passe réinitialisé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue : " + e.getMessage());
        }
    }

    @GetMapping("/verifierOtpCodeEmail")
    public ResponseEntity<?> verifyOtpCodeEmail(@RequestParam String emailActeur, @RequestParam String resetToken) {
        System.out.println("Received emailActeur: " + emailActeur);
        System.out.println("Received resetToken: " + resetToken);

        try {
            acteurService.verifyOtpCodeEmail(emailActeur, resetToken);
            return ResponseEntity.ok("Code vérifié avec succès");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue");
        }
    }

    @GetMapping("/verifierOtpCodeWhatsApp")
    public ResponseEntity<?> verifyOtpCodeWhatsApp(@RequestParam String whatsAppActeur, @RequestParam String code) {
        System.out.println("Received whatsAppActeur: " + whatsAppActeur);
        System.out.println("Received code: " + code);

        try {
            acteurService.verifyOtpCodeWhatsApp(whatsAppActeur, code);
            return ResponseEntity.ok("Code vérifié avec succès");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue");
        }
    }

    @GetMapping("/send-email-to-all-user")
    public ResponseEntity<String> sendEmailToAllUsers(@RequestParam ("emails") List<String> emails, @RequestParam("sujet")String sujet, @RequestParam("message")String message) {
       
        //  acteurService.sendMailToAllUser(email, sujet, message);
         if(!emails.isEmpty()){
            for (String email : emails) {
                acteurService.sendMailToAllUser(email, sujet, message);
                Alerte al = new Alerte(email, message, sujet);
                emailService.sendSimpleMail(al);
                
                System.out.println(email);
            }
            return new ResponseEntity<>("Email envoyé à tous les utilisateurs avec succès", HttpStatus.OK);
        } else {
            // Log the exception for debugging
            return new ResponseEntity<>("Failed to send emails", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

  

    @GetMapping("/send-email-to-all-choose")
    public ResponseEntity<String> sendEmailToAllUsersChoose(@RequestParam ("emails") List<String> emails, @RequestParam("sujet")String sujet, @RequestParam("message")String message, @RequestParam("libelle")String libelle) {
       
        //  acteurService.sendMailToAllUser(email, sujet, message);
         if(!emails.isEmpty()){
            for (String email : emails) {
                acteurService.sendMailToAllUserChoose(email, sujet, message, libelle);
                Alerte al = new Alerte(email, message, sujet);
                emailService.sendSimpleMail(al);
                
                System.out.println(email);
            }
            return new ResponseEntity<>("Email envoyé  avec succès à tous les " + libelle, HttpStatus.OK);
        } else {
            // Log the exception for debugging
            return new ResponseEntity<>("Echec de l'envoi d'emails car aucun email n'a été trouvé", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @PostMapping("/send-message-to-admin")
    // public ResponseEntity<String> sendMessageToAdmin(@RequestParam String message, @RequestParam String acteur ) {
    //     try {
    //         acteurService.sendMessageWaToAdmin(message, acteur);
    //         return new ResponseEntity<>("Message sent to admin successfully", HttpStatus.ACCEPTED);
    //     } catch (Exception e) {
    //         return new ResponseEntity<>("Failed to send message to admin: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

    @GetMapping("/send-email-to-all-checked-choose")
    public ResponseEntity<String> sendMailToAllUserCheckedChoose(@RequestParam ("emails") List<String> emails, @RequestParam("sujet")String sujet, @RequestParam("message")String message, @RequestParam("libelle")List<String> libelle) {
       
        //  acteurService.sendMailToAllUser(email, sujet, message);
         if(!emails.isEmpty()){
            for (String email : emails) {
                acteurService.sendMailToAllUserCheckedChoose(email, sujet, message, libelle);
                Alerte al = new Alerte(email, message, sujet);
                emailService.sendSimpleMail(al);
                
                System.out.println( "controller print " + email);
            }
            return new ResponseEntity<>("Email envoyé  avec succès à tous les " + libelle, HttpStatus.OK);
        } else {
            // Log the exception for debugging
            return new ResponseEntity<>("Echec de l'envoi d'emails car aucun email n'a été trouvé", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @GetMapping("/sendMessageWathsappToActeurByTypeActeur")
    // public ResponseEntity<String> sendMessageWathsappToActeur(@RequestParam String message, @RequestParam List<String> libelles) {
    //     try {
    //         acteurService.sendMessageToActeurByTypeActeur(message, libelles);
            
    //         return new ResponseEntity<>("Message envoyé avec succès à tous les acteurs correspondant aux libellés " + libelles.size(), HttpStatus.OK);
    //     } catch (Exception e) {
    //         // En cas d'erreur, retourner une réponse avec un message d'erreur
    //         return new ResponseEntity<>("Échec de l'envoi du message WhatsApp aux acteurs correspondant aux libellés " + libelles + ". Erreur : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }
    
    @GetMapping("/sendMessageWathsappToActeurByTypeActeurs")
    public ResponseEntity<String> sendMessageWathsappToActeur(@RequestParam String message, @RequestParam List<String> libelles) {
        try {
            acteurService.sendMessageToActeurByTypeActeur(message, libelles);
            
            return new ResponseEntity<>("Message envoyé avec succès à tous les acteurs correspondant aux libellés " + libelles.size(), HttpStatus.OK);
        } catch (Exception e) {
            // En cas d'erreur, retourner une réponse avec un message d'erreur
            return new ResponseEntity<>("Échec de l'envoi du message WhatsApp aux acteurs correspondant aux libellés " + libelles + ". Erreur : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sendEmailToActeurByTypeActeur")
    public ResponseEntity<String> sendEmailToActeurByTypeActeurs(@RequestParam String message, @RequestParam List<String> libelles, @RequestParam String sujet) {
        try {
            acteurService.sendEmailToActeurByTypeActeur(message, libelles, sujet);
            
            return new ResponseEntity<>("Message envoyé avec succès à tous les acteurs correspondant aux libellés " + libelles.size(), HttpStatus.OK);
        } catch (Exception e) {
            // En cas d'erreur, retourner une réponse avec un message d'erreur
            return new ResponseEntity<>("Échec de l'envoi du message email aux acteurs correspondant aux libellés " + libelles + ". Erreur : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
             //Mettre à jour un acteur
    @PutMapping("/update/{idActeur}")
    @Operation(summary = "Mise à jour d'un acteur ")
      public ResponseEntity<Acteur> updateActeur(
              @PathVariable String idActeur,
              @Valid @RequestParam("acteur") String acteurString,
              @RequestParam(value = "image1", required = false)  MultipartFile imageFile1,
              @RequestParam(value = "image2", required = false) MultipartFile imageFile2) {
          Acteur acteur = new Acteur();
          try {
               acteur = new JsonMapper().readValue(acteurString, Acteur.class);
          } catch (JsonProcessingException e) {
              return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
          }

          try {
            Acteur acteurMisAjour = acteurService.updateActeur(acteur, idActeur, imageFile1, imageFile2);
            return new ResponseEntity<>(acteurMisAjour, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
  
         
      }

             // Get Liste des  aceturs
      @GetMapping("/read")
      @Operation(summary = "Liste globale des acteurs")
    public ResponseEntity<List<Acteur>> getAllActeur() {
        return new ResponseEntity<>(acteurService.getAllActeur(), HttpStatus.OK);
    }

    @GetMapping("getActeurById/{idActeur}")
    @Operation(summary = "Recuperation d'un acteur")
    public Acteur getActeurById(@PathVariable String idActeur) {
        return acteurService.getActeurById(idActeur);
    }

    @PutMapping("/disable/{id}")
    //Desactiver un admin methode
    @Operation(summary = "Désactiver acteur ")
    public ResponseEntity <String> disableActeur(@PathVariable String id) throws Exception{
    
        acteurService.disableActeur(id);
        return new ResponseEntity<>("Acteur desactiver avec succes", HttpStatus.ACCEPTED);
    }
 
    @PutMapping("/deleteActeur/{id}")
    //Desactiver un admin methode
    @Operation(summary = "Demande de suppression de compte ")
    public ResponseEntity <String> demandeActeur(@PathVariable String id, @RequestParam String msg) throws Exception{
    
        acteurService.demandeSup(id,msg);
        return new ResponseEntity<>("Demande envoyé avec succèss", HttpStatus.ACCEPTED);
    }

    //Aciver admin
    @PutMapping("/enable/{id}")
    @Operation(summary = "Activer acteur ")
    public ResponseEntity <String> enableAdmin(@PathVariable String id) throws Exception{
    
        acteurService.enableActeur(id);
        return new ResponseEntity<>("Acteur activer avec succes", HttpStatus.ACCEPTED);
    }


    //liste acteur par type acteur
    @GetMapping("/listeByTypeActeur/{id}")
    @Operation(summary = "affichage de la liste des acteur par type acteur")
    public ResponseEntity<List<Acteur>> listeActeurByTypeActeur(@PathVariable String id){
        return  new ResponseEntity<>(acteurService.getAllActeurByTypeActeur(id), HttpStatus.OK);
    }

           //Supprimer un acteur
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression d'un acteur")
    public ResponseEntity<String> deleteActeur(@PathVariable String id){
        return new ResponseEntity<>(acteurService.deleteByIdActeur(id), HttpStatus.OK);
    }

    //Se connecter 
    @GetMapping("/login")
    @Operation(summary = "Connexion d'un Acteur ")
    public Acteur connexion(@RequestParam("emailActeur")  String emailActeur,
                            @RequestParam("password")  String password) {
        return acteurService.connexionActeur(emailActeur, password);
    }

    @PostMapping("/connexion")
    @Operation(summary = "Connexion d'un Acteur ")
    public Acteur connexionActeur(@RequestBody Map<String, String> loginData) {
        String emailActeur = loginData.get("emailActeur");
        String password = loginData.get("password");
        return acteurService.loginActeur(emailActeur, password);
    }
    

    //Se connecter 
    @GetMapping("/pinLogin")
    @Operation(summary = "Connexion d'un Acteur")
    public Acteur connexionActeurWithPin(
        @RequestParam("codeActeur")  String codeActeur,
        @RequestParam("password")  String password
    ) {
    return acteurService.connexionActeurWithPin(codeActeur,password);
    }

    @GetMapping("/codeAndNomActeurLogin")
    @Operation(summary = "Connexion d'un Acteur via code et nomActeur")
    public Acteur connexionActeurWithCodeAndNomAceur(
        @RequestParam("codeActeur")  String codeActeur,
        @RequestParam("nomActeur")  String nomActeur
    ) {
    return acteurService.connexionActeurWithCodeAndNomActeur(codeActeur,nomActeur);
    }

    
}