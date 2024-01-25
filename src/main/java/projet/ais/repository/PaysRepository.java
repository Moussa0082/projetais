package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Pays;
import java.util.*;

public interface PaysRepository  extends JpaRepository<Pays,Integer>{
    
    Pays findByNomPays(String nompPays);
    Pays findByIdPays(Integer idPays);


    List<Pays> findBySousRegionIdSousRegion(Integer idSousRegion);

}
