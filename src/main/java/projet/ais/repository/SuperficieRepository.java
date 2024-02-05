package projet.ais.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.Magasin;
import projet.ais.models.Superficie;

@Repository
public interface SuperficieRepository extends JpaRepository<Superficie, String>{
    
    Superficie findByIdSuperficie(String id);

    List<Superficie> findByActeurIdActeur(String id);
    
    List<Superficie> findBySpeculationIdSpeculation(String id);
}
