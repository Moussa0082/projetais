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
import java.nio.file.Path;

import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

import projet.ais.IdGenerator;
import projet.ais.Exception.NoContentException;
import projet.ais.models.Acteur;
import projet.ais.models.Alerte;
import java.time.format.DateTimeFormatter;

import projet.ais.models.Stock;
import projet.ais.models.TypeActeur;
import projet.ais.repository.ActeurRepository;
import projet.ais.repository.AlerteRepository;
import projet.ais.repository.TypeActeurRepository;

@Service
public class ActeurService {

    private static final long EXPIRE_TOKEN=30;


    @Autowired
     ActeurRepository acteurRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AlerteRepository alerteRepository;

    @Autowired
     MessageService messageService;

    @Autowired
    private TypeActeurRepository typeActeurRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    IdGenerator idGenerator ;
    

    private Map<String, LocalDateTime> verificationCodeTimestamps = new HashMap<>();


     @Autowired
     JavaMailSender javaMailSender;

     @Value("bane8251@gmail.com")
     String sender;


   

    // @Autowired
    // private TypeActeurRepository typeActeurRepository;

    //créer un acteur
      public Acteur createActeur(Acteur acteur, MultipartFile imageFile1, MultipartFile imageFile2) throws Exception {
        
        Acteur id = acteurRepository.findByIdActeur(acteur.getIdActeur());
        if(id != null){

            throw new IllegalArgumentException("Un acteur avec l'id " + id + " existe déjà");
        }
        
        // Vérifier si l'acteur a le même mail et le même type
        Acteur existingActeurAvecMemeType = acteurRepository.findByEmailActeurAndTypeActeurIn(acteur.getEmailActeur(), acteur.getTypeActeur());
        if (existingActeurAvecMemeType != null) {
            // Si un acteur avec le même email et type existe déjà
            throw new IllegalArgumentException("Un acteur avec le même email et type existe déjà");
        }
         
    // if (acteurRepository.findByEmailActeur(acteur.getEmailActeur()) == null) {
        
            if (acteur.getTypeActeur() == null) {
                throw new Exception("Veuillez choisir un type d'acteur pour créer un compte");
            }else{
                for (TypeActeur typeActeur : acteur.getTypeActeur()) {
                    if (typeActeur != null && typeActeur.getLibelle() != null && (typeActeur.getLibelle() == "Admin"  || typeActeur.getLibelle() == "admin")) {
                        acteur.setStatutActeur(true);
                        break; // Sortie de la boucle dès que "Admin" est trouvé
                    }else{
                        
                        acteur.setStatutActeur(false);
                        break; 
                    }
                }
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
            String code = idGenerator.genererCode();
            acteur.setCodeActeur(codeActeur);
            String pattern = "yyyy-MM-dd HH:mm";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(formatter);
            acteur.setDateAjout(formattedDateTime);
            acteur.setIdActeur(code);
            acteur.setWhatsAppActeur(acteur.getWhatsAppActeur());
           
            // Acteur admins = acteurRepository.findByTypeActeurLibelle("Admin");

            // Enregistrement de l'acteur
            // boolean isAdmin = false;
            if (acteur.getTypeActeur() != null) {
                for (TypeActeur typeActeur : acteur.getTypeActeur()) {
                    if (typeActeur != null && typeActeur.getLibelle() != null && typeActeur.getLibelle() == "Admin") {
                        acteur.setStatutActeur(true);
                        break; // Sortie de la boucle dès que "Admin" est trouvé
                    }else{
                        acteur.setStatutActeur(false);
                    }
                }
            }
            
            Acteur savedActeur = acteurRepository.save(acteur);
            
            
            // // Envoyer un e-mail à l'administrateur si un acteur "Admin" a été trouvé
            // if (admins != null) { // Vérifiez si des administrateurs ont été trouvés
            //     System.out.println(admins.getEmailActeur());
            //     for (TypeActeur adminType : admins.getTypeActeur()) {
            //         if (adminType.getLibelle().equals("Admin")) {
            //             // Si un administrateur est trouvé, envoyez un e-mail
            //             String msg = savedActeur.getNomActeur().toUpperCase() + " vient de créer un compte. Veuillez activer son compte dans les plus brefs délais !";
            //             Alerte alerte = new Alerte(admins.getEmailActeur(), msg, "Création d'un nouveau compte");
            //             alerteRepository.save(alerte);
            //             emailService.sendSimpleMail(alerte);
            //             messageService.sendMessageAndSave(admins.getEmailActeur(), msg, savedActeur);
            //             System.out.println(admins.getEmailActeur());
            //             break; // Sortez de la boucle dès qu'un administrateur est trouvé
            //         }
            //     }
            // } else {
            //     System.out.println("Aucun administrateur trouvé"); // Gérez le cas où aucun administrateur n'est trouvé
            // }

          // Récupérez l'administrateur
            Acteur admin = acteurRepository.findByTypeActeurLibelle("Admin");


            // Vérifiez si un administrateur a été trouvé
            if (admin != null) {
                // Accédez aux types d'acteurs de l'administrateur
                List<TypeActeur> typeActeurs = admin.getTypeActeur();
                if (typeActeurs != null) {
                    for (TypeActeur typeActeur : typeActeurs) {
                        if (typeActeur.getLibelle().equals("Admin")) {
                            // Si l'administrateur a le type "Admin", envoyez un e-mail
                            String msg = savedActeur.getNomActeur().toUpperCase() + " vient de créer un compte. Veuillez le contacter à son numero "+ savedActeur.getWhatsAppActeur()+"pour proceder à l'activation de son compte dans les plus brefs délais !";
                            Alerte alerte = new Alerte(admin.getEmailActeur(), msg, "Création d'un nouveau compte");
                            alerteRepository.save(alerte);
                            emailService.sendSimpleMail(alerte);
                            messageService.sendMessagePersonnalAndSave(admin.getWhatsAppActeur(), msg);
                            System.out.println(admin.getWhatsAppActeur());
                            break; // Sortez de la boucle dès qu'un administrateur est trouvé
                        }
                    }
                }
            } else {
                System.out.println("Aucun administrateur trouvé");
            }
            
            sendMessageToAdmin(savedActeur);

            System.out.println("Acteur :" + savedActeur.toString());
                     
            return savedActeur;
               
    }

 public ResponseEntity<String> sendMessageToAdmin(Acteur acteur) throws Exception {

    Acteur admins = acteurRepository.findByTypeActeurLibelle("Admin");

    if (admins != null) { // Vérifiez si des administrateurs ont été trouvés
        for (TypeActeur adminType : admins.getTypeActeur()) {
            if (adminType.getLibelle().equals("Admin")) {
                // Si un administrateur est trouvé, envoyez un e-mail
           String msg = acteur.getNomActeur().toUpperCase() + " vient de créer un compte. Veuillez activer son compte dans les plus brefs délais !";
           try {
            messageService.sendMessageAndSave(admins.getWhatsAppActeur(), msg,admins);
           } catch (Exception e) {
             throw new Exception("Erreur lors de l'envoie de message wathsapp : " +e.getMessage());
           }
                break; // Sortez de la boucle dès qu'un administrateur est trouvé
            }
        }
    } else {
        System.out.println("Aucun administrateur trouvé"); // Gérez le cas où aucun administrateur n'est trouvé
    }
        
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    // public ResponseEntity<String> sendMessageWaToAdmin(String message, String acteur) throws Exception {

    //     Acteur admins = acteurRepository.findByTypeActeurLibelle("Admin");
    
    //     if (admins != null) { // Vérifiez si des administrateurs ont été trouvés
    //                 // Si un administrateur est trouvé, envoyez un e-mail
    //            try {
    //             messageService.sendMessageAndSave(admins.getWhatsAppActeur(), message, acteur);
    //            } catch (Exception e) {
    //              throw new Exception("Erreur lors de l'envoie de message wathsapp : " +e.getMessage());
    //            }
            
    //     } else {
    //         System.out.println("Aucun administrateur trouvé"); // Gérez le cas où aucun administrateur n'est trouvé
    //     }
            
    //         return new ResponseEntity<>(HttpStatus.ACCEPTED);
    //     }
   
    public Acteur addTypesToActeur(String idActeur, List<TypeActeur> typeActeurs) throws Exception{
        Acteur acteur = acteurRepository.findByIdActeur(idActeur);
        if (acteur == null) {
            // Gérer le cas où aucun acteur n'est trouvé avec cet e-mail
            throw new IllegalArgumentException("Aucun acteur n'est trouvé avec cet email");
        }
        

        // Associer les types d'acteur à l'acteur existant
        acteur.getTypeActeur().addAll(typeActeurs);
        
        // Enregistrer les modifications
        return acteurRepository.save(acteur);
    }
    

    //envoi mail aux users
    public ResponseEntity<Void> sendMailToAllUser(String email, String sujet, String message){
        
        List<Acteur> allActeurs = acteurRepository.findAllByEmailActeur(email);

        // Envoyer un e-mail aux autres acteurs 
        for (Acteur ac : allActeurs) {
            for (TypeActeur typeActeur : ac.getTypeActeur()) {
                if (!typeActeur.getLibelle().equals("Admin")) {
                    // Envoyer un e-mail pour chaque type d'acteur différent de "Admin"
                    Alerte alerte = new Alerte(ac.getEmailActeur(), message, sujet);
                    emailService.sendSimpleMail(alerte);
                    System.out.println(ac.getEmailActeur());
                    break; // Sortez de la boucle interne dès qu'un type d'acteur différent de "Admin" est trouvé
                }
            }
        }
        
            
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }



    //envoi mail aux user par type choisit par l'admin
        public ResponseEntity<Void> sendMailToAllUserChoose(String email, String sujet, String message, String libelle) {

            // Récupérer les acteurs avec le type d'acteur spécifié
            List<Acteur> allActeurs = acteurRepository.findByTypeActeur_Libelle(libelle);
          
            // Envoyer un e-mail à chaque acteur
            for (Acteur ac : allActeurs) {
                Alerte alerte = new Alerte(ac.getEmailActeur(), message, sujet);
                alerteRepository.save(alerte);
                emailService.sendSimpleMail(alerte);
                System.out.println("Email envoyé à " + ac.getEmailActeur());
            }
          
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        

    //envoi mail aux user par types par exemple producteur et commerçant etc.. le nombre de type  choisit par l'admin
    public ResponseEntity<Void> sendMailToAllUserCheckedChoose(String email, String sujet, String message, List<String> libelles) {

        // Set to store unique email addresses to avoid duplicates
        Set<String> emailsSent = new HashSet<>();
    
        // Retrieve actors for each type label
        List<Acteur> allActeurs = new ArrayList<>();
        for (String libelle : libelles) {
            List<Acteur> acteurs = acteurRepository.findByTypeActeur_Libelle(libelle);
            allActeurs.addAll(acteurs);
        }
    
        // Send emails to each actor
        for (Acteur acteur : allActeurs) {
            String emailActeur = Objects.requireNonNullElse(acteur.getEmailActeur(), "");
    
            // Skip if email is null, empty, or already sent
            if (!emailsSent.contains(emailActeur) && !emailActeur.isEmpty()) {
                try {
                    Alerte alerte = new Alerte(emailActeur, message, sujet);
                    emailService.sendSimpleMail(alerte);
                    emailsSent.add(emailActeur);
                    System.out.println("Email sent to " + emailActeur);
                } catch (Exception e) {
                    // Handle email sending error
                    System.err.println("Error sending email to " + emailActeur + ": " + e.getMessage());
                }
            }
        }
    
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    
       //envoi message watsApp aux user par types par exemple producteur et commerçant etc.. le nombre de type  choisit par l'admin
    // public ResponseEntity<Void> sendMessageToActeurByTypeActeur(String message, List<String> libelles) {

    //     // Set to store unique email addresses to avoid duplicates
    //     Set<String> wathsappSend = new HashSet<>();
    
    //     // Retrieve actors for each type label
    //     List<Acteur> allActeurs = new ArrayList<>();
    //     for (String libelle : libelles) {
    //         List<Acteur> acteurs = acteurRepository.findByTypeActeur_Libelle(libelle);
    //         allActeurs.addAll(acteurs);
    //     }
    
        
    //     for (Acteur acteur : allActeurs) {
    //         String wathsapp = Objects.requireNonNullElse(acteur.getWhatsAppActeur(), "");
    
    //         // Skip if email is null, empty, or already sent
    //         if (!wathsappSend.contains(wathsapp) && !wathsapp.isEmpty()) {
    //             try {
    //                messageService.sendMessageAndSave(acteur.getWhatsAppActeur(), message, acteur.getNomActeur());
    //                 wathsappSend.add(wathsapp);
    //                 System.out.println("Message sent to " + wathsapp);
    //             } catch (Exception e) {
    //                 // Handle email sending error
    //                 System.err.println("Error sending message to " + wathsapp + ": " + e.getMessage());
    //             }
    //         }
    //     }
    
    //     return new ResponseEntity<>(HttpStatus.ACCEPTED);
    // }
       //envoi message watsApp aux user par types par exemple producteur et commerçant etc.. le nombre de type  choisit par l'admin
    public ResponseEntity<Void> sendMessageToActeurByTypeActeur(String message, List<String> libelles) {

        // Set to store unique email addresses to avoid duplicates
        Set<String> wathsappSend = new HashSet<>();
    
        // Retrieve actors for each type label
        List<Acteur> allActeurs = new ArrayList<>();
        for (String libelle : libelles) {
            List<Acteur> acteurs = acteurRepository.findByTypeActeur_Libelle(libelle);
            allActeurs.addAll(acteurs);
        }
    
        
        for (Acteur acteur : allActeurs) {
            String wathsapp = Objects.requireNonNullElse(acteur.getWhatsAppActeur(), "");
    
            // Skip if email is null, empty, or already sent
            if (!wathsappSend.contains(wathsapp) && !wathsapp.isEmpty()) {
                try {
                   messageService.sendMessageAndSave(acteur.getWhatsAppActeur(), message, acteur);
                    wathsappSend.add(wathsapp);
                    System.out.println("Message sent to " + wathsapp);
                } catch (Exception e) {
                    // Handle email sending error
                    System.err.println("Error sending message to " + wathsapp + ": " + e.getMessage());
                }
            }
        }
    
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
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
      public Acteur updateActeur(Acteur acteur, String id, MultipartFile imageFile1, MultipartFile imageFile2) throws Exception {
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
            
                    // Date d = new Date(); 
                    // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    //  String dt = sdf.format(d);
        ac.setDateModif(LocalDateTime.now().toString());
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

    public ResponseEntity<String> disableActeur(String id) throws Exception {
        Optional<Acteur> acteur = acteurRepository.findById(id);
        if (acteur.isPresent()) {
            acteur.get().setStatutActeur(false);
            acteurRepository.save(acteur.get());
            Alerte alerte = new Alerte(acteur.get().getEmailActeur(), "Votre compte a été desactivé par l'administrateur vous ne pouvez plus acceder à votre compte veuillez contacter l'administrateur " , "Desactivation de compte par l'administrateur de koumi");
            alerteRepository.save(alerte);
            emailService.sendSimpleMail(alerte);
            messageService.sendMessagePersonnalAndSave(acteur.get().getWhatsAppActeur(), "Votre compte a été desactivé par l'administrateur vous ne pouvez plus acceder à votre compte veuillez contacter l'administrateur ");
            return new ResponseEntity<>("L'acteur " + acteur.get().getNomActeur() + " a été désactivé avec succès", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Admin non trouvé avec l'ID " + id, HttpStatus.BAD_REQUEST);
        }
    }



      //Fonction pour un email à un utilisateur
    //   public String verifyNewUserMail(String email, String sujet, String message) throws Exception {
    //     Acteur userVerif =  acteurRepository.findByEmailActeur(email);
    //     if (userVerif == null)
    //         throw new Exception("Cet email n'existe pas dans notre système");

    //     userVerif = new Acteur();
    //     userVerif.setEmailActeur(email);
    //     String code = getRandomNumberString();
    //     sendMail(userVerif,code);
    //     return code;
    // }




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
   

    //Fonction pour envoyer un code de verification à l'email de l'utilisateur
    String code = getRandomNumberString();

    // Mot de passe oublié : envoyer un code à l'utilisateur par e-mail
public String sendOtpCodeEmail(String email) throws Exception {
    Acteur userVerif = acteurRepository.findByEmailActeur(email);
    if (userVerif == null) {
        throw new Exception("Cet email n'existe pas, veuillez vérifier l'email saisi");
    }
    
    // Générez le code
    String code = getRandomNumberString();
    
    // Enregistrez le code et son horodatage dans la base de données
    userVerif.setResetToken(code);
    userVerif.setTokenCreationDate(LocalDateTime.now().plusMinutes(2)); // Code expirera après 2 minute (à adapter selon vos besoins)
    acteurRepository.save(userVerif);
    
    // Envoyez le code par e-mail
    sendMail(userVerif, code);
    
    return code;
}


    //Mot de pass oublier envoyer un code au user par whatts app
    public String sendOtpCodeWhatsApp(String whatsAppActeur) throws Exception {
        Acteur userVerif = acteurRepository.findByWhatsAppActeur(whatsAppActeur);
        if (userVerif == null)
        throw new Exception("Ce numero n'existe pas, verifier  le numéro saisi");
         // Stockez temporairement le code dans le champ resetToken de l'utilisateur
        userVerif.setResetToken(code);
        userVerif.setTokenCreationDate(LocalDateTime.now().plusMinutes(2)); // Code expirera après 2 minutes
        acteurRepository.save(userVerif);
          String msg = "Votre code de verification temporaire est " + code + " veuillez garder ce code pour vous uniquement si vous n'avez pas demander à changer de mot de passe veuiilez ignorer ce message";
            messageService.sendMessageAndSave(whatsAppActeur, msg, userVerif);
        return code;
    }

    // Vérifier le code envoyé par e-mail
    public boolean verifyOtpCodeEmail(String emailActeur, String resetToken) {
    Acteur userVerif = acteurRepository.findByEmailActeurAndResetToken(emailActeur, resetToken);
    if (userVerif == null) {
        throw new RuntimeException("Code incorrect ou expiré");
    }
    
    // Vérifiez si le code est expiré
    LocalDateTime tokenCreationDate = userVerif.getTokenCreationDate();
    if (tokenCreationDate == null || tokenCreationDate.isBefore(LocalDateTime.now().minusMinutes(1))) {
        // Code expiré
        throw new RuntimeException("Code expiré");
    }
    
    // Réinitialisez le token et la date de création
    userVerif.setResetToken(null);
    userVerif.setTokenCreationDate(null);
    acteurRepository.save(userVerif);
    
    return true; // Code vérifié avec succès
}
    // Vérifier le code envoyé par whats app
    public boolean verifyOtpCodeWhtasApp(String whatsAppActeur, String resetToken) {
    Acteur userVerif = acteurRepository.findByEmailActeurAndResetToken(whatsAppActeur, resetToken);
    if (userVerif == null) {
        throw new RuntimeException("Code incorrect ou expiré");
    }
    
    // Vérifiez si le code est expiré
    LocalDateTime tokenCreationDate = userVerif.getTokenCreationDate();
    if (tokenCreationDate == null || tokenCreationDate.isBefore(LocalDateTime.now().minusMinutes(2))) {
        // Code expiré
        throw new RuntimeException("Code expiré");
    }
    
    // Réinitialisez le token et la date de création
    userVerif.setResetToken(null);
    userVerif.setTokenCreationDate(null);
    acteurRepository.save(userVerif);
    
    return true; // Code vérifié avec succès
}


    public boolean verifyOtpCodeWhatsApp(String whatsAppActeur,String code ) {
        if (isCodeExpired(code)) {
            throw new RuntimeException("Code expiré");
        }
        
        Acteur userVerif = acteurRepository.findByWhatsAppActeur(whatsAppActeur);
        if (userVerif == null) {
            throw new RuntimeException("Ce numéro n'existe pas, veuillez vérifier le numéro saisi");
        }
    
        // Stockez temporairement le code dans le champ resetToken de l'utilisateur
        userVerif.setResetToken(code);
        userVerif.setTokenCreationDate(LocalDateTime.now().plusMinutes(2)); // Code expirera après 2 minutes
        acteurRepository.save(userVerif);
    
        return true; // Code vérifié avec succès
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
        return duration.getSeconds() > 120; // Code expiré après 60 secondes
    }

    //Fonction pour reinitialiser le mot de passe par email
    public Acteur resetPasswordEmail(String email, String password) throws Exception{
        Acteur userVerif = acteurRepository.findByEmailActeur(email);
        // Vérifier si le code est expiré
        // if (isCodeExpired(code)) {
        //     throw new Exception("Code expiré");
        // }
        
        userVerif.setPassword(passwordEncoder.encode(password));

        return acteurRepository.save(userVerif);
    }
   //Function pour reinitialiser le mot de pass par whatsApp numéro
    public Acteur resetPasswordWhatsApp(String whatsAppActeur, String password) throws Exception{
        Acteur userVerif = acteurRepository.findByWhatsAppActeur(whatsAppActeur);
        // Vérifier si le code est expiré
        // if (isCodeExpired(code)) {
        //     throw new Exception("Code expiré");
        // }
        
        userVerif.setPassword(passwordEncoder.encode(password));

        return acteurRepository.save(userVerif);
    }

    //Fonction pour générer 6 chiffres en chaîne de caractère
    private String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(9999);

        // this will convert any number sequence into 4 character.
        return String.format("%04d", number);
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

    // Methode d'envoi d'email à un user 
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

    // fin methode d'envoi d'eamil à un user


  
   

    // fin logique service mot de passe oublier 
   
    //activer un acteur
    public ResponseEntity<String> enableActeur(String id) throws Exception {
        Optional<Acteur> acteur = acteurRepository.findById(id);
        if (acteur.isPresent()) {
            acteur.get().setStatutActeur(true);
            acteurRepository.save(acteur.get());
             Alerte alerte = new Alerte(acteur.get().getEmailActeur(), "Votre compte a été activé par le super admin vous pouvez acceder votre compte" , "Activation de compte par l'administrateur de koumi");
            alerteRepository.save(alerte);
             emailService.sendSimpleMail(alerte);
             messageService.sendMessagePersonnalAndSave(acteur.get().getWhatsAppActeur(), "Votre compte a été activé par le super admin vous pouvez acceder votre compte");
            return new ResponseEntity<>("Le compte de " + acteur.get().getNomActeur() +  " a été activé avec succès", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Acteur non trouvé avec l'ID " + id, HttpStatus.BAD_REQUEST);
        }
    }


     //Liste type acteur par acteur
     public List<Acteur> getAllActeurByTypeActeur(String id) {
        List<Acteur> acteurList = acteurRepository.findByTypeActeurIdTypeActeur(id);
    
        if (acteurList.isEmpty()) {
            throw new EntityNotFoundException("Aucun acteur trouvé");
        }
    
        // Trier les acteurs en fonction de la description de leur type d'acteur
        acteurList = acteurList.stream()
                .sorted((acteur1, acteur2) ->
                        acteur2.getTypeActeur().stream()
                                .map(TypeActeur::getDescriptionTypeActeur)
                                .findFirst().orElse("").compareTo(
                                        acteur1.getTypeActeur().stream()
                                                .map(TypeActeur::getDescriptionTypeActeur)
                                                .findFirst().orElse("")))
                .collect(Collectors.toList());
    
        return acteurList;
    }
    

      //Supprimer acteur
    public String deleteByIdActeur(String id){
        Acteur acteur = acteurRepository.findByIdActeur(id);
        if(acteur == null){
            throw new EntityNotFoundException("Désolé l'acteur à supprimer n'existe pas");
        }
        acteurRepository.delete(acteur);
        return "Acteur supprimé avec succèss";
    }


     //Se connecter 
      public Acteur connexionActeur(String emailActeur, String password){
        Acteur acteur = acteurRepository.findByEmailActeur(emailActeur);
        if (acteur == null || !passwordEncoder.matches(password, acteur.getPassword())) {
            throw new EntityNotFoundException("Email ou mot de passe incorrect");
        }
        
        if(acteur.getStatutActeur()==false){
            throw new NoContentException("Connexion échoué votre compte  est desactivé \n veuillez contacter l'administrateur pour la procedure d'activation de votre compte !");
        }
         return acteur;
    }
   
       


}
