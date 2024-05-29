package projet.ais.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Commande;
import projet.ais.models.DetailCommande;
import projet.ais.models.Stock;

public interface DetailCommandeRepository extends JpaRepository<DetailCommande,String>{

    DetailCommande findByIdDetailCommande(String idDetailCommande);
    List<DetailCommande> findByCommandeIdCommande(String idCommande);


    List<DetailCommande> findByNomProduit(String nomProduit);
    List<DetailCommande> findByCommandeIdCommande(Commande commande);
    List<DetailCommande> findByCommande(Optional<Commande> commandes);
	String countByCommande(Commande commande);
    
}
