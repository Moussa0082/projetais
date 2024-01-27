package projet.ais.services;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projet.ais.models.ParametreFicheDonnees;
import projet.ais.models.RegroupementParametre;
import projet.ais.repository.ParametreFicheDonneesRepository;
import projet.ais.repository.RegroupementParametreRepository;

@Service
public class RegroupementSevice {
    
    @Autowired
    RegroupementParametreRepository regroupementParametreRepository;

    @Autowired
    ParametreFicheDonneesRepository parametreFicheDonneesRepository ;

    public RegroupementParametre createParametreRegroupement(RegroupementParametre regroupementParametre){
        ParametreFicheDonnees parametreFicheDonnees = parametreFicheDonneesRepository.findByIdParametre(regroupementParametre.getParametreFicheDonnees().getIdParametre());
        if(parametreFicheDonnees == null)
            throw new IllegalArgumentException("Aune parametre trouvé pour le regroupement");
        
    Date dates = new Date();
        Instant instant = dates.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        regroupementParametre.setDateModif(dates);
        regroupementParametre.setDateAjout(dates);
        return regroupementParametreRepository.save(regroupementParametre);
    }

    public RegroupementParametre updateParametreRegroupement(RegroupementParametre regroupementParametre, Integer id){
        RegroupementParametre regroupementParametres = regroupementParametreRepository.findById(id).orElseThrow(null);
        regroupementParametres.setParametreRegroupe(regroupementParametre.getParametreRegroupe());
        regroupementParametres.setDateAjout(regroupementParametres.getDateAjout());
        Date dates = new Date();
        Instant instant = dates.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        regroupementParametres.setDateModif(dates);
        return regroupementParametreRepository.save(regroupementParametres);
    }

    public List<RegroupementParametre> getAllRegroupementParametres() {
    List<RegroupementParametre> regroupementParam = regroupementParametreRepository.findAll();
    regroupementParam = regroupementParam
    .stream().sorted((r1,r2) -> r2.getParametreRegroupe().compareTo(r1.getParametreRegroupe()))
        .collect(Collectors.toList());

    return regroupementParam;
    }

    public String deleteRegroupement(Integer id){
        RegroupementParametre regroupementParam = regroupementParametreRepository.findById(id).orElseThrow(null);

        regroupementParametreRepository.delete(regroupementParam);
        return "Supprimé avec success";
    }

}
