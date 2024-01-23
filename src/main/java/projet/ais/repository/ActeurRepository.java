package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Acteur;

public interface ActeurRepository extends JpaRepository<Acteur, Integer>{

     Acteur findByIdActeur(long idActeur);

     Acteur findByEmailActeurAndPassword(String emailActeur, String Password);

     Acteur findByEmailActeur(String emailActeur);
    
}
