package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Pays;

public interface PaysRepository  extends JpaRepository<Pays,Integer>{
    
}
