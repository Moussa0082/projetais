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
import projet.ais.repository.ActeurRepository;
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



            // Mot de passe oublier 
   
            // @PostMapping("/forgot-password")
            // public String forgotPass(@RequestParam String email){
            //     String response = acteurService.forgotPass(email);
        
            //     if(!response.startsWith("Invalid")){
            //         response= "http://localhost:9000/reset-password?token=" + response;
            //     }
            //     return response;
            // }
        
            // @PutMapping("/reset-password")
            //     public String resetPass(@RequestParam String resetToken, @RequestParam String password){
            //         return acteurService.resetPass(resetToken,password);
            //     }
            // @PostMapping("/forgot-password")
            // public ResponseEntity<String> forgotPassword(@RequestBody Acteur acteur) {
            //     String resetToken = acteurService.forgotPass(acteur);
            //     if (resetToken == null) {
            //         return new ResponseEntity<>("Adresse e-mail invalide.", HttpStatus.BAD_REQUEST);
            //     } else {
            //         // // Envoyer le code de réinitialisation à l'utilisateur (par exemple, par e-mail ou SMS)
            //         // Alerte al = new Alerte(acteur.getEmailActeur(), "Vueiller saisir le code de confirmation "+ resetToken +" qui \n expire dans 30 s pour passer à l'etape de réinitialisation de votre mot de passe", "Code de confirmation");
            //         // emailService.sendSimpleMail(al);
            //         return new ResponseEntity<>(resetToken, HttpStatus.OK);
            //     }
            // }
        
            // @PostMapping("/reset-password")
            // public ResponseEntity<String> resetPassword(@RequestBody Acteur request) {
            //     String result = acteurService.resetPass(request.getResetToken(), request.getPassword());
            //     if (result.equals("Mot de passe modifié avec succès")) {
            //         return new ResponseEntity<>(result, HttpStatus.OK);
            //     } else {
            //         return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            //     }
            // }

                @GetMapping("/verifmail")
    @Operation(summary = "Verifier l'email de l'utilisateur en lui envoyant un code de verification dans son mail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "L'email exist et le cade a été envoyer avec succès", content = {
                    @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))
            }),
            @ApiResponse(responseCode = "500",description = "Erreur serveur", content = @Content),
    })
    public ResponseEntity<Object> verifierEmail(@RequestParam("email") String email) throws Exception {
        return ResponseHandler.generateResponse(acteurService.verifyUserEmail(email), HttpStatus.OK,null);
    }

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

    
             //Mettre à jour un acteur
      @PutMapping("/update/{id}")
      @Operation(summary = "Mise à jour d'un acteur ")
      public ResponseEntity<Acteur> updateActeur(
              @PathVariable Integer id,
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
    public ResponseEntity <String> disableActeur(@PathVariable Integer id){
    
        acteurService.disableActeur(id);
        return new ResponseEntity<>("Acteur desactiver avec succes", HttpStatus.ACCEPTED);
    }

    //Aciver admin
      @PutMapping("/enable/{id}")
    //Desactiver un admin methode
    @Operation(summary = "Activer acteur ")
    public ResponseEntity <String> enableAdmin(@PathVariable Integer id){
    
        acteurService.enableActeur(id);
        return new ResponseEntity<>("Acteur activer avec succes", HttpStatus.ACCEPTED);
    }


    //liste acteur par type acteur
    @GetMapping("/listeByTypeActeur/{id}")
    @Operation(summary = "affichage de la liste des acteur par type acteur")
    public ResponseEntity<List<Acteur>> listeActeurByTypeActeur(@PathVariable Integer id){
        return  new ResponseEntity<>(acteurService.getAllActeurByTypeActeur(id), HttpStatus.OK);
    }

           //Supprimer un acteur
           @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression d'un acteur")
    public ResponseEntity<String> deleteActeur(@PathVariable Integer id){
        return new ResponseEntity<>(acteurService.deleteByIdActeur(id), HttpStatus.OK);
    }

    
}
