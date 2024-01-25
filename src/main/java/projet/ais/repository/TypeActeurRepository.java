package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.TypeActeur;

public interface TypeActeurRepository extends JpaRepository<TypeActeur, Integer>{

    TypeActeur findByCodeTypeActeur(String codeTypeActeur);
    TypeActeur findByIdTypeActeur(Integer idTypeActeur);
    TypeActeur findByLibelle(String libelle);
    
}
