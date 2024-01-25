package projet.ais.services;

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
    
    public ParametreFicheDonnees createParametre(ParametreFicheDonnees parametreFicheDonnees){
        String codes = codeGenerator.genererCode();
        parametreFicheDonnees.setCodeParametre(codes);
        return parametreFicheDonneesRepository.save(parametreFicheDonnees);
    }

    public ParametreFicheDonnees updateParametre(ParametreFicheDonnees parametreFicheDonnees, Integer id){
        
        ParametreFicheDonnees param = parametreFicheDonneesRepository.findById(id).orElseThrow(null);
        param.setClasseParametre(parametreFicheDonnees.getClasseParametre());
        param.setChampParametre(parametreFicheDonnees.getChampParametre());
        param.setLibelleParametre(parametreFicheDonnees.getLibelleParametre());
        param.setListeDonneesParametre(parametreFicheDonnees.getListeDonneesParametre());
        param.setValeurMaxParametre(parametreFicheDonnees.getValeurMaxParametre());
        param.setValeurMinParametre(parametreFicheDonnees.getValeurMinParametre());
        param.setValeurObligatoireParametre(parametreFicheDonnees.getValeurObligatoireParametre());
        param.setCritereChampParametre(parametreFicheDonnees.getCritereChampParametre());
        
        return parametreFicheDonneesRepository.save(parametreFicheDonnees);
    }

    public List<ParametreFicheDonnees> getListe(){
        List<ParametreFicheDonnees> paramList = parametreFicheDonneesRepository.findAll();

        if(paramList.isEmpty())
            throw new EntityNotFoundException("Aucun parametre trouvé");
        return paramList;
    }

    public String deleteParametre(Integer id){
        ParametreFicheDonnees parametreFicheDonnees = parametreFicheDonneesRepository.findById(id).orElseThrow(null);

        parametreFicheDonneesRepository.delete(parametreFicheDonnees);
        return "Supprimé avec succèss";
    }
}
