package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projet.ais.models.Historique;


@Repository
public interface HistoriqueRepository  extends JpaRepository<Historique, String>{
    
}
