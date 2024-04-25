package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.Forme;
import java.util.List;


@Repository
public interface FormeRepository extends JpaRepository<Forme , String> {
    
    Forme findBylibelleForme(String libelle);
}
