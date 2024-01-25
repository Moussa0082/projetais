package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.Unite;

@Repository
public interface UniteRepository extends JpaRepository<Unite, Integer>{
    
    Unite findByIdUnite(Integer id);

    Unite findByNomUnite(String nom);
}
