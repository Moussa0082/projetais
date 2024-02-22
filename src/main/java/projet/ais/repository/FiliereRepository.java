package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.Filiere;
import projet.ais.models.Stock;

import java.util.List;


@Repository
public interface FiliereRepository  extends JpaRepository<Filiere , String>{
    
    Filiere findByIdFiliere(String idFiliere);

    Filiere findByLibelleFiliere(String libelle);
    List<Filiere> findByActeurIdActeur(String id);
}
