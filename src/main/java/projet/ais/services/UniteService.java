package projet.ais.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Stock;
import projet.ais.models.Unite;
import projet.ais.repository.UniteRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

        unite.setDateModif(LocalDateTime.now());
        unite.setDateAjout(LocalDateTime.now());
        return uniteRepository.save(unite);
    }

    public Unite updateUnite(Unite unite, String id){
        Unite unites = uniteRepository.findById(id).orElseThrow(null);

        unites.setNomUnite(unite.getNomUnite());
        unites.setDateAjout(unites.getDateAjout());
        unites.setPersonneModif(unite.getPersonneModif());
        unite.setDateModif(LocalDateTime.now());
        return uniteRepository.save(unites);
    }

    public List<Unite> getAllUnites(){
        List<Unite> uniteList = uniteRepository.findAll();

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
