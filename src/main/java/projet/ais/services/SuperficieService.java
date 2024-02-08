package projet.ais.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Acteur;
import projet.ais.models.Campagne;
import projet.ais.models.Intrant;
import projet.ais.models.Speculation;
import projet.ais.models.Superficie;
import projet.ais.repository.ActeurRepository;
import projet.ais.repository.CampagneRepository;
import projet.ais.repository.IntrantRepository;
import projet.ais.repository.SuperficieRepository;

@Service
public class SuperficieService {
    
    @Autowired
    CodeGenerator codeGenerator;
    @Autowired
    IdGenerator idGenerator ;
    @Autowired
    SuperficieRepository superficieRepository;
    @Autowired
    ActeurRepository acteurRepository;
    @Autowired
    CampagneRepository campagneRepository ;
    @Autowired
    IntrantRepository intrantRepository;


    public Superficie createSuperficie(Superficie superficie){
        Acteur acteur = acteurRepository.findByIdActeur(superficie.getActeur().getIdActeur());

        Campagne campagne = campagneRepository.findByIdCampagne(superficie.getCampagne().getIdCampagne());

        List<Intrant> intrants = superficie.getIntrants();

        List<String> idIntrants = new ArrayList<>();

        for(Intrant intrant : intrants){
            idIntrants.add(intrant.getIdIntrant());
        }

        List<Intrant> intrantList = intrantRepository.findByIdIntrantIn(idIntrants);

        if(intrantList.isEmpty())
            throw new EntityNotFoundException("Aucune intrant trouvé");

        if(campagne == null)
            throw new EntityNotFoundException("Aucune campagne trouvé");

        if(acteur == null)
            throw new EntityNotFoundException("Aucun acteur trouvé");

        String codes = codeGenerator.genererCode();
        String idCode = idGenerator.genererCode();

        superficie.setCodeSuperficie(codes);
        superficie.setIdSuperficie(idCode);
        superficie.setDateAjout(LocalDateTime.now());
        superficie.setDateModif(LocalDateTime.now());

        return superficieRepository.save(superficie);
    }

    public Superficie updateSuperficie(Superficie superficie, String id){
        Superficie sup = superficieRepository.findById(id).orElseThrow(null);
        sup.setLocalite(superficie.getLocalite());
        sup.setDateSemi(superficie.getDateSemi());
        sup.setSuperficieHa(superficie.getSuperficieHa());

        sup.setDateModif(LocalDateTime.now());
        return superficieRepository.save(sup);
    }

    public List<Superficie> getAllSuperficie(){
        List<Superficie> superficieList = superficieRepository.findAll();

        superficieList = superficieList
        .stream().sorted((s1, s2) -> s2.getIdSuperficie().compareTo(s1.getIdSuperficie()))
        .collect(Collectors.toList());

        return superficieList;
    }

    public List<Superficie> getAllSuperficieByActeur(String id){
        List<Superficie> superficieList = superficieRepository.findByActeurIdActeur(id);

        superficieList = superficieList
        .stream().sorted((s1, s2) -> s2.getIdSuperficie().compareTo(s1.getIdSuperficie()))
        .collect(Collectors.toList());

        return superficieList;
    }

    public List<Superficie> getAllSuperficieBySpeculation(String id){
        List<Superficie> superficieList = superficieRepository.findBySpeculationIdSpeculation(id);

        superficieList = superficieList
        .stream().sorted((s1, s2) -> s2.getIdSuperficie().compareTo(s1.getIdSuperficie()))
        .collect(Collectors.toList());

        return superficieList;
    }

    public String DeleteSuperficie(String id) {
        Superficie sup = superficieRepository.findById(id).orElseThrow(null);

        superficieRepository.delete(sup);

        return "Supprimé avec succèss";
    }


    public Superficie active(String id) throws Exception{
        Superficie sup = superficieRepository.findById(id).orElseThrow(null);

        try {
            sup.setStatutSuperficie(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation : " + e.getMessage());
        }
        return superficieRepository.save(sup);
    }

    public Superficie desactive(String id) throws Exception{
        Superficie sup = superficieRepository.findById(id).orElseThrow(null);

        try {
            sup.setStatutSuperficie(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la desactivation : " + e.getMessage());
        }
        return superficieRepository.save(sup);
    }
}
