package projet.ais.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.nio.file.Path;

import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
import projet.ais.models.Acteur;
import projet.ais.models.Alerte;
import projet.ais.repository.ActeurRepository;
import projet.ais.repository.AlerteRepository;
import projet.ais.repository.TypeActeurRepository;

@Service
public class ActeurService {

    private static final long EXPIRE_TOKEN=30;


    @Autowired
    private ActeurRepository acteurRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AlerteRepository alerteRepository;

    @Autowired
    private TypeActeurRepository typeActeurRepository;

    private Map<String, LocalDateTime> verificationCodeTimestamps = new HashMap<>();


     @Autowired
     JavaMailSender javaMailSender;

     @Value("bane8251@gmail.com")
     String sender;


    @Autowired
    EmailService emailService;

    // @Autowired
    // private TypeActeurRepository typeActeurRepository;

    //créer un acteur
      public Acteur createActeur(Acteur acteur, MultipartFile imageFile1, MultipartFile imageFile2) throws Exception {
        
        
    // Vérifier si l'acteur a le même mail et le même type
    Acteur existingActeurAvecMemeType = acteurRepository.findByEmailActeurAndTypeActeur(acteur.getEmailActeur(), acteur.getTypeActeur());
    if (existingActeurAvecMemeType != null) {
        // Si un acteur avec le même email et type existe déjà
        throw new IllegalArgumentException("Un acteur avec le même email et type existe déjà");
    }
         
    // if (acteurRepository.findByEmailActeur(acteur.getEmailActeur()) == null) {
        
            if (acteur.getTypeActeur() == null) {
                throw new Exception("Veuillez choisir un type d'acteur pour créer un compte");
            }
            
            
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
            
            
            
            
            // Enregistrement de l'acteur
    if ((acteur.getTypeActeur().getLibelle()) == "Admin") {
        System.out.println(acteur.getTypeActeur().getLibelle());
        acteur.setStatutActeur(true);
    } else {
        acteur.setStatutActeur(false);
    }

    Date d = new Date(); 
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
     String dt = sdf.format(d);
     acteur.setDateAjout(dt);
            Acteur savedActeur = acteurRepository.save(acteur);
            Acteur admins = acteurRepository.findByTypeActeurLibelle("Admin");
               if(admins != null && admins.getTypeActeur().getLibelle() != "Admin"){
              System.out.println(admins.getEmailActeur());
              // Envoyer un email à chaque administrateur
              String msg = savedActeur.getNomActeur().toUpperCase() + " vient de créer un compte. Veuillez activer son compte dans les plus brefs délais !";
              // for (Acteur admin : admins) {
                  
                  Alerte alerte = new Alerte(admins.getEmailActeur(), msg, "Création d'un nouveau compte");
                  emailService.sendSimpleMail(alerte);
                //   System.out.println(emailService.sendSimpleMail(alerte));
               }  else{
                System.out.println("Acteur non trouver");
               }

            //     }
            
        
    
// }


    return savedActeur;
        // } 
        // else {
        //     throw new IllegalArgumentException("L'email " + acteur.getEmailActeur() + " existe déjà");
        // }
    }

    // public ResponseEntity<String> sendMailToAllUser(Acteur acteur){
    //     // Assuming you have a method to send emails
    //     Alerte alerte = new Alerte();
    //     // Iterate over all users and check if their type is not "Admin", then send an email
    //     for (Acteur user : getAllUsers(acteur)) {
    //         if (user.getTypeActeur().getLibelle() != "Admin") {
    //            Alerte al = new Alerte(user.getEmailActeur(), alerte.getMessage(), alerte.getSujet());
    //            alerteRepository.save(al);
    //            emailService.sendSimpleMail(al);
    //             // Your email sending logic goes here
    //         }
    //     }
    //     return new ResponseEntity<>("Email envoyé à tous les utilisateurs avec succès", HttpStatus.ACCEPTED);
    // }


    public ResponseEntity<String> sendMailToAllUser(Alerte alerte){
        // Assuming you have a method to send emails
        //  Acteur acteur = new Acteur();
        //  Alerte alerteExistant = alerteRepository.findByActeurIdActeur(alerte.getActeur().getIdActeur());
            // if (user.getTypeActeur().getLibelle() != "Admin") {
               Alerte al = new Alerte(alerte.getActeur().getEmailActeur(), alerte.getMessage(), alerte.getSujet());
               alerteRepository.save(al);
               emailService.sendSimpleMail(al);
                // Your email sending logic goes here
            
        // }
        return new ResponseEntity<>("Email envoyé à "+ alerte.getActeur().getEmailActeur() +" avec succès", HttpStatus.ACCEPTED);
    }

    // private List<Acteur> getAllUsers(Acteur acteur) {
    // List<Acteur> users = acteurRepository.findByTypeActeur(acteur.getTypeActeur());
    //     return users;
    // }


    
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
            
                    Date d = new Date(); 
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                     String dt = sdf.format(d);
                     ac.setDateModif(dt);
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



      //Fonction pour un email à un utilisateur
      public String verifyNewUserMail(String email, String sujet, String message) throws Exception {
        Acteur userVerif =  acteurRepository.findByEmailActeur(email);
        if (userVerif == null)
            throw new Exception("Cet email n'existe pas dans notre système");

        userVerif = new Acteur();
        userVerif.setEmailActeur(email);
        String code = getRandomNumberString();
        sendMail(userVerif,code);
        return code;
    }




    //Mot de pass oublier 


