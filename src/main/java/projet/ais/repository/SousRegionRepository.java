package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import projet.ais.models.SousRegion;

public interface SousRegionRepository  extends JpaRepository<SousRegion, Integer>{


    SousRegion findByNomSousRegion(String nomSousRegion);
    
    SousRegion findByIdSousRegion(Integer idSousRegion);

    List <SousRegion> findByContinentIdContinent(Integer idContinent);
    
}
