package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.CategorieProduit;
import java.util.List;

@Repository
public interface CategorieProduitRepository  extends JpaRepository<CategorieProduit, Integer>{
    
    CategorieProduit findByIdCategorieProduit(Integer idCategorieProduit);

    CategorieProduit findBylibelleCategorie(String libelle);
    
    List<CategorieProduit> findByFiliereIdFiliere(long idFiliere);
}