     //Fonction pour verifier l'email du nouveau utilisateur s'il existe déjà
    public String verifyNewUserMail(String email) throws Exception {
        Acteur userVerif =  acteurRepository.findByEmailActeur(email);
        if (userVerif != null)
            throw new Exception("exist");

        userVerif = new Acteur();
        userVerif.setEmailActeur(email);
        String code = getRandomNumberString();
        sendMail(userVerif,code);
        return code;
    }


    //Envoi email à un utilisateur specifique
    public String sendToUser(String mail, String sujet, String message) throws Exception {
        Acteur userVerif =  acteurRepository.findByEmailActeur(mail);
        if (userVerif == null){

            throw new Exception("Cet email n'existe pas");
        }else{

    sendMailAuUser(mail, sujet, message);
            return "Email correcte";
        }
         
    }

    // fin logique email à un utilisateur specifique
   



    //Fonction pour modifer un utilisateur
    // public Utilisateur updateUser(Utilisateur utilisateur, String... oldPass){
    //     Utilisateur userVerif = utilisateurRepository.findByIdUtilisateur(utilisateur.getIdUtilisateur());
    //     if (userVerif == null)
    //         throw new NotFoundException("invalid");

    //     if (oldPass.length != 0){
    //         if(!passwordEncoder.matches(oldPass[0], userVerif.getMotDePasse()))
    //             throw new NotFoundException("old invalid");
    //         utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
    //         return utilisateurRepository.save(utilisateur);
    //     }
    //     return utilisateurRepository.save(utilisateur);
    // }

    //Fonction pour verifier l'email de l'utilisateur
    String code = getRandomNumberString();
    public String verifyUserEmail(String email) throws Exception {
        Acteur userVerif = acteurRepository.findByEmailActeur(email);
        if (userVerif == null)
            throw new Exception("invalid");

        sendMail(userVerif,code);
        return code;
    }

     // Fonction pour vérifier si le code est expiré
     private boolean isCodeExpired(String code) {
        LocalDateTime timestamp = verificationCodeTimestamps.get(code);
        if (timestamp == null) {
            return true; // Code non trouvé (expiré)
        }

        // Comparer avec le timestamp actuel
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(timestamp, now);
        return duration.getSeconds() > 30; // Code expiré après 30 secondes
    }

    //Fonction pour reinitialiser le mot de passe
    public Acteur resetPassword(String email, String password) throws Exception{
        Acteur userVerif = acteurRepository.findByEmailActeur(email);
        // Vérifier si le code est expiré
        if (isCodeExpired(code)) {
            throw new Exception("Code expiré");
        }
        
        userVerif.setPassword(passwordEncoder.encode(password));

        return acteurRepository.save(userVerif);
    }

    //Fonction pour générer 6 chiffres en chaîne de caractère
    private String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    private void sendMail(Acteur acteur, String code) throws Exception {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        try {
            mailMessage.setFrom(sender);
            mailMessage.setTo(acteur.getEmailActeur());
            mailMessage.setText("Votre code de verification est "+code);
            mailMessage.setSubject("Validation email");

            javaMailSender.send(mailMessage);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    private void sendMailAuUser(String email, String sujet, String message) throws Exception {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        try {
            mailMessage.setFrom(sender);
            mailMessage.setTo(email);
            mailMessage.setText(message);
            mailMessage.setSubject(sujet);

            javaMailSender.send(mailMessage);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }


  
    // public String forgotPass(Acteur acteur){
    //     Acteur userOptional = acteurRepository.findByEmailActeur(acteur.getEmailActeur());

    //     // if(userOptional == null){
    //     //     return "Cet email n'existe pas dans notre base de données ";
    //     // }
    //     if(userOptional.getEmailActeur() == null){
    //             return "Cet email n'existe pas dans notre base de données ";
            

    //     }else{
    //         System.out.println(acteur.getEmailActeur());
    //         System.out.println("Email non trouver");
    //     }

    //     Acteur user= userOptional;
    //     String tk = genererCode();
    //     user.setResetToken(tk);
    //     user.setTokenCreationDate(LocalDateTime.now());
    //   // Envoyer le code de réinitialisation à l'utilisateur (par exemple, par e-mail ou SMS)
    //   Alerte al = new Alerte(acteur.getEmailActeur(), "Vueiller saisir le code de confirmation "+ tk +" qui \n expire dans 30 s pour passer à l'etape de réinitialisation de votre mot de passe", "Code de confirmation");
    //   emailService.sendSimpleMail(al);
    //     user=acteurRepository.save(user);
    //     return user.getResetToken();
    // }

    // public String resetPass(String resetToken, String password){
    //     Optional<Optional<Acteur>> userOptional= Optional.ofNullable(acteurRepository.findByResetToken(resetToken));

    //     if(!userOptional.isPresent()){
    //         return "Code invalide";
    //     }
    //     LocalDateTime tokenCreationDate = userOptional.get().get().getTokenCreationDate();

    //     if (isTokenExpired(tokenCreationDate)) {
    //         return "Code expiré.";

    //     }

    //     Acteur user = userOptional.get().get();

    //     user.setPassword(password);
    //     user.setResetToken(null);
    //     user.setTokenCreationDate(null);

    //     acteurRepository.save(user);

    //     return "Mot de passe modifier avec succès.";
    // }

    // private String generateToken() {
    //     StringBuilder token = new StringBuilder();

    //     return token.append(UUID.randomUUID().toString())
    //             .append(UUID.randomUUID().toString()).toString();
    // }

    // private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {

    //     LocalDateTime now = LocalDateTime.now();
    //     Duration diff = Duration.between(tokenCreationDate, now);

    //     return diff.toMinutes() >=EXPIRE_TOKEN;
    // }

    // fin logique service mot de passe oublier 
   
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
