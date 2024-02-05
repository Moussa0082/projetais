package projet.ais.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Campagne;
import projet.ais.models.Speculation;
import projet.ais.repository.CampagneRepository;

@Service
public class CampagneService {
    
    @Autowired
    CampagneRepository campagneRepository;
    @Autowired
    CodeGenerator codeGenerator;
    @Autowired
    IdGenerator idGenerator ;

    public Campagne createCampagne(Campagne campagne) {
        Campagne campagne2 = campagneRepository.findByNomCampagne(campagne.getNomCampagne());

        if(campagne2 != null)
            throw new EntityNotFoundException("Ce campagne est déjà enregister");
        
        String codes= codeGenerator.genererCode();
        String idCode = idGenerator.genererCode();

        campagne.setDateModif(LocalDateTime.now());
        campagne.setDateAjout(LocalDateTime.now());
        campagne.setIdCampagne(idCode);
        campagne.setCodeCampagne(codes);
        return campagneRepository.save(campagne);
    }

    public Campagne updateCampagne(Campagne campagne, String id){
        Campagne camp = campagneRepository.findById(id).orElseThrow(null);

        camp.setNomCampagne(campagne.getNomCampagne());
        camp.setDescription(campagne.getDescription());
        camp.setDateModif(LocalDateTime.now());

        return campagneRepository.save(camp);
    }

    public List<Campagne> getCampagnes(){
        List<Campagne> campagnes = campagneRepository.findAll();

        campagnes = campagnes
        .stream().sorted((s1, s2) -> s2.getNomCampagne().compareTo(s1.getNomCampagne()))
        .collect(Collectors.toList());

        return campagnes;
    }

    public String deleteCampagne(String id){
        Campagne campagne = campagneRepository.findById(id).orElseThrow(null);

        campagneRepository.delete(campagne);

        return "Supprimé avec succèss";
    }

    public Campagne active(String id) throws Exception{
        Campagne campagne = campagneRepository.findById(id).orElseThrow(null);

        try {
            campagne.setStatutCampagne(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation : " + e.getMessage());
        }
        return campagneRepository.save(campagne);
    }

    public Campagne desactive(String id) throws Exception{
        Campagne campagne = campagneRepository.findById(id).orElseThrow(null);

        try {
            campagne.setStatutCampagne(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation : " + e.getMessage());
        }
        return campagneRepository.save(campagne);
    }
}
