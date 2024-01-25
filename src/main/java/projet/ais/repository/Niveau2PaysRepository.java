package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

import projet.ais.models.Niveau2Pays;

public interface Niveau2PaysRepository  extends JpaRepository<Niveau2Pays, Integer>{
    
      Niveau2Pays findByNomN2(String nomN2);

      Niveau2Pays findByIdNiveau2Pays (Integer idNiveau2Pays);

      List<Niveau2Pays> findByNiveau1PaysIdNiveau1Pays(Integer idNiveau1Pays); 

}
