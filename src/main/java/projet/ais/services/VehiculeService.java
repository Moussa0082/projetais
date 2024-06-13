package projet.ais.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.io.ByteArrayOutputStream;


// import org.apache.http.client.methods.HttpPost;
// import org.apache.http.entity.FileEntity;
// import org.apache.http.impl.client.HttpClients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Intrant;
import projet.ais.models.Stock;
import projet.ais.models.TypeMateriel;
import projet.ais.models.Vehicule;
import projet.ais.repository.VehiculeRepository;

// import org.apache.http.HttpEntity;
// import org.apache.http.HttpResponse;
// import org.apache.http.client.HttpClient;
// import org.apache.http.client.methods.HttpPost;
// import org.apache.http.entity.ContentType;
// import org.apache.http.entity.FileEntity;
// import org.apache.http.impl.client.HttpClients;
// import org.apache.commons.net.ftp.FTP;
// import org.apache.commons.net.ftp.FTPClient;
import java.io.File;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

@Service
public class VehiculeService {

    @Autowired
    private VehiculeRepository vehiculeRepository;
  

    @Autowired
    CodeGenerator codeGenerator;
    @Autowired
    IdGenerator idGenerator ;
    // Connexion FTP
    private static final String FTP_SERVER = "ftp.koumi.ml";
    private static final int FTP_PORT = 21; // Mise à jour si nécessaire
    private static final String FTP_USER = "default_koumi";
    private static final String FTP_PASSWORD = "H8hd#e3KejJR";
    // private static final String FTP_IMAGES_DIRECTORY = "/images";

    public Vehicule createVehicule(Vehicule vehicule, MultipartFile imageFile) throws Exception {
        Vehicule vh = vehiculeRepository.findByIdVehicule(vehicule.getIdVehicule());
        if (vh != null) {
            throw new IllegalArgumentException("Un vehicule avec l'id " + vh + " existe déjà");
        }

        // Traitement du fichier image 
        if (imageFile != null) {
            try {
                String imageLocation = "/ais";
                Path imageRootLocation = Paths.get(imageLocation);
                if (!Files.exists(imageRootLocation)) {
                    Files.createDirectories(imageRootLocation);
                }

                String imageName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Path imagePath = imageRootLocation.resolve(imageName);
                Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                
                // // Enregistrement du chemin local de l'image dans l'objet Vehicule
                // vehicule.setPhotoVehicule("ais/" + imageName);
                
                // Téléchargement de l'image vers le serveur FTP et récupération du chemin en ligne
               // Téléchargement de l'image vers le serveur FTP et récupération du chemin en ligne
                String onlineImagePath = uploadImageToFTP(imagePath, imageName);
                vehicule.setPhotoVehicule(imageName); // Enregistrement du chemin en ligne dans l'objet Vehicule

                
            } catch (IOException e) {
                throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
            }
        }

        String codes = codeGenerator.genererCode();
        String idcodes = idGenerator.genererCode();
        vehicule.setCodeVehicule(codes);
        vehicule.setIdVehicule(idcodes);
        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);
        vehicule.setDateAjout(formattedDateTime);
        
        // Enregistrement de l'objet Vehicule dans la base de données
        Vehicule savedVehicule = vehiculeRepository.save(vehicule);

