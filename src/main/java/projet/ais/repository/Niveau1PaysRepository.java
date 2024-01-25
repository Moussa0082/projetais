package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import projet.ais.models.Niveau1Pays;

public interface Niveau1PaysRepository extends JpaRepository<Niveau1Pays, Integer>{
    

    Niveau1Pays findByNomN1(String nomN1);
    Niveau1Pays findByIdNiveau1Pays(Integer id);

    List<Niveau1Pays> findByPaysIdPays(Integer idPays);

    

}
