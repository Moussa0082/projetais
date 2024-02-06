package projet.ais.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Acteur;
import projet.ais.models.Intrant;
import projet.ais.models.Vehicule;
import projet.ais.repository.ActeurRepository;
import projet.ais.repository.IntrantRepository;

@Service
public class IntrantService {

    @Autowired
    private IntrantRepository intrantRepository;
    @Autowired
    ActeurRepository acteurRepository;

    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    CodeGenerator codeGenerator;


     //créer un intrant
      public Intrant createIntrant(Intrant intrant, MultipartFile imageFile) throws Exception {
        
        Intrant it = intrantRepository.findByIdIntrant(intrant.getIdIntrant());
        if(it != null){
            throw new IllegalArgumentException("Un intrant avec l'id " + it + " existe déjà");
        }

        Acteur acteur = acteurRepository.findByIdActeur(intrant.getActeur().getIdActeur());

        if(acteur == null)
            throw new EntityNotFoundException("Aucun acteur trouvé");
            
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
                    intrant.setPhotoIntrant(imageLocation);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }

            intrant.setIdIntrant(idGenerator.genererCode());
            intrant.setCondeIntrant(codeGenerator.genererCode());
           Intrant savedIntrant = intrantRepository.save(intrant);        
   
         return savedIntrant;
   
    }


       //Liste des intrants par acteur
    public List<Intrant> getAllIntrantByActeur(String id){
        List<Intrant>  intrantList = intrantRepository.findAllByActeurIdActeur(id);

        if(intrantList.isEmpty()){
            throw new EntityNotFoundException("Aucun intrant trouvé");
        }
        intrantList = intrantList
                .stream().sorted((d1, d2) -> d2.getNomIntrant().compareTo(d1.getNomIntrant()))
                .collect(Collectors.toList());
        return intrantList;
    } 

    public List<Intrant> getAllIntrantBySuperficie(String id){
        List<Intrant>  intrantList = intrantRepository.findBySuperficieIdSuperficie(id);

        if(intrantList.isEmpty()){
            throw new EntityNotFoundException("Aucun intrant trouvé");
        }
        intrantList = intrantList
                .stream().sorted((d1, d2) -> d2.getNomIntrant().compareTo(d1.getNomIntrant()))
                .collect(Collectors.toList());
        return intrantList;
    } 

      //Modifier intrant
      public Intrant updateIntrant(Intrant intrant, MultipartFile imageFile , String id) throws Exception {
        
        Intrant it = intrantRepository.findByIdIntrant(id);
        if(it == null){

            throw new IllegalArgumentException("L'intrant avec l'id " + it + " n'existe pas");
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
                    it.setPhotoIntrant("ais/" + imageName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }
            it.setNomIntrant(intrant.getNomIntrant());
            it.setQuantiteIntrant(intrant.getQuantiteIntrant());
            it.setDescriptionIntrant(intrant.getDescriptionIntrant());
            it.setDateModif(LocalDateTime.now());
            Intrant savedIntrant = intrantRepository.save(it);        
   
           return savedIntrant;
   
    }
  
      //Liste des intrant
       public List<Intrant> getAllIntrant(){
        List<Intrant> intrantList = intrantRepository.findAll();

        intrantList = intrantList
        .stream().sorted((v1,v2) -> v2.getNomIntrant().compareTo(v1.getNomIntrant()))
        .collect(Collectors.toList());

        return intrantList;
    }

    public String deleteIntrant(String id){
        Intrant intrant = intrantRepository.findById(id).orElseThrow(null);

        intrantRepository.delete(intrant);
        return "Intrant supprimé avec success";
    }

    public Intrant active(String id) throws Exception{
        Intrant intrant = intrantRepository.findById(id).orElseThrow(null);

        try {
            intrant.setStatutIntrant(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation de l'intrant: " + e.getMessage());
        }
        return intrantRepository.save(intrant);
    }

    public Intrant desactive(String id) throws Exception{
        Intrant intrant = intrantRepository.findById(id).orElseThrow(null);

        try {
            intrant.setStatutIntrant(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la desactivation de l'intrant : " + e.getMessage());
        }
        return intrantRepository.save(intrant);
    }
    
}
