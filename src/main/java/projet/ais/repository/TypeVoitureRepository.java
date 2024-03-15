package projet.ais.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.TypeVoiture;

@Repository
public interface TypeVoitureRepository extends JpaRepository<TypeVoiture, String>{
    
    TypeVoiture findByNom(String nom);

    List<TypeVoiture> findByActeurIdActeur(String idActeur); 
}
