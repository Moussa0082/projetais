package projet.ais.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Acteur;
import projet.ais.models.CategorieProduit;
import projet.ais.models.RenvoieParametre;
import projet.ais.models.Speculation;
import projet.ais.repository.ActeurRepository;
import projet.ais.repository.CategorieProduitRepository;
import projet.ais.repository.SpeculationRepository;
import com.sun.jdi.request.DuplicateRequestException;
import jakarta.persistence.EntityNotFoundException;

import java.time.Instant;
import java.time.LocalDateTime;
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
    @Autowired
    IdGenerator idGenerator ;
       @Autowired
    ActeurRepository acteurRepository;

    public Speculation createSpeculation(Speculation speculation){

        CategorieProduit categorieProduit = categorieProduitRepository.findByIdCategorieProduit(speculation.getCategorieProduit().getIdCategorieProduit());
        Speculation speculations = speculationRepository.findBynomSpeculation(speculation.getNomSpeculation());

         Acteur acteur = acteurRepository.findByIdActeur(speculation.getActeur().getIdActeur());

        if(acteur == null)
            throw new IllegalStateException("Aucun acteur disponible");
        
        if(categorieProduit == null)
            throw new EntityNotFoundException("Cette categorie n'existe pas");

        if(speculations != null)
            throw new DuplicateRequestException("Cette speculation existe déjà");
        
            String codes = codeGenerator.genererCode();
            String idcodes = idGenerator.genererCode();
            
            speculation.setCodeSpeculation(codes);
            speculation.setIdSpeculation(idcodes);

        speculation.setDateModif(LocalDateTime.now());
        speculation.setDateAjout(LocalDateTime.now());
        return speculationRepository.save(speculation);
    }


    public Speculation updateSpeculation(Speculation speculation, String id){

       Speculation speculations = speculationRepository.findById(id).orElseThrow(null);
       speculations.setDescriptionSpeculation(speculation.getDescriptionSpeculation());
       speculations.setNomSpeculation(speculation.getNomSpeculation());
        speculations.setPersonneModif(speculation.getPersonneModif());
       speculations.setDateModif(LocalDateTime.now());
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

    public List<Speculation> getAllSpeculationByActeur(String id){
        List<Speculation> speculations = speculationRepository.findByActeurIdActeur(id);

        if(speculations.isEmpty())
            throw new EntityNotFoundException("Speculation non trouvé");
        
        speculations = speculations
        .stream().sorted((s1, s2) -> s2.getNomSpeculation().compareTo(s1.getNomSpeculation()))
        .collect(Collectors.toList());

        return speculations;
    }

    public List<Speculation> getAllSpeculationByCategorie(String id){
        List<Speculation> speculations = speculationRepository.findByCategorieProduitIdCategorieProduit(id);

        if(speculations.isEmpty())
            throw new EntityNotFoundException("Speculation non trouvé");
        
        speculations = speculations
        .stream().sorted((s1, s2) -> s2.getNomSpeculation().compareTo(s1.getNomSpeculation()))
        .collect(Collectors.toList());

        return speculations;
    }

    public String DeleteSpeculations(String id){
        Speculation speculation = speculationRepository.findById(id).orElseThrow(null);

        speculationRepository.delete(speculation);
        return "Supprimer avec succèss";
    }

    public Speculation active(String id) throws Exception{
        Speculation speculation = speculationRepository.findById(id).orElseThrow(null);

        try {
            speculation.setStatutSpeculation(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation : " + e.getMessage());
        }
        return speculationRepository.save(speculation);
    }

    public Speculation desactive(String id) throws Exception{
        Speculation speculation = speculationRepository.findById(id).orElseThrow(null);

        try {
            speculation.setStatutSpeculation(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la desactivation : " + e.getMessage());
        }
        return speculationRepository.save(speculation);
    }
}
