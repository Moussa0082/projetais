package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.ParametreGeneraux;

@Repository
public interface ParametreGenerauxRepository  extends JpaRepository<ParametreGeneraux, Integer>{
    
<<<<<<< HEAD
    
=======
    ParametreGeneraux findByNomParametreGeneraux(String nomParametreGeneraux);
    


>>>>>>> 49d90da99c2457cf00eca9a3367ca837f9a26f9f
}
