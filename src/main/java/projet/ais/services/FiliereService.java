package projet.ais.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import projet.ais.models.Filiere;
import projet.ais.repository.FiliereRepository;

@Service
public class FiliereService {
    
    @Autowired
    FiliereRepository filiereRepository;

  public Filiere createFiliere(Filiere filiere){

    Filiere filieres = filiereRepository.findByLibelleFiliere(filiere.getLibelleFiliere());

    if(filieres != null)
        throw new DataIntegrityViolationException("Ce filiere existe déjà");

    return filiereRepository.save(filiere);
  }

  
  public Filiere updateFiliere(Filiere filiere, Integer id){

    Filiere filieres = filiereRepository.findById(id).orElseThrow(null);

    filieres.setDescriptionFiliere(filiere.getDescriptionFiliere());
    filieres.setLibelleFiliere(filiere.getLibelleFiliere());

    return filiereRepository.save(filieres);
  }

  
}
