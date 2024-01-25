package projet.ais.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projet.ais.models.ParametreFicheDonnees;
import projet.ais.models.RenvoieParametre;
import projet.ais.repository.ParametreFicheDonneesRepository;
import projet.ais.repository.RenvoieParametreRepository;

@Service
public class RenvoieParametreService {
    
    @Autowired
    RenvoieParametreRepository renvoieParametreRepository;
    @Autowired
    ParametreFicheDonneesRepository parametreFicheDonneesRepository;

    public RenvoieParametre createParametre(RenvoieParametre renvoieParametre){
        ParametreFicheDonnees param = parametreFicheDonneesRepository.findByIdParametre(renvoieParametre.getIdRenvoiParametre());

        if(param == null)
            throw new IllegalArgumentException("Aucune parametre trouvé");
        return renvoieParametreRepository.save(renvoieParametre);
    }

    public RenvoieParametre updateParametre(RenvoieParametre renvoieParametre, Integer id){

        RenvoieParametre ren = renvoieParametreRepository.findById(id).orElseThrow(null);
       ren.setConditionRenvoi(renvoieParametre.getConditionRenvoi());
       ren.setValeurConditionRenvoi(renvoieParametre.getValeurConditionRenvoi());
       ren.setDescriptionRenvoie(renvoieParametre.getDescriptionRenvoie());

       if(renvoieParametre.getParametreFicheDonnees() != null){
        ren.setParametreFicheDonnees(renvoieParametre.getParametreFicheDonnees());
       }

        return renvoieParametreRepository.save(ren);
    }

    public List<RenvoieParametre> getAllRenvoie(){
        List<RenvoieParametre> renvoieList= renvoieParametreRepository.findAll();

        if(renvoieList.isEmpty())
            throw new IllegalArgumentException("Aucun parametre trouvé");
        return renvoieList;
    }

    public String deleteParametre(Integer id){
        RenvoieParametre ren = renvoieParametreRepository.findById(id).orElseThrow(null);

        renvoieParametreRepository.delete(ren);
        return "Supprimé avec succèss";
    }
}