        return savedVehicule; 
    }
    

    public String uploadImageToFTP(Path imagePath, String imageName) throws Exception {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(FTP_SERVER, FTP_PORT);
            ftpClient.login(FTP_USER, FTP_PASSWORD);
            ftpClient.enterLocalPassiveMode();
    
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
    
            try (InputStream inputStream = Files.newInputStream(imagePath)) {
                String remoteFilePath = "/web/koumi-server/images/" + imageName; // Chemin d'acc                                                         ès complet sur le serveur FTP
                boolean uploadResult = ftpClient.storeFile(remoteFilePath, inputStream);
                if (uploadResult) {
                    return "ftp://" + FTP_USER + "@" + FTP_SERVER + remoteFilePath; // Retourne le lien complet de l'image en ligne
                } else {
                    throw new Exception("Erreur lors du chargement de l'image sur le serveur FTP.");
                }
            }
        } catch (IOException e) {
            throw new Exception("Erreur lors de la connexion au serveur FTP : " + e.getMessage());
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
      // Méthode pour récupérer une image à partir de son nom
      public byte[] getImageByName(String imageName) throws IOException {
        // Chemin où les images sont stockées sur le serveur FTP
        String imagePath = "/web/koumi-server/images/";
    
        // Télécharger l'image à partir du serveur FTP en utilisant son nom
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            FTPClient ftpClient = new FTPClient();
            try {
                ftpClient.connect(FTP_SERVER, FTP_PORT);
                ftpClient.login(FTP_USER, FTP_PASSWORD);
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
    
                // Chemin d'accès complet de l'image sur le serveur FTP
                String remoteFilePath = imagePath + imageName;
    
                // Télécharger l'image depuis le serveur FTP
                if (ftpClient.retrieveFile(remoteFilePath, outputStream)) {
                    return outputStream.toByteArray(); // Retourner le tableau d'octets de l'image
                } else {
                    throw new IOException("Erreur lors du téléchargement de l'image depuis le serveur FTP.");
                }
            } finally {
                try {
                    if (ftpClient.isConnected()) {
                        ftpClient.logout();
                        ftpClient.disconnect();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


     // recuperer les vehicules par  typevoiture avec pagination
    public Page<Vehicule> getVehiculeByTypeVoitureWithPagination(String idTypeVoiture,Pageable pageable) {
        return vehiculeRepository.findByTypeVoiture_IdTypeVoitureAndStatutVehiculeAndActeurStatutActeur(idTypeVoiture, true,true, pageable);
    }

    // recuperer les vehicules par  categorie avec pagination
    public Page<Vehicule> getVehiculeByActeurWithPagination(String idActeur,Pageable pageable) {
        return vehiculeRepository.findByActeur_IdActeur(idActeur, pageable);
    }


      public Page<Vehicule> getAllVehiculePageable(Pageable pageable) {
        return vehiculeRepository.findAllByStatutVehiculeAndActeurStatutActeur(true,true,pageable);
    }


        @Transactional
    public Page<Vehicule> getAllVehiculePageableByPaysByCategorie(String idTypeVoiture, String niveau3PaysActeur, Pageable pageable) {
        // Fetch vehicules from the specified country
        Page<Vehicule> vehiculesByPays = vehiculeRepository.findAllByTypeVoiture_IdTypeVoitureAndStatutVehiculeTrueAndPaysAndActeurStatutActeurTrue(
            idTypeVoiture, niveau3PaysActeur.trim().toLowerCase(), pageable);

        List<Vehicule> vehiculesList = new ArrayList<>(vehiculesByPays.getContent());

        // Fetch vehicules from other countries if needed
        if (vehiculesList.size() < pageable.getPageSize()) {
            Pageable complementPageable = PageRequest.of(0, pageable.getPageSize() - vehiculesList.size());
            Page<Vehicule> vehiculeComplement = vehiculeRepository.findAllByTypeVoiture_IdTypeVoitureAndStatutVehiculeTrueAndActeurStatutActeurTrueAndPaysNot(
                idTypeVoiture, niveau3PaysActeur.trim().toLowerCase(), complementPageable);
                vehiculesList.addAll(vehiculeComplement.getContent());
        }

        return new PageImpl<>(vehiculesList, pageable, vehiculesByPays.getTotalElements() + vehiculesList.size());
    }

    

        public Page<Vehicule> getAllVehiculePageableByPays(String niveau3PaysActeur, Pageable pageable) {
        Page<Vehicule> vehiculeByPays = vehiculeRepository.findAllByStatutVehiculeTrueAndPaysAndActeurStatutActeurTrue(niveau3PaysActeur.trim().toLowerCase(), pageable);
        
        if (!vehiculeByPays.hasContent()) {
            System.out.println("Pas d'autres vehicule à fetch pour le pays " + niveau3PaysActeur);
            return vehiculeRepository.findAllByStatutVehiculeAndActeurStatutActeur(true, true, pageable);
        } else {
            System.out.println("Vehicules fetch pour le pays " + niveau3PaysActeur);
            List<Vehicule> vehiculesList = new ArrayList<>(vehiculeByPays.getContent());

            // Si le nombre de vehicule est inférieur au nombre requis, compléter avec des vehicules d'autres pays
            if (vehiculesList.size() < pageable.getPageSize()) {
                Pageable complementPageable = PageRequest.of(0, pageable.getPageSize() - vehiculesList.size());
                Page<Vehicule> vehiculeComplement = vehiculeRepository.findAllByStatutVehiculeTrueAndActeurStatutActeurTrueAndPaysNot(niveau3PaysActeur.trim().toLowerCase(), complementPageable);
                vehiculesList.addAll(vehiculeComplement.getContent());
            }

            return new PageImpl<>(vehiculesList, pageable, vehiculeByPays.getTotalElements() + vehiculesList.size());
        }
    }



     //créer un vehicule
    //     public Vehicule createVehicule(Vehicule vehicule, MultipartFile imageFile) throws Exception  {
        
    //     Vehicule vh = vehiculeRepository.findByIdVehicule(vehicule.getIdVehicule());
    //     if(vh != null){

    //         throw new IllegalArgumentException("Un vehicule avec l'id " + vh + " existe déjà");
    //     }

        
    //         // Traitement du fichier image siege acteur
    //         if (imageFile != null) {
    //             String baseUrl = "https://koumi.ml/";
    //             String imageLocation = "ais";
    //             try {
    //                 Path imageRootLocation = Paths.get(imageLocation);
    //                 if (!Files.exists(imageRootLocation)) {
    //                     Files.createDirectories(imageRootLocation);
    //                 }
            
    //                 String imageName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
    //                 Path imagePath = imageRootLocation.resolve(imageName);
    //                 Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                    
    //                 String imageUrl = baseUrl + imageLocation + "/" + imageName;
    //                 vehicule.setPhotoVehicule(imageUrl);// vehicule.setPhotoVehicule("ais/" + imageName);
    //             } catch (IOException e) {
    //                 throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
    //             }
    //         }

    //         String codes = codeGenerator.genererCode();
    //         String idcodes = idGenerator.genererCode();
    //         vehicule.setCodeVehicule(codes);
    //         vehicule.setIdVehicule(idcodes);
    //     String pattern = "yyyy-MM-dd HH:mm";
    //     DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    //     LocalDateTime now = LocalDateTime.now();
    //     String formattedDateTime = now.format(formatter);
    //     vehicule.setDateAjout(formattedDateTime);
    //        Vehicule savedVehicule = vehiculeRepository.save(vehicule);        
   
    //      return savedVehicule;
   
    // }
    // public Vehicule createVehicule(Vehicule vehicule, MultipartFile imageFile) throws Exception {
    //     Vehicule vh = vehiculeRepository.findByIdVehicule(vehicule.getIdVehicule());
    //     if(vh != null){
    //         throw new IllegalArgumentException("Un véhicule avec l'ID " + vh.getIdVehicule() + " existe déjà");
    //     }

    //     // Traitement du fichier image
    //     if (imageFile != null && !imageFile.isEmpty()) {
    //         // Chemin où les images seront stockées sur le serveur distant
    //         String imagePath = "/images";

    //         // Connexion au serveur FTP
    //         FTPClient ftpClient = new FTPClient();
    //         try {
    //             // ftpClient.connect(server, port);
    //             ftpClient.connect(server, port);
    //             ftpClient.login(user, password);
    //             ftpClient.enterLocalPassiveMode();
    //             ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                
    //             String imageName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
    //             String remoteFilePath = imagePath + "/" + imageName;

    //             // Téléversement de l'image vers le serveur FTP distant
    //             boolean uploaded = ftpClient.storeFile(remoteFilePath, imageFile.getInputStream());
    //             if (!uploaded) {
    //                 throw new Exception("Échec du téléversement de l'image sur le serveur FTP.");
    //             }

    //             // Stockage du chemin  complet dans l'objet vehicule
    //             vehicule.setPhotoVehicule(remoteFilePath);
    //         } catch (IOException ex) {
    //             throw new Exception("Erreur lors du téléversement de l'image sur le serveur FTP : " + ex.getMessage());
    //         } finally {
    //             // Déconnexion du serveur FTP
    //             if (ftpClient.isConnected()) {
    //                 ftpClient.logout();
    //                 ftpClient.disconnect();
    //             }
    //         }
    //     }

    //     // Génération de codes et de la date
    //     String codes = codeGenerator.genererCode();
    //     String idcodes = idGenerator.genererCode();
    //     vehicule.setCodeVehicule(codes);
    //     vehicule.setIdVehicule(idcodes);
    //     String pattern = "yyyy-MM-dd HH:mm";
    //     DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    //     LocalDateTime now = LocalDateTime.now();
    //     String formattedDateTime = now.format(formatter);
    //     vehicule.setDateAjout(formattedDateTime);

    //     // Sauvegarde du véhicule dans la base de données
    //     Vehicule savedVehicule = vehiculeRepository.save(vehicule);

    //     return savedVehicule;
    // }


       //Liste des vehicules par acteur
    public List<Vehicule> getAllVehiculeByActeur(String id){
        List<Vehicule>  vehiculeList = vehiculeRepository.findAllByActeurIdActeur(id);

        if(vehiculeList.isEmpty()){
            throw new EntityNotFoundException("Aucun vehicule trouvé");
        }
        vehiculeList = vehiculeList
                .stream().sorted((d1, d2) -> d2.getNomVehicule().compareTo(d1.getNomVehicule()))
                .collect(Collectors.toList());
        return vehiculeList;
    } 
   
    public List<Vehicule> getVehiculesByTypeVoiture(String idTypeVoiture){
        List<Vehicule>  vehiculeList =vehiculeRepository.findAllByTypeVoitureIdTypeVoiture(idTypeVoiture);

        if(vehiculeList.isEmpty()){
            throw new EntityNotFoundException("Aucun vehicule trouvé");
        }
        vehiculeList = vehiculeList
                .stream().sorted((d1, d2) -> d2.getNomVehicule().compareTo(d1.getNomVehicule()))
                .collect(Collectors.toList());
        return vehiculeList;
    }


      //Modifier vehicule
      public Vehicule updateVehicule(Vehicule vehicule, MultipartFile imageFile , String id) throws Exception {
        
        Vehicule vh = vehiculeRepository.findByIdVehicule(id);
        if(vh == null){

            throw new IllegalArgumentException("Le vehicule avec l'id " + vh + " n'existe pas");
        }
        
    

            // Traitement du fichier image siege acteur
            if (imageFile != null) {
                try {
                    String imageLocation = "/ais";
                    Path imageRootLocation = Paths.get(imageLocation);
                    if (!Files.exists(imageRootLocation)) {
                        Files.createDirectories(imageRootLocation);
                    }
    
                    String imageName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                    Path imagePath = imageRootLocation.resolve(imageName);
                    Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                    
                    // // Enregistrement du chemin local de l'image dans l'objet Vehicule
                    // vehicule.setPhotoVehicule("ais/" + imageName);
                    
                    // Téléchargement de l'image vers le serveur FTP et récupération du chemin en ligne
                   // Téléchargement de l'image vers le serveur FTP et récupération du chemin en ligne
                    String onlineImagePath = uploadImageToFTP(imagePath, imageName);
                    vh.setPhotoVehicule(imageName); // Enregistrement du chemin en ligne dans l'objet Vehicule
    
                    
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }
            
            vh.setNomVehicule(vehicule.getNomVehicule());
            vh.setCapaciteVehicule(vehicule.getCapaciteVehicule());
            vh.setEtatVehicule(vehicule.getEtatVehicule());
            vh.setPrixParDestination(vehicule.getPrixParDestination());
            vh.setLocalisation(vehicule.getLocalisation());
            vh.setDescription(vehicule.getDescription());
            vh.setNbKilometrage(vehicule.getNbKilometrage());
            
            if(vehicule.getMonnaie() != null){
                vh.setMonnaie(vehicule.getMonnaie());
            }
        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);
        vh.setDateAjout(formattedDateTime);
            Vehicule savedVehicule = vehiculeRepository.save(vh);        
   
           return savedVehicule;
   
    }
  
      //Liste des vehicules
       public List<Vehicule> getAllVehicules(){
        List<Vehicule> vehiculeList = vehiculeRepository.findAll();

        vehiculeList = vehiculeList
        .stream().sorted((v1,v2) -> v2.getNomVehicule().compareTo(v1.getNomVehicule()))
        .collect(Collectors.toList());

        return vehiculeList;
    }

    public String deleteVehicule(String id){
        Vehicule vehicule = vehiculeRepository.findById(id).orElseThrow(null);

        vehiculeRepository.delete(vehicule);
        return "Vehicule supprimé avec success";
    }

    public Vehicule active(String id) throws Exception{
        Vehicule vehicule = vehiculeRepository.findById(id).orElseThrow(null);

        try {
            vehicule.setStatutVehicule(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation du vehicule: " + e.getMessage());
        }
        return vehiculeRepository.save(vehicule);
    }

    public Vehicule desactive(String id) throws Exception{
        Vehicule vehicule = vehiculeRepository.findById(id).orElseThrow(null);

        try {
            vehicule.setStatutVehicule(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la desactivation du vehicule : " + e.getMessage());
        }
        return vehiculeRepository.save(vehicule);
    }

    
    
}

