package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.SousRegion;

public interface SousRegionRepository  extends JpaRepository<SousRegion, Integer>{


    SousRegion findByNomSousRegion(String nomSousRegion);
    
    SousRegion findByIdSousRegion(Integer idSousRegion);
    
}
