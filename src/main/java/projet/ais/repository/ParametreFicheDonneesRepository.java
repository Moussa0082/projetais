package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.ParametreFicheDonnees;

public interface ParametreFicheDonneesRepository  extends JpaRepository<ParametreFicheDonnees, Integer>{
    
}
