package projet.ais.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.CommandeMateriel;

@Repository
public interface CommandeMaterielRepository extends JpaRepository<CommandeMateriel, String> {
    
    CommandeMateriel findByIdCommandeMateriel(String idCommande);
    List<CommandeMateriel> findByActeurIdActeur(String idActeur);
}
