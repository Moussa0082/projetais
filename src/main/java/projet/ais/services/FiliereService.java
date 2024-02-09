package projet.ais.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.CategorieProduit;
import projet.ais.models.Filiere;
import projet.ais.repository.FiliereRepository;

import java.time.Instant;
import java.time.LocalDateTime;
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
  @Autowired
    IdGenerator idGenerator ;

  public Filiere createFiliere(Filiere filiere){
    Filiere filieres = filiereRepository.findByLibelleFiliere(filiere.getLibelleFiliere());

    if(filieres != null)
        throw new DataIntegrityViolationException("Ce filiere existe déjà");
    
    String codes = codeGenerator.genererCode();
    String Idcodes = idGenerator.genererCode();
    filiere.setCodeFiliere(codes);
    filiere.setIdFiliere(Idcodes);

            filiere.setDateModif(LocalDateTime.now());
    return filiereRepository.save(filiere);
  }

  
  public Filiere updateFiliere(Filiere filiere, String id){

    Filiere filieres = filiereRepository.findById(id).orElseThrow(null);

    filieres.setDescriptionFiliere(filiere.getDescriptionFiliere());
    filieres.setLibelleFiliere(filiere.getLibelleFiliere());
    filieres.setDateAjout(filieres.getDateAjout());
    filieres.setPersonneModif(filiere.getPersonneModif());

    filieres.setDateModif(LocalDateTime.now());
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

  public String DeleteFiliere(String id){
    Filiere filiere = filiereRepository.findById(id).orElseThrow(null);

    filiereRepository.delete(filiere);

    return "Supprimer avec succèss";
  }

  public Filiere active(String id) throws Exception{
    Filiere filieres = filiereRepository.findById(id).orElseThrow(null);

        try {
          filieres.setStatutFiliere(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation  de la filiere : " + e.getMessage());
        }
        return filiereRepository.save(filieres);
    }

    public Filiere desactive(String id) throws Exception{
      Filiere filieres = filiereRepository.findById(id).orElseThrow(null);

      try {
        filieres.setStatutFiliere(false);
      } catch (Exception e) {
          throw new Exception("Erreur lors de desactivation  de la filiere : " + e.getMessage());
      }
      return filiereRepository.save(filieres);
    }
}
