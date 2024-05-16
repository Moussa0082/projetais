package projet.ais.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Alertes;
import projet.ais.models.Alertes;

public interface AlertesRepository extends JpaRepository<Alertes , String> {
    
      Alertes findByIdAlerte(String idAlertes);
      Page<Alertes> findByPhotoAlerteIsNotNull(Pageable pageable);

}
