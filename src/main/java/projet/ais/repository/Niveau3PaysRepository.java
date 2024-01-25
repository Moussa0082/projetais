package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import projet.ais.models.Niveau3Pays;

public interface Niveau3PaysRepository  extends JpaRepository<Niveau3Pays, Integer>{
    
    Niveau3Pays findByNomN3(String nomN3);

    Niveau3Pays findByIdNiveau3Pays(Integer idNiveau3Pays);

    List<Niveau3Pays> findByNiveau2PaysIdNiveau2Pays(Integer idNiveau2Pays);

}
