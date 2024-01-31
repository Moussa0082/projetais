package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.ParametreFiche;

public interface ParametreFicheRepository extends JpaRepository<ParametreFiche , String>{
    
    ParametreFiche findByIdParametreFiche(String id);
}
