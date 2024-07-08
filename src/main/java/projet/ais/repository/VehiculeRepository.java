package projet.ais.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Acteur;
import projet.ais.models.Stock;
import projet.ais.models.Vehicule;
import java.util.*;

public interface VehiculeRepository extends JpaRepository<Vehicule , String> {

    Vehicule findByIdVehicule(String idVehicule);


    List<Vehicule> findAllByActeurIdActeur(String idActeur);
    
    List<Vehicule> findAllByTypeVoitureIdTypeVoiture(String idTypeVoiture);


    Vehicule findByNomVehiculeAndActeur(String nomVehicule, Acteur acteur);

    Vehicule findByNomVehiculeAndCapaciteVehiculeAndActeur(String nomVehicule, String capaciteVehicule, Acteur acteur);


    Page<Vehicule> findByActeur_IdActeur(String idActeur, Pageable pageable);


    Page<Vehicule> findByTypeVoiture_IdTypeVoitureAndStatutVehiculeAndActeurStatutActeur(String idTypeVoiture, boolean statutVehicule, boolean statutActeur , Pageable pageable);


    Page<Vehicule> findAllByStatutVehiculeAndActeurStatutActeur(boolean statutVehicule, boolean statutActeur, Pageable pageable);


    Page<Vehicule> findAllByStatutVehiculeTrueAndPaysAndActeurStatutActeurTrue(String pays, Pageable pageable);


    Page<Vehicule> findAllByStatutVehiculeTrueAndActeurStatutActeurTrueAndPaysNot(String pays,
            Pageable complementPageable);


    Page<Vehicule> findAllByTypeVoiture_IdTypeVoiture(
            String idTypeVoiture, Pageable pageable);
//     Page<Vehicule> findAllByTypeVoiture_IdTypeVoitureAndPays(
//             String idTypeVoiture, String pays, Pageable pageable);
//     Page<Vehicule> findAllByTypeVoiture_IdTypeVoitureAndStatutVehiculeTrueAndPaysAndActeurStatutActeurTrue(
//             String idTypeVoiture, String pays, Pageable pageable);


    Page<Vehicule> findAllByTypeVoiture_IdTypeVoitureAndStatutVehiculeTrueAndActeurStatutActeurTrueAndPaysNot(
            String idTypeVoiture, String pays, Pageable complementPageable);


    Page<Vehicule> findAllByTypeVoiture_IdTypeVoitureAndStatutVehiculeTrueAndActeurStatutActeurTrue(
            String idTypeVoiture, Pageable pageable);
    
}
