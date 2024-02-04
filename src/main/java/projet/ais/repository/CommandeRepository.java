package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Commande;

public interface CommandeRepository extends JpaRepository<Commande,String>{

         Commande findByIdCommande(String idCommande);

    
}
