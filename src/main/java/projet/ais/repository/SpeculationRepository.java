package projet.ais.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.Speculation;

@Repository
public interface SpeculationRepository extends JpaRepository<Speculation, Integer>{
    
    Speculation findByIdSpeculation(Integer idSpeculation);

    Speculation findBynomSpeculation(String nom);
    
    List<Speculation> findByCategorieProduitIdCategorieProduit(long idCategorieProduit);
}
