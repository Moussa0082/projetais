package projet.ais.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.Speculation;
import projet.ais.models.Superficie;

@Repository
public interface SpeculationRepository extends JpaRepository<Speculation, String>{
    
    Speculation findByIdSpeculation(String idSpeculation);

    Speculation findBynomSpeculation(String nom);
    
        List<Speculation> findByActeurIdActeur(String id);

    List<Speculation> findByCategorieProduitIdCategorieProduit(String idCategorieProduit);

    List<Speculation> findByCategorieProduitIdCategorieProduitIn(List<String> idsJson);

}
