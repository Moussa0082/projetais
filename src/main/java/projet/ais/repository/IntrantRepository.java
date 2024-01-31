package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Intrant;

public interface IntrantRepository extends JpaRepository<Intrant , String> {
    
}
