package projet.ais.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projet.ais.CodeGenerator;
import projet.ais.models.Unite;
import projet.ais.repository.UniteRepository;

import java.time.Instant;
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

    public Unite createUnite(Unite unite){
        Unite unites = uniteRepository.findByNomUnite(unite.getNomUnite());

        if(unites != null)
            throw new IllegalStateException("cet Unité existe déjà");
        
        String codes = codeGenerator.genererCode();
        unite.setCodeUnite(codes);
        Date dates = new Date();
        Instant instant = dates.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        unite.setDateModif(dates);
        unite.setDateAjout(dates);
        return uniteRepository.save(unite);
    }

    public Unite updateUnite(Unite unite, Integer id){
        Unite unites = uniteRepository.findById(id).orElseThrow(null);

        unites.setNomUnite(unite.getNomUnite());
        unites.setDateAjout(unites.getDateAjout());
        Date dates = new Date();
        Instant instant = dates.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        unite.setDateModif(dates);
        return uniteRepository.save(unites);
    }

    public List<Unite> getAllUnites(){
        List<Unite> uniteList = uniteRepository.findAll();

        uniteList = uniteList
        .stream().sorted((u1,u2) -> u2.getNomUnite().compareTo(u1.getNomUnite()))
        .collect(Collectors.toList());

        return uniteList;
    }

    public String deleteUnite(Integer id){
        Unite unite = uniteRepository.findById(id).orElseThrow(null);

        uniteRepository.delete(unite);
        return "Supprimé avec success";
    }
}
