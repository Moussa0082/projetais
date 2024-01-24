package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.Filiere;


@Repository
public interface FiliereRepository  extends JpaRepository<Filiere , Integer>{
    
    Filiere findByIdFiliere(Integer idFiliere);

    Filiere findByLibelleFiliere(String libelle);
}
