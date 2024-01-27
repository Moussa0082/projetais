package projet.ais.services;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.CodeGenerator;
import projet.ais.models.ParametreFicheDonnees;
import projet.ais.repository.ParametreFicheDonneesRepository;

@Service
public class ParametreFicheDonneeService {
    
    @Autowired
    ParametreFicheDonneesRepository parametreFicheDonneesRepository;
    @Autowired
    CodeGenerator codeGenerator;
    
    public ParametreFicheDonnees createParametreFiche(ParametreFicheDonnees parametreFicheDonnees){
        String codes = codeGenerator.genererCode();
        parametreFicheDonnees.setCodeParametre(codes);
        Date dates = new Date();
            Instant instant = dates.toInstant();
            ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
            parametreFicheDonnees.setDateAjout(dates);
            parametreFicheDonnees.setDateModif(dates);
        return parametreFicheDonneesRepository.save(parametreFicheDonnees);
    }

    public ParametreFicheDonnees updateParametreFiche(ParametreFicheDonnees parametreFicheDonnees, Integer id){
        
        ParametreFicheDonnees param = parametreFicheDonneesRepository.findById(id).orElseThrow(null);
        param.setClasseParametre(parametreFicheDonnees.getClasseParametre());
        param.setChampParametre(parametreFicheDonnees.getChampParametre());
        param.setLibelle(parametreFicheDonnees.getLibelle());
        param.setListeDonnee(parametreFicheDonnees.getListeDonnee());
        param.setValeurMax(parametreFicheDonnees.getValeurMax());
        param.setValeurMin(parametreFicheDonnees.getValeurMin());
        param.setValeurObligatoire(parametreFicheDonnees.getValeurObligatoire());
        param.setCritereChamp(parametreFicheDonnees.getCritereChamp());
        param.setDateAjout(param.getDateAjout());
        Date dates = new Date();
        Instant instant = dates.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        param.setDateModif(dates);
        return parametreFicheDonneesRepository.save(parametreFicheDonnees);
    }

    public List<ParametreFicheDonnees> getListe(){
        List<ParametreFicheDonnees> paramList = parametreFicheDonneesRepository.findAll();

        if(paramList.isEmpty())
            throw new EntityNotFoundException("Aucun parametre trouvé");
        return paramList;
    }

    public String deleteParametreFiche(Integer id){
        ParametreFicheDonnees parametreFicheDonnees = parametreFicheDonneesRepository.findById(id).orElseThrow(null);

        parametreFicheDonneesRepository.delete(parametreFicheDonnees);
        return "Supprimé avec succèss";
    }
}
