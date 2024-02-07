package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.DetailCommande;

public interface DetailCommandeRepository extends JpaRepository<DetailCommande,String>{

    DetailCommande findByIdDetailCommande(String idDetailCommande);
    
}
