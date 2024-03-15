package projet.ais.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Commande;
import projet.ais.models.DetailCommande;
import projet.ais.models.Stock;

public interface DetailCommandeRepository extends JpaRepository<DetailCommande,String>{

    DetailCommande findByIdDetailCommande(String idDetailCommande);


    List<DetailCommande> findByNomProduit(String nomProduit);
    
}
