package projet.ais.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Magasin;
import projet.ais.models.Materiel;
import projet.ais.models.Vehicule;

import java.util.*;

public interface MaterielRepository  extends JpaRepository<Materiel , String>{
    
    Materiel findByIdMateriel(String id);

    List<Materiel> findByActeurIdActeur(String id);

    List<Materiel> findByIdMaterielAndActeurIdActeur(String idMaterien, String idActeur);

    List<Materiel> findAllByTypeMaterielIdTypeMateriel(String idTypeMateriel);

    Page<Materiel> findByTypeMateriel_IdTypeMaterielAndStatutAndActeurStatutActeur(String idTypeMateriel, boolean statut, boolean statutActeur , Pageable pageable);

    Page<Materiel> findByActeur_IdActeur(String idActeur, Pageable pageable);

    Page<Materiel> findAllByStatutAndActeurStatutActeur(boolean statut, boolean statutActeur, Pageable pageable);
}
