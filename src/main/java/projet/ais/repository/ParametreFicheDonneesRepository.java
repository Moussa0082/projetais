package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.ParametreFicheDonnees;

@Repository
public interface ParametreFicheDonneesRepository  extends JpaRepository<ParametreFicheDonnees, Integer>{
    
    ParametreFicheDonnees findByIdParametre(Integer id);
}
