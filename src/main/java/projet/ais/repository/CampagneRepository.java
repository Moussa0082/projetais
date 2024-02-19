package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;

import projet.ais.models.Campagne;
import projet.ais.models.Superficie;

import java.util.List;


@Controller
public interface CampagneRepository extends JpaRepository<Campagne, String>{
    
    Campagne findByIdCampagne(String idCampagne);
    Campagne findByNomCampagne(String nom);

    List<Campagne> findByActeurIdActeur(String id);

}
