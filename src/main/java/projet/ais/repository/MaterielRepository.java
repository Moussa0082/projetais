package projet.ais.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Intrant;
import projet.ais.models.Magasin;
import projet.ais.models.Materiel;
import projet.ais.models.Stock;
import projet.ais.models.Vehicule;

import java.util.*;

public interface MaterielRepository  extends JpaRepository<Materiel , String>{
    
    Materiel findByIdMateriel(String id);

    List<Materiel> findByActeurIdActeur(String id);

    List<Materiel> findByIdMaterielAndActeurIdActeur(String idMaterien, String idActeur);

    List<Materiel> findAllByTypeMaterielIdTypeMateriel(String idTypeMateriel);

    Page<Materiel> findByTypeMateriel_IdTypeMateriel(String idTypeMateriel, Pageable pageable);
//     Page<Materiel> findByTypeMateriel_IdTypeMaterielAndStatutAndActeurStatutActeur(String idTypeMateriel, boolean statut, boolean statutActeur , Pageable pageable);

    Page<Materiel> findByActeur_IdActeur(String idActeur, Pageable pageable);

    Page<Materiel> findAllByStatutAndActeurStatutActeurAndSpeculationIsNull(boolean statut, boolean statutActeur, Pageable pageable);

//  Page<Materiel> findBySpeculation_CategorieProduit_filiere_libelleFiliereAndStatutAndActeurStatutActeur(String pays,String libelleFiliere,boolean statut, boolean statutActeur, Pageable pageable);
//  Page<Materiel> findAllBySpeculation_CategorieProduit_filiere_libelleFiliereAndStatutTrueAndActeurStatutActeurTrueAndPaysNot(String libelleFiliere,boolean statut, boolean statutActeur,String pays, Pageable pageable);
Page<Materiel> findBySpeculation_CategorieProduit_Filiere_LibelleFiliereAndPays(
        String libelleFiliere, String pays, Pageable pageable);

    Page<Materiel> findAllBySpeculation_CategorieProduit_Filiere_LibelleFiliereAndPaysNot(
        String libelleFiliere, String pays, Pageable pageable);
//         Page<Materiel> findBySpeculation_CategorieProduit_filiere_libelleFiliereAndStatutAndActeurStatutActeur(
//         String libelleFiliere, String pays, Pageable pageable);

//     Page<Materiel> findAllBySpeculation_CategorieProduit_filiere_libelleFiliereAndStatutTrueAndActeurStatutActeurTrueAndPaysNot(
//         String libelleFiliere, String pays, Pageable pageable);

        Page<Materiel> findAllByStatutTrueAndPaysAndActeurStatutActeurTrue(String pays, Pageable pageable);
        Page<Materiel> findAllByStatutTrueAndPaysAndActeurStatutActeurTrueAndSpeculationIsNull(String pays, Pageable pageable);

        Page<Materiel> findAllByStatutTrueAndActeurStatutActeurTrueAndPaysNotAndSpeculationIsNull(String pays, Pageable pageable);

        Page<Materiel> findAllByTypeMaterielIdTypeMaterielAndStatutTrueAndPaysAndActeurStatutActeurTrue(String idTypeMateriel,
                String pays, Pageable pageable);

        Page<Materiel> findAllByTypeMateriel_IdTypeMaterielAndStatutTrueAndActeurStatutActeurTrueAndPaysNot(
                        String idTypeMateriel, String pays, Pageable complementPageable);

        Page<Materiel> findAllByTypeMateriel_IdTypeMaterielAndStatutTrueAndActeurStatutActeurTrue(String idTypeMateriel,
                Pageable pageable);

                Page<Materiel> findByTypeMateriel_IdTypeMaterielAndSpeculation_CategorieProduit_Filiere_LibelleFiliereAndPays(
                        String idTypeMateriel, String libelleFiliere, String pays, Pageable pageable);
                
                    Page<Materiel> findAllByTypeMateriel_IdTypeMaterielAndSpeculation_CategorieProduit_Filiere_LibelleFiliereAndPaysNot(
                        String idTypeMateriel, String libelleFiliere, String pays, Pageable pageable);
                        
}
