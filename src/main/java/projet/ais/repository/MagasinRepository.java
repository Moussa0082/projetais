package projet.ais.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import projet.ais.models.Acteur;
import projet.ais.models.Magasin;
import projet.ais.models.Niveau1Pays;

@Repository
public interface MagasinRepository  extends JpaRepository<Magasin, String>{
    
    Magasin findByIdMagasin(String idMagasin);
    
    List<Magasin> findByActeurIdActeur(String idActeur);

    List<Magasin> findByNiveau1PaysIdNiveau1Pays(String idNiveau1Pays);

    //   @Query("SELECT m FROM Magasin m " +
    //        "JOIN m.niveau1Pays np " +
    //        "JOIN m.acteur a " +
    //        "WHERE np.idNiveau1Pays = :idNiveau1Pays " +   
    //        "AND a.idActeur = :idActeur")
    // List<Magasin> findByNiveau1PaysAndActeur(@Param("idNiveau1Pays") String idNiveau1Pays,
    //                                          @Param("idActeur") String idActeur);
    List<Magasin> findAllByActeurIdActeurAndNiveau1PaysIdNiveau1Pays(String idActeur,String idNiveau1Pays);

    // List<Magasin> findByNiveau1PaysAndActeur(String idNiveau1Pays, String idActeur);
}
