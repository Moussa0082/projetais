package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Conseil;

public interface ConseilRepository  extends JpaRepository<Conseil , String>{
    
}
