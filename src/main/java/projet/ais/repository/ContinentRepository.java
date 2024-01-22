package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Continent;

public interface ContinentRepository  extends JpaRepository<Continent , Integer>{
    
}
