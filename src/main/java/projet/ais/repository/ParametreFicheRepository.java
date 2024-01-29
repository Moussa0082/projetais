package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.ParametreFiche;

public interface ParametreFicheRepository extends JpaRepository<ParametreFiche , Integer>{
    
    ParametreFiche findByIdParametreFiche(Integer id);
}
