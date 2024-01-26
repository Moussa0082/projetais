package projet.ais.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import java.util.Random;
import java.time.LocalDate;
import java.util.Date;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import projet.ais.Exception.NoContentException;
import projet.ais.models.Acteur;
import projet.ais.models.Alerte;
import projet.ais.models.TypeActeur;
import projet.ais.repository.ActeurRepository;
import projet.ais.repository.TypeActeurRepository;

@Service
public class ActeurService {

    @Autowired
    private ActeurRepository acteurRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;


    @Autowired
    EmailService emailService;

    @Autowired
    private TypeActeurRepository typeActeurRepository;

    //créer un user
      public Acteur createActeur(Acteur acteur, MultipartFile imageFile1, MultipartFile imageFile2) throws Exception {
        
     
         
        if (acteurRepository.findByEmailActeur(acteur.getEmailActeur()) == null) {
          
            if (acteur.getTypeActeur() == null) {
                throw new Exception("Veuillez choisir un type d'acteur pour créer un compte");
            }
    
            // // Vérifier si le type d'acteur est valide
             

            //On hashe le mot de passe
            String passWordHasher = passwordEncoder.encode(acteur.getPassword());
            acteur.setPassword(passWordHasher);

            // Traitement du fichier image siege acteur
            if (imageFile1 != null) {
                String imageLocation = "C:\\xampp\\htdocs\\ais";
                try {
                    Path imageRootLocation = Paths.get(imageLocation);
                    if (!Files.exists(imageRootLocation)) {
                        Files.createDirectories(imageRootLocation);
                    }
    
                    String imageName = UUID.randomUUID().toString() + "_" + imageFile1.getOriginalFilename();
                    Path imagePath = imageRootLocation.resolve(imageName);
                    Files.copy(imageFile1.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                    acteur.setPhotoSiegeActeur("ais/" + imageName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }
            // image logo acteur 
            if (imageFile2 != null) {
                String imageLocation = "C:\\xampp\\htdocs\\ais";
                try {
                    Path imageRootLocation = Paths.get(imageLocation);
                    if (!Files.exists(imageRootLocation)) {
                        Files.createDirectories(imageRootLocation);
                    }
    
                    String imageName = UUID.randomUUID().toString() + "_" + imageFile2.getOriginalFilename();
                    Path imagePath = imageRootLocation.resolve(imageName);
                    Files.copy(imageFile2.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                    acteur.setLogoActeur("ais/" + imageName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }

            String codeActeur = genererCode();
            acteur.setCodeActeur(codeActeur);
            acteur.setStatutActeur(false);

                
            Acteur savedActeur = acteurRepository.save(acteur);
            String msg = " " + savedActeur.getNomActeur().toUpperCase() +  " viens de créer un compte veuiller activer son compte !" ;
            Acteur acteurExistant = acteurRepository.findByTypeActeur(acteur.getTypeActeur());
            if(acteurExistant.getTypeActeur().getLibelle() == "Admin"){
              String mail = acteurExistant.getEmailActeur();
              Alerte alerte = new Alerte(mail, msg, "Création d'un nouveau compte");
              emailService.sendSimpleMail(alerte);
            }
            return savedActeur;
        } 
        else {
            throw new IllegalArgumentException("L'email " + acteur.getEmailActeur() + " existe déjà");
        }
    }


    
public String genererCode() {
    // Générer 2 lettres aléatoires
    String lettresAleatoires = genererLettresAleatoires(2);

    // Générer 3 chiffres aléatoires
    String chiffresAleatoires = genererChiffresAleatoires(3);



    // Concaténer les parties pour former le code final
    String codeFinal = lettresAleatoires + chiffresAleatoires ;

    return codeFinal;
}

private String genererLettresAleatoires(int longueur) {
    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    return genererChaineAleatoire(alphabet, longueur);
}

private String genererChiffresAleatoires(int longueur) {
    String chiffres = "0123456789";
    return genererChaineAleatoire(chiffres, longueur);
}

private String genererChaineAleatoire(String source, int longueur) {
    Random random = new Random();
    StringBuilder resultat = new StringBuilder();
    for (int i = 0; i < longueur; i++) {
        int index = random.nextInt(source.length());
        resultat.append(source.charAt(index));
    }
    return resultat.toString();
}
    
    //créer un user
      public Acteur updateActeur(Acteur acteur, Integer id, MultipartFile imageFile1, MultipartFile imageFile2) throws Exception {
        // TypeActeur typeActeur = typeActeurRepository.findByIdTypeActeur(acteur.getTypeActeur());
        Acteur ac = acteurRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Acteur non trouver avec l'id " + id));
        
                     // Traitement du fichier image siege acteur
                     if (imageFile1 != null) {
                        String imageLocation = "C:\\xampp\\htdocs\\ais";
                        try {
                            Path imageRootLocation = Paths.get(imageLocation);
                            if (!Files.exists(imageRootLocation)) {
                                Files.createDirectories(imageRootLocation);
                            }
            
                            String imageName = UUID.randomUUID().toString() + "_" + imageFile1.getOriginalFilename();
                            Path imagePath = imageRootLocation.resolve(imageName);
                            Files.copy(imageFile1.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                            ac.setPhotoSiegeActeur("ais/" + imageName);
                        } catch (IOException e) {
                            throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                        }
                    }
                    // image logo acteur 
                    if (imageFile2 != null) {
                        String imageLocation = "C:\\xampp\\htdocs\\ais";
                        try {
                            Path imageRootLocation = Paths.get(imageLocation);
                            if (!Files.exists(imageRootLocation)) {
                                Files.createDirectories(imageRootLocation);
                            }
            
                            String imageName = UUID.randomUUID().toString() + "_" + imageFile2.getOriginalFilename();
                            Path imagePath = imageRootLocation.resolve(imageName);
                            Files.copy(imageFile2.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                            ac.setLogoActeur("ais/" + imageName);
                        } catch (IOException e) {
                            throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                        }
                    }
            

         ac.setAdresseActeur(acteur.getAdresseActeur());
         ac.setNomActeur(acteur.getNomActeur());
         ac.setTelephoneActeur(acteur.getTelephoneActeur());
         ac.setWhatsAppActeur(acteur.getWhatsAppActeur());
         ac.setLocaliteActeur(acteur.getLocaliteActeur());
         ac.setEmailActeur(acteur.getEmailActeur());
         ac.setMaillonActeur(acteur.getMaillonActeur());
         ac.setFiliereActeur(acteur.getFiliereActeur());
         ac.setTypeActeur(acteur.getTypeActeur());


    // Mettez à jour le mot de passe si un nouveau mot de passe est fourni
    if (acteur.getPassword() != null && !acteur.getPassword().isEmpty()) {
        String hashedPassword = passwordEncoder.encode(acteur.getPassword());
        ac.setPassword(hashedPassword);
    }
        
    
            


            return acteurRepository.save(ac);
        
        
    }
    

       //Recuperer la liste des Admins
     public List<Acteur> getAllActeur(){

        List<Acteur> acteurList = acteurRepository.findAll();

        acteurList = acteurList
                .stream().sorted((d1, d2) -> d2.getEmailActeur().compareTo(d1.getEmailActeur()))
                .collect(Collectors.toList());
        return acteurList;
    }

    //Desactiver un acteur

    public ResponseEntity<String> disableActeur(Integer id) {
        Optional<Acteur> acteur = acteurRepository.findById(id);
        if (acteur.isPresent()) {
            acteur.get().setStatutActeur(false);
            acteurRepository.save(acteur.get());
            Alerte alerte = new Alerte(acteur.get().getEmailActeur(), "Votre compte a été desactivé par l'administrateur vous ne pouvez plus acceder à votre compte " + "\n veuillez contacter l'administrateur ");
            emailService.sendSimpleMail(alerte);
            return new ResponseEntity<>("L'acteur " + acteur.get().getNomActeur() + " a été désactivé avec succès", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Admin non trouvé avec l'ID " + id, HttpStatus.BAD_REQUEST);
        }
    }
   
    //activer un acteur
    public ResponseEntity<String> enableActeur(Integer id) {
        Optional<Acteur> acteur = acteurRepository.findById(id);
        if (acteur.isPresent()) {
            acteur.get().setStatutActeur(true);
            acteurRepository.save(acteur.get());
             Alerte alerte = new Alerte(acteur.get().getEmailActeur(), "Votre compte a été activé par le super admin vous pouvez acceder votre compte");
            emailService.sendSimpleMail(alerte);
            return new ResponseEntity<>("Le compte de " + acteur.get().getNomActeur() +  " a été activé avec succès", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Acteur non trouvé avec l'ID " + id, HttpStatus.BAD_REQUEST);
        }
    }


     //Liste type acteur par acteur
    public List<Acteur> getAllActeurByTypeActeur(Integer id){
        List<Acteur>  acteurList = acteurRepository.findByTypeActeurIdTypeActeur(id);

        if(acteurList.isEmpty()){
            throw new EntityNotFoundException("Aucun acteur trouvé");
        }
        acteurList = acteurList
                .stream().sorted((d1, d2) -> d2.getTypeActeur().getDescriptionTypeActeur().compareTo(d1.getTypeActeur().getDescriptionTypeActeur()))
                .collect(Collectors.toList());
        return acteurList;
    } 


  
      //Supprimer acteur
    public String deleteByIdActeur(Integer id){
        Acteur acteur = acteurRepository.findByIdActeur(id);
        if(acteur == null){
            throw new EntityNotFoundException("Désolé l'acteur à supprimer n'existe pas");
        }
        acteurRepository.delete(acteur);
        return "Acteur supprimé avec succèss";
    }

}
