package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.CategorieProduit;
import java.util.List;


@Repository
public interface CategorieProduitRepository  extends JpaRepository<CategorieProduit, String>{
    
    CategorieProduit findByIdCategorieProduit(String idCategorieProduit);

    CategorieProduit findBylibelleCategorie(String libelle);
    
    List<CategorieProduit> findByFiliereIdFiliere(String idFiliere);

    // List<CategorieProduit> findByMagasinIdMagasin(String idMagasin);
}
