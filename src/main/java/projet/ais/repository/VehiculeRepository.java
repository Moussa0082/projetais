package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Acteur;
import projet.ais.models.Vehicule;

public interface VehiculeRepository extends JpaRepository<Vehicule , String> {

    Vehicule findByIdVehicule(String idVehicule);

    Vehicule findByNomVehiculeAndActeur(String nomVehicule, Acteur acteur);

    Vehicule findByNomVehiculeAndCapaciteVehiculeAndActeur(String nomVehicule, String capaciteVehicule, Acteur acteur);
    
}
