package projet.ais.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.TypeMateriel;

@Repository
public interface TypeMaterielRepository extends JpaRepository<TypeMateriel, String>{
    
    TypeMateriel findByNom(String nom);

    // List<TypeMateriel> findByActeurIdActeur(String idActeur); 
}
