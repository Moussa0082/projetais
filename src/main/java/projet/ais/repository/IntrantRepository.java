package projet.ais.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Intrant;

public interface IntrantRepository extends JpaRepository<Intrant , String> {

    Intrant findByIdIntrant(String idIntrant);

    List<Intrant> findAllByActeurIdActeur(String idIntrant);
    
}
