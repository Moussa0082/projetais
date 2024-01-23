package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Magasin;

public interface MagasinRepository  extends JpaRepository<Magasin, Integer>{
    
}
