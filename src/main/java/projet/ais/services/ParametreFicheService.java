package projet.ais.services;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import projet.ais.CodeGenerator;
import projet.ais.models.ParametreFiche;
import projet.ais.repository.ParametreFicheRepository;

@Service
public class ParametreFicheService {
    
    @Autowired
    ParametreFicheRepository parametreFicheRepository;
    
    @Autowired
    CodeGenerator codeGenerator;

    public ParametreFiche createParametreFiche(ParametreFiche parametreFiche){

        String codes = codeGenerator.genererCode();
        parametreFiche.setCodeParametre(codes);
        Date dates = new Date();
            Instant instant = dates.toInstant();
            ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
            parametreFiche.setDateAjout(dates);
            parametreFiche.setDateModif(dates);

        return parametreFicheRepository.save(parametreFiche);
    }


    public ParametreFiche updateParametreFiche(ParametreFiche parametreFiche, Integer id){

        ParametreFiche param = parametreFicheRepository.findById(id).orElseThrow(null);
        param.setClasseParametre(parametreFiche.getClasseParametre());
        param.setChampParametre(parametreFiche.getChampParametre());
        param.setLibelleParametre(parametreFiche.getLibelleParametre());
        param.setTypeDonneeParametre(parametreFiche.getTypeDonneeParametre());
        param.setListeDonneeParametre(parametreFiche.getListeDonneeParametre());
        param.setValeurMax(parametreFiche.getValeurMax());
        param.setValeurMin(parametreFiche.getValeurMin());
        param.setValeurObligatoire(parametreFiche.getValeurObligatoire());
        param.setCritereChampParametre(parametreFiche.getCritereChampParametre());
        param.setDateAjout(parametreFiche.getDateAjout());

        Date dates = new Date();
            Instant instant = dates.toInstant();
            ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
            param.setDateModif(dates);

        return parametreFicheRepository.save(param);
    }

    public List<ParametreFiche> getParametreFicheDonnees() {
        List<ParametreFiche> paramList = parametreFicheRepository.findAll();

        return paramList;
    }
    
    public String deleteParametreFiche(Integer id){
        ParametreFiche parametreFiche = parametreFicheRepository.findById(id).orElseThrow(null);

        parametreFicheRepository.delete(parametreFiche);
        return "supprimé avec succèss";
    }
}
