package projet.ais.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.CodeGenerator;
import projet.ais.models.Filiere;
import projet.ais.repository.FiliereRepository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FiliereService {
    
    @Autowired
    FiliereRepository filiereRepository;
    @Autowired
    CodeGenerator codeGenerator;

  public Filiere createFiliere(Filiere filiere){
    Filiere filieres = filiereRepository.findByLibelleFiliere(filiere.getLibelleFiliere());

    if(filieres != null)
        throw new DataIntegrityViolationException("Ce filiere existe déjà");
    
    String codes = codeGenerator.genererCode();
    filiere.setCodeFiliere(codes);
     Date dates = new Date();
            Instant instant = dates.toInstant();
            ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
            filiere.setDateAjout(dates);
            filiere.setDateModif(dates);
    return filiereRepository.save(filiere);
  }

  
  public Filiere updateFiliere(Filiere filiere, Integer id){

    Filiere filieres = filiereRepository.findById(id).orElseThrow(null);

    filieres.setDescriptionFiliere(filiere.getDescriptionFiliere());
    filieres.setLibelleFiliere(filiere.getLibelleFiliere());
    filieres.setDateAjout(filieres.getDateAjout());

    Date dates = new Date();
    Instant instant = dates.toInstant();
    ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
    filieres.setDateModif(dates);
    return filiereRepository.save(filieres);
  }

  public List<Filiere> getAllf(){
    List<Filiere> filiereList = filiereRepository.findAll();

    if(filiereList.isEmpty())
        throw new EntityNotFoundException("Aucun filiere trouvé");
    
        filiereList = filiereList
                .stream().sorted((d1, d2) -> d2.getLibelleFiliere().compareTo(d1.getLibelleFiliere()))
                .collect(Collectors.toList());
    return filiereList;
  }

  public String DeleteFiliere(Integer id){
    Filiere filiere = filiereRepository.findById(id).orElseThrow(null);

    filiereRepository.delete(filiere);

    return "Supprimer avec succèss";
  }
}
