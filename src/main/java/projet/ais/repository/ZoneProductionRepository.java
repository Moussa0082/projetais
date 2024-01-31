package projet.ais.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.ZoneProduction;

@Repository
public interface ZoneProductionRepository extends JpaRepository<ZoneProduction, String>{
    
    ZoneProduction findByidZoneProduction(String idZoneProduction);

    ZoneProduction findByNomZoneProduction(String nomZoneProduction);
    
    
}
