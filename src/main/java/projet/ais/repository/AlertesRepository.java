package projet.ais.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Alertes;
// import projet.ais.models.Alertes;

public interface AlertesRepository extends JpaRepository<Alertes , String> {
    
      Alertes findByIdAlerte(String idAlertes);
      Page<Alertes> findByPhotoAlerteIsNotNullAndStatutAlerte(boolean statutAlerte,Pageable pageable);
    Page<Alertes> findByPhotoAlerteIsNotNullAndStatutAlerteAndPays(boolean statutAlerte, String niveau3PaysNom,
            Pageable pageable);
    Alertes findByPays(String pays);

    Page<Alertes> findByPaysAndStatutAlerte(String pays, boolean statutAlerte, Pageable pageable);

    Page<Alertes> findByPhotoAlerteIsNotNullAndStatutAlerteTrueAndPays(String payspays, Pageable pageable);
    Page<Alertes> findByPhotoAlerteIsNotNullAndStatutAlerteTrueAndPaysNot(String pays, Pageable pageable);
}
