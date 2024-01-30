package projet.ais.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projet.ais.CodeGenerator;
import projet.ais.models.CategorieProduit;
import projet.ais.models.RenvoieParametre;
import projet.ais.models.Speculation;
import projet.ais.repository.CategorieProduitRepository;
import projet.ais.repository.SpeculationRepository;
import com.sun.jdi.request.DuplicateRequestException;
import jakarta.persistence.EntityNotFoundException;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SpeculationService {
    
    @Autowired
    SpeculationRepository speculationRepository;
    @Autowired
    CategorieProduitRepository categorieProduitRepository;
    @Autowired
    CodeGenerator codeGenerator;
   

    public Speculation createSpeculation(Speculation speculation){

        CategorieProduit categorieProduit = categorieProduitRepository.findByIdCategorieProduit(speculation.getCategorieProduit().getIdCategorieProduit());
        Speculation speculations = speculationRepository.findBynomSpeculation(speculation.getNomSpeculation());

        if(categorieProduit == null)
            throw new EntityNotFoundException("Cette categorie n'existe pas");

        if(speculations != null)
            throw new DuplicateRequestException("Cette speculation existe déjà");
        
            String codes = codeGenerator.genererCode();
            speculation.setCodeSpeculation(codes);
            Date dates = new Date();
        Instant instant = dates.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        speculation.setDateModif(dates);
        speculation.setDateAjout(dates);
        return speculationRepository.save(speculation);
    }


    public Speculation updateSpeculation(Speculation speculation, Integer id){

       Speculation speculations = speculationRepository.findById(id).orElseThrow(null);
       speculations.setDescriptionSpeculation(speculation.getDescriptionSpeculation());
       speculations.setNomSpeculation(speculation.getNomSpeculation());
        speculations.setDateAjout(speculations.getDateAjout());
       Date dates = new Date();
       Instant instant = dates.toInstant();
       ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
       speculations.setDateModif(dates);
        return speculationRepository.save(speculations);
    }

    public List<Speculation> getAllSpeculation(){
        List<Speculation> speculations = speculationRepository.findAll();

        if(speculations.isEmpty())
            throw new EntityNotFoundException("Speculation non trouvé");
        
        speculations = speculations
        .stream().sorted((s1, s2) -> s2.getNomSpeculation().compareTo(s1.getNomSpeculation()))
        .collect(Collectors.toList());

        return speculations;
    }

    public List<Speculation> getAllSpeculationByCategorie(Integer id){
        List<Speculation> speculations = speculationRepository.findByCategorieProduitIdCategorieProduit(id);

        if(speculations.isEmpty())
            throw new EntityNotFoundException("Speculation non trouvé");
        
        speculations = speculations
        .stream().sorted((s1, s2) -> s2.getNomSpeculation().compareTo(s1.getNomSpeculation()))
        .collect(Collectors.toList());

        return speculations;
    }

    public String DeleteSpeculations(Integer id){
        Speculation speculation = speculationRepository.findById(id).orElseThrow(null);

        speculationRepository.delete(speculation);
        return "Supprimer avec succèss";
    }

    public Speculation active(Integer id) throws Exception{
        Speculation speculation = speculationRepository.findById(id).orElseThrow(null);

        try {
            speculation.setStatutSpeculation(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation : " + e.getMessage());
        }
        return speculationRepository.save(speculation);
    }

    public Speculation desactive(Integer id) throws Exception{
        Speculation speculation = speculationRepository.findById(id).orElseThrow(null);

        try {
            speculation.setStatutSpeculation(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la desactivation : " + e.getMessage());
        }
        return speculationRepository.save(speculation);
    }
}
