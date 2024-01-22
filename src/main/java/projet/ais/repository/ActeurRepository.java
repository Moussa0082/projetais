package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Acteur;

public interface ActeurRepository extends JpaRepository<Acteur, Integer>{
    
}
