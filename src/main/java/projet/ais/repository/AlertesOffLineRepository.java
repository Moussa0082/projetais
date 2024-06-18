package projet.ais.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Acteur;
import projet.ais.models.Alerte;
import projet.ais.models.Alertes;
import projet.ais.models.AlertesOffLine;

public interface AlertesOffLineRepository  extends JpaRepository <AlertesOffLine, String> {
    
    AlertesOffLine findByIdAlerteOffLine(String idAlerteOffLine);
      Page<AlertesOffLine> findByPhotoAlerteOffLineIsNotNullAndStatutAlerteOffLine(boolean statutAlerteOffLine,Pageable pageable);
    Page<AlertesOffLine> findByPhotoAlerteOffLineIsNotNullAndStatutAlerteOffLineAndPays(boolean statutAlerteOffLine, String niveau3PaysActeur,
            Pageable pageable);
    AlertesOffLine findByPays(String pays);
    Page<AlertesOffLine> findByPhotoAlerteOffLineIsNotNullAndStatutAlerteOffLineTrueAndPays(String pays, Pageable pageable);
    Page<AlertesOffLine> findByPhotoAlerteOffLineIsNotNullAndStatutAlerteOffLineTrueAndPaysNot(String pays,
            Pageable pageable);

}
