package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.GreenApi;

public interface GreenRepository extends JpaRepository<GreenApi , String> {
    
}
