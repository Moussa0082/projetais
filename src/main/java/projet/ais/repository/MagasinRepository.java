package projet.ais.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.Magasin;

@Repository
public interface MagasinRepository  extends JpaRepository<Magasin, String>{
    
    Magasin findByIdMagasin(String idMagasin);


    
    List<Magasin> findByActeurIdActeur(String idActeur);

    List<Magasin> findByNiveau1PaysIdNiveau1Pays(String idNiveau1Pays);
}
