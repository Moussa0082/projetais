package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Alerte;

public interface AlerteRepository extends JpaRepository <Alerte, Long>{
    
}
