package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Pays;
import java.util.*;

public interface PaysRepository  extends JpaRepository<Pays,String>{
    
    Pays findByNomPays(String nompPays);
    Pays findByIdPays(String idPays);


    List<Pays> findBySousRegionIdSousRegion(String idSousRegion);

}
