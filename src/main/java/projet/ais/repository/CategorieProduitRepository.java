package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.CategorieProduit;

public interface CategorieProduitRepository  extends JpaRepository<CategorieProduit, Integer>{
    
}
