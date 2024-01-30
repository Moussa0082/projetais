package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Acteur;
import projet.ais.models.Alerte;

public interface AlerteRepository extends JpaRepository <Alerte, Integer>{
    
    Alerte findByActeurIdActeur(Integer idActeur);

    Alerte findByActeur(Acteur acteur);

}
