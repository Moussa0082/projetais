package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.RegroupementParametre;

@Repository
public interface RegroupementParametreRepository  extends JpaRepository<RegroupementParametre, String>{
    
    RegroupementParametre findByIdRegroupement(String id);
}
