package projet.ais.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projet.ais.IdGenerator;
import projet.ais.models.ParametreFiche;
import projet.ais.models.RegroupementParametre;
import projet.ais.repository.ParametreFicheRepository;
import projet.ais.repository.RegroupementParametreRepository;

@Service
public class RegroupementSevice {
    
    @Autowired
    RegroupementParametreRepository regroupementParametreRepository;

    @Autowired
    ParametreFicheRepository parametreFicheRepository;
  @Autowired
    IdGenerator idGenerator ;

    public RegroupementParametre createParametreRegroupement(RegroupementParametre regroupementParametre){
        ParametreFiche parametreFicheDonnees = parametreFicheRepository.findByIdParametreFiche(regroupementParametre.getParametreFiche().getIdParametreFiche());
        
        if(parametreFicheDonnees == null)
            throw new IllegalArgumentException("Aucune parametre trouvé pour le regroupement");
        
        String code = idGenerator.genererCode();
        regroupementParametre.setIdRegroupement(code);
        Date dates = new Date();
        Instant instant = dates.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        regroupementParametre.setDateModif(LocalDateTime.now());
        regroupementParametre.setDateAjout(LocalDateTime.now());
        return regroupementParametreRepository.save(regroupementParametre);
    }

    public RegroupementParametre updateParametreRegroupement(RegroupementParametre regroupementParametre, String id){
        RegroupementParametre regroupementParametres = regroupementParametreRepository.findById(id).orElseThrow(null);
        regroupementParametres.setParametreRegroupe(regroupementParametre.getParametreRegroupe());
        regroupementParametres.setDateAjout(regroupementParametres.getDateAjout());
        
        Date dates = new Date();
        Instant instant = dates.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        regroupementParametres.setDateModif(LocalDateTime.now());
        return regroupementParametreRepository.save(regroupementParametres);
    }

    public List<RegroupementParametre> getAllRegroupementParametres() {
    List<RegroupementParametre> regroupementParam = regroupementParametreRepository.findAll();
    regroupementParam = regroupementParam
    .stream().sorted((r1,r2) -> r2.getParametreRegroupe().compareTo(r1.getParametreRegroupe()))
        .collect(Collectors.toList());

    return regroupementParam;
    }

    public String deleteRegroupement(String id){
        RegroupementParametre regroupementParam = regroupementParametreRepository.findById(id).orElseThrow(null);

        regroupementParametreRepository.delete(regroupementParam);
        return "Supprimé avec success";
    }

    public RegroupementParametre active(String id) throws Exception{
        RegroupementParametre regroupementParam = regroupementParametreRepository.findById(id).orElseThrow(null);

        try {
            regroupementParam.setStatutRegroupement(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation : " + e.getMessage());
        }
        return regroupementParametreRepository.save(regroupementParam);
    }

    public RegroupementParametre desactive(String id) throws Exception{
        RegroupementParametre regroupementParam = regroupementParametreRepository.findById(id).orElseThrow(null);

        try {
            regroupementParam.setStatutRegroupement(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la desactivation : " + e.getMessage());
        }
        return regroupementParametreRepository.save(regroupementParam);
    }
}
