package projet.ais.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Unite;
import projet.ais.repository.UniteRepository;

@Service
public class UniteService {
    
    @Autowired
    UniteRepository uniteRepository;
    @Autowired
    CodeGenerator codeGenerator;
  @Autowired
    IdGenerator idGenerator ;


    public Unite createUnite(Unite unite){
        Unite unites = uniteRepository.findByNomUnite(unite.getNomUnite());

        if(unites != null)
            throw new IllegalStateException("cet Unité existe déjà");
        
        String codes = codeGenerator.genererCode();
        String Idcodes = idGenerator.genererCode();
        unite.setCodeUnite(codes);
        unite.setIdUnite(Idcodes);

        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);  
        unite.setDateAjout(formattedDateTime);
        return uniteRepository.save(unite);
    }

    public Unite updateUnite(Unite unite, String id){
        Unite unites = uniteRepository.findById(id).orElseThrow(null);

        unites.setNomUnite(unite.getNomUnite());
        unites.setPersonneModif(unite.getPersonneModif());
        unites.setSigleUnite(unite.getSigleUnite());
        unites.setDescription(unite.getDescription());

         String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);  
        unites.setDateModif(formattedDateTime);
        return uniteRepository.save(unites);
    }

    public List<Unite> getAllUnites(){
        List<Unite> uniteList = uniteRepository.findAll();

        if(uniteList.isEmpty())
             throw new EntityNotFoundException("Liste sous region vide");

        uniteList = uniteList
        .stream().sorted((u1,u2) -> u2.getNomUnite().compareTo(u1.getNomUnite()))
        .collect(Collectors.toList());

        return uniteList;
    }

    public List<Unite> getAllUnitesByActeur(String id){
        List<Unite> uniteList = uniteRepository.findByActeurIdActeur(id);

        if(uniteList.isEmpty())
             throw new EntityNotFoundException("Liste sous region vide");
             
        uniteList = uniteList
        .stream().sorted((u1,u2) -> u2.getNomUnite().compareTo(u1.getNomUnite()))
        .collect(Collectors.toList());

        return uniteList;
    }

    public String deleteUnite(String id){
        Unite unite = uniteRepository.findById(id).orElseThrow(null);

        uniteRepository.delete(unite);
        return "Supprimé avec success";
    }

    public Unite active(String id) throws Exception{
        Unite unite = uniteRepository.findById(id).orElseThrow(null);

        try {
            unite.setStatutUnite(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation : " + e.getMessage());
        }
        return uniteRepository.save(unite);
    }

    public Unite desactive(String id) throws Exception{
        Unite unite = uniteRepository.findById(id).orElseThrow(null);

        try {
            unite.setStatutUnite(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la desactivation : " + e.getMessage());
        }
        return uniteRepository.save(unite);
    }
}
