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

    Page<Materiels> findAllByStatutAndActeurStatutActeurAndSpeculationIsNull(boolean statut, boolean statutActeur, Pageable pageable);

//  Page<Materiel> findBySpeculation_CategorieProduit_filiere_libelleFiliereAndStatutAndActeurStatutActeur(String pays,String libelleFiliere,boolean statut, boolean statutActeur, Pageable pageable);
//  Page<Materiel> findAllBySpeculation_CategorieProduit_filiere_libelleFiliereAndStatutTrueAndActeurStatutActeurTrueAndPaysNot(String libelleFiliere,boolean statut, boolean statutActeur,String pays, Pageable pageable);
Page<Materiels> findBySpeculation_CategorieProduit_Filiere_LibelleFiliereAndPays(
        String libelleFiliere, String pays, Pageable pageable);

    Page<Materiels> findAllBySpeculation_CategorieProduit_Filiere_LibelleFiliereAndPaysNot(
        String libelleFiliere, String pays, Pageable pageable);
//         Page<Materiel> findBySpeculation_CategorieProduit_filiere_libelleFiliereAndStatutAndActeurStatutActeur(
//         String libelleFiliere, String pays, Pageable pageable);

//     Page<Materiel> findAllBySpeculation_CategorieProduit_filiere_libelleFiliereAndStatutTrueAndActeurStatutActeurTrueAndPaysNot(
//         String libelleFiliere, String pays, Pageable pageable);

        Page<Materiels> findAllByStatutTrueAndPaysAndActeurStatutActeurTrue(String pays, Pageable pageable);
        Page<Materiels> findAllByStatutTrueAndPaysAndActeurStatutActeurTrueAndSpeculationIsNull(String pays, Pageable pageable);

        Page<Materiels> findAllByStatutTrueAndActeurStatutActeurTrueAndPaysNotAndSpeculationIsNull(String pays, Pageable pageable);

        Page<Materiels> findAllByTypeMaterielIdTypeMaterielAndStatutTrueAndPaysAndActeurStatutActeurTrue(String idTypeMateriel,
                String pays, Pageable pageable);

        Page<Materiels> findAllByTypeMateriel_IdTypeMaterielAndStatutTrueAndActeurStatutActeurTrueAndPaysNot(
                        String idTypeMateriel, String pays, Pageable complementPageable);

        Page<Materiels> findAllByTypeMateriel_IdTypeMaterielAndStatutTrueAndActeurStatutActeurTrue(String idTypeMateriel,
                Pageable pageable);

                Page<Materiels> findByTypeMateriel_IdTypeMaterielAndSpeculation_CategorieProduit_Filiere_LibelleFiliereAndPays(
                        String idTypeMateriel, String libelleFiliere, String pays, Pageable pageable);
                
                    Page<Materiels> findAllByTypeMateriel_IdTypeMaterielAndSpeculation_CategorieProduit_Filiere_LibelleFiliereAndPaysNot(
                        String idTypeMateriel, String libelleFiliere, String pays, Pageable pageable);
}
