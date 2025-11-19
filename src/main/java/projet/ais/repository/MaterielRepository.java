package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projet.ais.models.Materiels;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface MaterielRepository  extends JpaRepository<Materiels, String>{
    

    Materiels findByIdMateriel(String id);

    List<Materiels> findByActeurIdActeur(String id);

    List<Materiels> findByIdMaterielAndActeurIdActeur(String idMaterien, String idActeur);

    List<Materiels> findAllByTypeMaterielIdTypeMateriel(String idTypeMateriel);

    Page<Materiels> findByTypeMateriel_IdTypeMateriel(String idTypeMateriel, Pageable pageable);
//     Page<Materiel> findByTypeMateriel_IdTypeMaterielAndStatutAndActeurStatutActeur(String idTypeMateriel, boolean statut, boolean statutActeur , Pageable pageable);

    Page<Materiels> findByActeur_IdActeur(String idActeur, Pageable pageable);

    // Page<Materiels> findAllMateriel(Pageable pageable);


}
