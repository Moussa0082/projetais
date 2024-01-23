package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Filiere;



public interface FiliereRepository  extends JpaRepository<Filiere , Integer>{
    
    Filiere findByIdFiliere(Integer idFiliere);

    Filiere findByLibelleFiliere(String libelle);
}
