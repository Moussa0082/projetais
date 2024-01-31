package projet.ais.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import projet.ais.models.Unite;
import projet.ais.models.Vehicule;
import projet.ais.repository.VehiculeRepository;

@Service
public class VehiculeService {

    @Autowired
    private VehiculeRepository vehiculeRepository;

     //créer un vehicule
      public Vehicule createVehicule(Vehicule vehicule, MultipartFile imageFile) throws Exception {
        
        Vehicule vh = vehiculeRepository.findByIdVehicule(vehicule.getIdVehicule());
        if(vh != null){

            throw new IllegalArgumentException("Un vehicule avec l'id " + vh + " existe déjà");
        }
        
    // Vérifier si le meme vehicule existe avec le même acteur
    // Vehicule existantVehicule = vehiculeRepository.findByNomVehiculeAndCapaciteVehiculeAndActeur(vehicule.getNomVehicule(), vehicule.getCapaciteVehicule(), vehicule.getActeur());
    // if (existantVehicule != null) {
    //     // si le meme vehicule existe avec le même acteur
    //     throw new IllegalArgumentException("Ce vehicule existe déjà avec le même acteur");
    // }

            // Traitement du fichier image siege acteur
            if (imageFile != null) {
                String imageLocation = "C:\\xampp\\htdocs\\ais";
                try {
                    Path imageRootLocation = Paths.get(imageLocation);
                    if (!Files.exists(imageRootLocation)) {
                        Files.createDirectories(imageRootLocation);
                    }
    
                    String imageName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                    Path imagePath = imageRootLocation.resolve(imageName);
                    Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                    vehicule.setPhotoVehicule("ais/" + imageName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }
            vehicule.setDateAjout(LocalDateTime.now());
           Vehicule savedVehicule = vehiculeRepository.save(vehicule);        
   
    return savedVehicule;
   
    }





      //Modifier vehicule
      public Vehicule updateVehicule(Vehicule vehicule, MultipartFile imageFile , String id) throws Exception {
        
        Vehicule vh = vehiculeRepository.findByIdVehicule(id);
        if(vh == null){

            throw new IllegalArgumentException("Le vehicule avec l'id " + vh + " n'existe pas");
        }
        
    // Vérifier si le meme vehicule existe avec le même acteur
    // Vehicule existantVehicule = vehiculeRepository.findByNomVehiculeAndCapaciteVehiculeAndActeur(vehicule.getNomVehicule(), vehicule.getCapaciteVehicule(), vehicule.getActeur());
    // if (existantVehicule != null) {
    //     // si le meme vehicule existe avec le même acteur
    //     throw new IllegalArgumentException("Ce vehicule existe déjà avec le même acteur");
    // }

            // Traitement du fichier image siege acteur
            if (imageFile != null) {
                String imageLocation = "C:\\xampp\\htdocs\\ais";
                try {
                    Path imageRootLocation = Paths.get(imageLocation);
                    if (!Files.exists(imageRootLocation)) {
                        Files.createDirectories(imageRootLocation);
                    }
    
                    String imageName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                    Path imagePath = imageRootLocation.resolve(imageName);
                    Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                    vh.setPhotoVehicule("ais/" + imageName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }
            vh.setNomVehicule(vehicule.getNomVehicule());
            vh.setCapaciteVehicule(vehicule.getCapaciteVehicule());
            vehicule.setDateModif(LocalDateTime.now());
            vh.setActeur(vehicule.getActeur());
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

