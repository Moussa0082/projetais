package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Monnaie;

public interface MonnaieRepository extends JpaRepository<Monnaie,String>{
    
}
