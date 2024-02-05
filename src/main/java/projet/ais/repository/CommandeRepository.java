package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.Commande;

@Repository
public interface CommandeRepository extends JpaRepository<Commande,String>{

         Commande findByIdCommande(String idCommande);

    
}
