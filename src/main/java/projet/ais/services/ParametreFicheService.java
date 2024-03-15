package projet.ais.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Magasin;
import projet.ais.models.ParametreFiche;
import projet.ais.repository.ParametreFicheRepository;
import java.time.format.DateTimeFormatter;


@Service
public class ParametreFicheService {
    
    @Autowired
    ParametreFicheRepository parametreFicheRepository;
    
    @Autowired
    CodeGenerator codeGenerator;
      @Autowired
    IdGenerator idGenerator ;

    public ParametreFiche createParametreFiche(ParametreFiche parametreFiche){

        String codes = codeGenerator.genererCode();
        String idCodes = idGenerator.genererCode();
        parametreFiche.setCodeParametre(codes);
        parametreFiche.setIdParametreFiche(idCodes);
     
        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);
        parametreFiche.setDateAjout(formattedDateTime);

        return parametreFicheRepository.save(parametreFiche);
    }


    public ParametreFiche updateParametreFiche(ParametreFiche parametreFiche, String id){

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

        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);
        param.setDateModif(formattedDateTime);

        return parametreFicheRepository.save(param);
    }

    public List<ParametreFiche> getParametreFicheDonnees() {
        List<ParametreFiche> paramList = parametreFicheRepository.findAll();

        return paramList;
    }
    
    public String deleteParametreFiche(String id){
        ParametreFiche parametreFiche = parametreFicheRepository.findById(id).orElseThrow(null);

        parametreFicheRepository.delete(parametreFiche);
        return "supprimé avec succèss";
    }

    public ParametreFiche active(String id) throws Exception{
        ParametreFiche parametreFiche = parametreFicheRepository.findById(id).orElseThrow(null);

        try {
            parametreFiche.setStatutParametre(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation : " + e.getMessage());
        }
        return parametreFicheRepository.save(parametreFiche);
    }

    public ParametreFiche desactive(String id) throws Exception{
        ParametreFiche parametreFiche = parametreFicheRepository.findById(id).orElseThrow(null);

        try {
            parametreFiche.setStatutParametre(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de desactiver  : " + e.getMessage());
        }
        return parametreFicheRepository.save(parametreFiche);
    }
}
