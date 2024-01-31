package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Materiel;
import java.util.*;

public interface MaterielRepository  extends JpaRepository<Materiel , String>{
    
    Materiel findByIdMateriel(String id);

    List<Materiel> findByActeurIdActeur(String id);
}
