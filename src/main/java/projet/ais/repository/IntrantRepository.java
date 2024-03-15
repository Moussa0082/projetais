package projet.ais.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Intrant;
import projet.ais.models.Superficie;

public interface IntrantRepository extends JpaRepository<Intrant , String> {

    Intrant findByIdIntrant(String idIntrant);

    List<Intrant> findByIdIntrantIn(List<String> idIntrants);

    List<Intrant> findAllByActeurIdActeur(String idIntrant);
    
    // List<Intrant> findBySuperficieIdSuperficie(String id);
}
