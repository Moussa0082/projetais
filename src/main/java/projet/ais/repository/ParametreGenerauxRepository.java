package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.ParametreGeneraux;

@Repository
public interface ParametreGenerauxRepository  extends JpaRepository<ParametreGeneraux, Integer>{
    
    
}
