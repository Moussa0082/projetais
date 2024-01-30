package projet.ais.services;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import projet.ais.CodeGenerator;
import projet.ais.models.Magasin;
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

    public ParametreFiche active(Integer id) throws Exception{
        ParametreFiche parametreFiche = parametreFicheRepository.findById(id).orElseThrow(null);

        try {
            parametreFiche.setStatutParametre(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation : " + e.getMessage());
        }
        return parametreFicheRepository.save(parametreFiche);
    }

    public ParametreFiche desactive(Integer id) throws Exception{
        ParametreFiche parametreFiche = parametreFicheRepository.findById(id).orElseThrow(null);

        try {
            parametreFiche.setStatutParametre(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de desactiver  : " + e.getMessage());
        }
        return parametreFicheRepository.save(parametreFiche);
    }
}
