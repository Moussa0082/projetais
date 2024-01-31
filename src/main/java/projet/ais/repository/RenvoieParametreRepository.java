package projet.ais.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.RenvoieParametre;

@Repository
public interface RenvoieParametreRepository extends JpaRepository<RenvoieParametre, String>{
    
    
    RenvoieParametre findByIdRenvoiParametre(String id);
    List<RenvoieParametre> findByParametreFicheIdParametreFiche(String id);
}
