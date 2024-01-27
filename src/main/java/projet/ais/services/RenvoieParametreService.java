package projet.ais.services;


import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
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

    public RenvoieParametre createParametreRenvoie(RenvoieParametre renvoieParametre){
        ParametreFicheDonnees param = parametreFicheDonneesRepository.findByIdParametre(renvoieParametre.getIdRenvoiParametre());

        if(param == null)
            throw new IllegalArgumentException("Aucune parametre trouvé");

        Date dates = new Date();
        Instant instant = dates.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        renvoieParametre.setDateModif(dates);
        renvoieParametre.setDateAjout(dates);
        return renvoieParametreRepository.save(renvoieParametre);
    }

    public RenvoieParametre updateParametreRenvoie(RenvoieParametre renvoieParametre, Integer id){

        RenvoieParametre ren = renvoieParametreRepository.findById(id).orElseThrow(null);
    ren.setConditionRenvoi(renvoieParametre.getConditionRenvoi());
    ren.setValeurConditionRenvoi(renvoieParametre.getValeurConditionRenvoi());
    ren.setDescriptionRenvoie(renvoieParametre.getDescriptionRenvoie());
        ren.setDateAjout(ren.getDateAjout());
    //    if(renvoieParametre.getParametreFicheDonnees() != null){
    //     ren.setParametreFicheDonnees(renvoieParametre.getParametreFicheDonnees());
    //    }
    Date dates = new Date();
    Instant instant = dates.toInstant();
    ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
    ren.setDateModif(dates);
        return renvoieParametreRepository.save(ren);
    }

    public List<RenvoieParametre> getAllRenvoie(){
        List<RenvoieParametre> renvoieList= renvoieParametreRepository.findAll();

        if(renvoieList.isEmpty())
            throw new IllegalArgumentException("Aucun parametre trouvé");
        return renvoieList;
    }

     public List<RenvoieParametre> getAllRenvoieByIdParametre(Integer id){
         List<RenvoieParametre> renvoieList= renvoieParametreRepository.findByParametreFicheDonneesIdParametre(id);

         if(renvoieList.isEmpty())
             throw new IllegalArgumentException("Aucun parametre trouvé");
         return renvoieList;
    }

    public String deleteParametreRenvoie(Integer id){
        RenvoieParametre ren = renvoieParametreRepository.findById(id).orElseThrow(null);

        renvoieParametreRepository.delete(ren);
        return "Supprimé avec succèss";
    }
}