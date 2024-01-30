package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import projet.ais.models.SousRegion;

public interface SousRegionRepository  extends JpaRepository<SousRegion, String>{


    SousRegion findByNomSousRegion(String nomSousRegion);
    
    SousRegion findByIdSousRegion(String idSousRegion);

    List <SousRegion> findByContinentIdContinent(String idContinent);
    
}
