package projet.ais.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.Magasin;

@Repository
public interface MagasinRepository  extends JpaRepository<Magasin, Integer>{
    
    Magasin findByIdMagasin(Integer idMagasin);
    
    List<Magasin> findByActeurIdActeur(Integer idActeur);
}
