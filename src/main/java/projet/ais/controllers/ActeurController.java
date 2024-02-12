package projet.ais.controllers;

import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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
import projet.ais.models.TypeActeur;
import projet.ais.repository.ActeurRepository;
import projet.ais.repository.TypeActeurRepository;
import projet.ais.services.ActeurService;
import projet.ais.services.EmailService;

@RestController
@RequestMapping("/acteur")
public class ActeurController {


    @Autowired
    private ActeurService acteurService;

    @Autowired
    private ActeurRepository acteurRepository;

    @Autowired
    private TypeActeurRepository typeActeurRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping("/create")
    @Operation(summary = "création d'un acteur")
     public ResponseEntity<Acteur> createActeur(
            @Valid @RequestParam("acteur") String adminString,
            @RequestParam(value = "image1", required = false) MultipartFile imageFile1,
            @RequestParam(value = "image2", required = false) MultipartFile imageFile2)
            throws Exception {
                

                Acteur acteur = new Acteur();
                try {
                    acteur = new JsonMapper().readValue(adminString, Acteur.class);
                } catch (JsonProcessingException e) {
                    throw new Exception(e.getMessage());
                }
            
                // je le cree et le sauvegarde.
                Acteur savedActeur = acteurService.createActeur(acteur, imageFile1, imageFile2);
            
                return new ResponseEntity<>(savedActeur, HttpStatus.CREATED);
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
                List<Map<String, String>> typeActeursMapList = (List<Map<String, String>>) requestBody.get("typeActeurs");
                
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
            

            


                @GetMapping("/verifmail")
    @Operation(summary = "Verifier l'email de l'utilisateur en lui envoyant un code de verification à son adresse email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "L'email exist et le code a été envoyer avec succès", content = {
                    @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))
            }),
            @ApiResponse(responseCode = "500",description = "Erreur serveur", content = @Content),
    })
    public ResponseEntity<Object> verifierEmail(@RequestParam("email") String email) throws Exception {
        return ResponseHandler.generateResponse(acteurService.verifyUserEmail(email), HttpStatus.OK,null);
    }

    //Envoyer un email à un utilisateur spécifique
    @GetMapping("/sendMailToUser")
    @Operation(summary = "Envoyer un mail à un utilisateur")
    public ResponseEntity<Object> sendMailToUser(@RequestParam ("email") String email, @RequestParam("sujet")String sujet, @RequestParam("message")String message) throws Exception {
        acteurService.sendToUser(email, sujet, message);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/resetpassword")
    @Operation(summary = "Réinitialise le mot de passe de l'utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Mot de passe réinitialisé avec succès", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Acteur.class))
            }),
            @ApiResponse(responseCode = "500",description = "Erreur serveur", content = @Content),
    })
    public ResponseEntity<Object> resetPassword(@RequestParam("email") String email, @RequestParam("password") String password) throws Exception {
        return ResponseHandler.generateResponse("succes", HttpStatus.OK,acteurService.resetPassword(email,password));
    }
            // fin logique  mot de passe oublier



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

    @GetMapping("/sendMessageWathsappToActeurByTypeActeur")
    public ResponseEntity<String> sendMessageWathsappToActeur(@RequestParam String message, @RequestParam List<String> libelles) {
        try {
            acteurService.sendMessageToActeurByTypeActeur(message, libelles);
            
            return new ResponseEntity<>("Message envoyé avec succès à tous les acteurs correspondant aux libellés " + libelles.size(), HttpStatus.OK);
        } catch (Exception e) {
            // En cas d'erreur, retourner une réponse avec un message d'erreur
            return new ResponseEntity<>("Échec de l'envoi du message WhatsApp aux acteurs correspondant aux libellés " + libelles + ". Erreur : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
             //Mettre à jour un acteur
    @PutMapping("/update/{id}")
    @Operation(summary = "Mise à jour d'un acteur ")
      public ResponseEntity<Acteur> updateActeur(
              @PathVariable String id,
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
            Acteur acteurMisAjour = acteurService.updateActeur(acteur, id, imageFile1, imageFile2);
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

   
    @PutMapping("/disable/{id}")
    //Desactiver un admin methode
    @Operation(summary = "Désactiver acteur ")
    public ResponseEntity <String> disableActeur(@PathVariable String id){
    
        acteurService.disableActeur(id);
        return new ResponseEntity<>("Acteur desactiver avec succes", HttpStatus.ACCEPTED);
    }

    //Aciver admin
      @PutMapping("/enable/{id}")
    @Operation(summary = "Activer acteur ")
    public ResponseEntity <String> enableAdmin(@PathVariable String id){
    
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

    
}
