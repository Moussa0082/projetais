package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

import projet.ais.models.Niveau2Pays;

public interface Niveau2PaysRepository  extends JpaRepository<Niveau2Pays, String>{
    
      Niveau2Pays findByNomN2(String nomN2);

      Niveau2Pays findByIdNiveau2Pays (String idNiveau2Pays);

      List<Niveau2Pays> findByNiveau1PaysIdNiveau1Pays(String idNiveau1Pays); 

}
