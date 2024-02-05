package projet.ais.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.TypeActeur;

public interface TypeActeurRepository extends JpaRepository<TypeActeur, String>{

    // TypeActeur findByCodeTypeActeur(String codeTypeActeur);
    TypeActeur findByIdTypeActeur(String idTypeActeur);
    TypeActeur findByLibelle(String libelle);

    List<TypeActeur> findByIdTypeActeurIn(List<String> typeActeurIds);
    
}
