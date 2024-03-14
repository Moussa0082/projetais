package projet.ais.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Acteur;
import projet.ais.models.Unite;
import projet.ais.models.Vehicule;
import projet.ais.repository.VehiculeRepository;

@Service
public class VehiculeService {

    @Autowired
    private VehiculeRepository vehiculeRepository;
  

     @Autowired
    CodeGenerator codeGenerator;
  @Autowired
    IdGenerator idGenerator ;


     //créer un vehicule
      public Vehicule createVehicule(Vehicule vehicule, MultipartFile imageFile) throws Exception {
        
        Vehicule vh = vehiculeRepository.findByIdVehicule(vehicule.getIdVehicule());
        if(vh != null){

            throw new IllegalArgumentException("Un vehicule avec l'id " + vh + " existe déjà");
        }

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

            String codes = codeGenerator.genererCode();
            String idcodes = idGenerator.genererCode();
            vehicule.setCodeVehicule(codes);
            vehicule.setIdVehicule(idcodes);
        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);
        vehicule.setDateAjout(formattedDateTime);
           Vehicule savedVehicule = vehiculeRepository.save(vehicule);        
   
         return savedVehicule;
   
    }


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
            vh.setEtatVehicule(vehicule.getEtatVehicule());
            vh.setPrixParDestination(vehicule.getPrixParDestination());
            vh.setLocalisation(vehicule.getLocalisation());
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

