package projet.ais.services;


import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projet.ais.IdGenerator;
import projet.ais.models.ParametreFiche;
import projet.ais.models.RegroupementParametre;
import projet.ais.models.RenvoieParametre;
import projet.ais.repository.ParametreFicheRepository;
import projet.ais.repository.RenvoieParametreRepository;

@Service
public class RenvoieParametreService {
    
    @Autowired
    RenvoieParametreRepository renvoieParametreRepository;
    @Autowired
    ParametreFicheRepository parametreFicheRepository;
      @Autowired
    IdGenerator idGenerator ;

    public RenvoieParametre createParametreRenvoie(RenvoieParametre renvoieParametre){
        ParametreFiche param = parametreFicheRepository.findByIdParametreFiche(renvoieParametre.getIdRenvoiParametre());

        if(param == null)
            throw new IllegalArgumentException("Aucune parametre trouvé");

        String code = idGenerator.genererCode();
        renvoieParametre.setIdRenvoiParametre(code);
        Date dates = new Date();
        Instant instant = dates.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        renvoieParametre.setDateModif(dates);
        renvoieParametre.setDateAjout(dates);
        return renvoieParametreRepository.save(renvoieParametre);
    }

    public RenvoieParametre updateParametreRenvoie(RenvoieParametre renvoieParametre, String id){

        RenvoieParametre ren = renvoieParametreRepository.findById(id).orElseThrow(null);
    ren.setConditionRenvoi(renvoieParametre.getConditionRenvoi());
    ren.setValeurConditionRenvoi(renvoieParametre.getValeurConditionRenvoi());
    ren.setDescriptionRenvoie(renvoieParametre.getDescriptionRenvoie());
    ren.setDateAjout(ren.getDateAjout());

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

    //  public List<RenvoieParametre> getAllRenvoieByIdParametre(Integer id){
    //      List<RenvoieParametre> renvoieList= renvoieParametreRepository.findByParametreFicheDonneesIdParametre(id);

    //      if(renvoieList.isEmpty())
    //          throw new IllegalArgumentException("Aucun parametre trouvé");
    //      return renvoieList;
    // }

    public String deleteParametreRenvoie(String id){
        RenvoieParametre ren = renvoieParametreRepository.findById(id).orElseThrow(null);

        renvoieParametreRepository.delete(ren);
        return "Supprimé avec succèss";
    }

    public RenvoieParametre active(String id) throws Exception{
        RenvoieParametre ren = renvoieParametreRepository.findById(id).orElseThrow(null);

        try {
            ren.setStatutRenvoie(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation : " + e.getMessage());
        }
        return renvoieParametreRepository.save(ren);
    }

    public RenvoieParametre desactive(String id) throws Exception{
        RenvoieParametre ren = renvoieParametreRepository.findById(id).orElseThrow(null);

        try {
            ren.setStatutRenvoie(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la desactivation : " + e.getMessage());
        }
        return renvoieParametreRepository.save(ren);
    }
}
