package projet.ais.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import projet.ais.models.Commande;
import projet.ais.models.DetailCommande;
import projet.ais.models.Stock;
import projet.ais.repository.CommandeRepository;
import java.util.*;
import projet.ais.repository.DetailCommandeRepository;
import projet.ais.repository.StockRepository;

@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private DetailCommandeRepository detailCommandeRepository;

    //  //Passer une commande methode
    // public ResponseEntity<String> passerCommande(Commande commande, Stock stock){
    //     //Recuperer la commande par son id
    //     Commande cmdExistant = commandeRepository.findByIdCommande(commande.getIdCommande());
    //     if(cmdExistant != null){
    //          throw new IllegalArgumentException("Une commande avec l'id "+ cmdExistant + "existe déjà");
    //     }
    //     Stock stockExistantAuPanier = stockRepository.findByCommandeIdCommande(commande.getIdCommande());
    //     if(stockExistantAuPanier != null){
    //         throw new IllegalArgumentException("Ce produit existe déjà au panier");
    //     }
        

    //     return new ResponseEntity<>("Commande passer avec succès votre numéro de commande est "+ commande.getCodeCommande(), HttpStatus.OK);
    // }

    public void ajouterStocksACommande(Commande commande, List<Stock> stocks) {
        // Créer la commande s'il n'existe pas
        Commande cm = commandeRepository.findByIdCommande(commande.getIdCommande());
        if (commande.getIdCommande() == null) {
            commandeRepository.save(commande);
        }else{
            throw new IllegalStateException("Commande déjà existante avec l'id" + cm ) ;
        }
         commande.setStatutCommande(false);
        
         // Récupérer la commande (nouvellement créée ou existante)
       Commande savedCommande = commandeRepository.save(commande);

        // Associer les stocks à la commande
        for (Stock stock : stocks) {
            DetailCommande detailCommande = new DetailCommande();
            // Remplir les colonnes de DetailCommande selon vos besoins
            //Enregistre l'id du vendeur
            detailCommande.setCodeProduit(stock.getCodeStock());
            detailCommande.setNomProduit(stock.getNomProduit());
            detailCommande.setCommande(savedCommande);
            detailCommande.setQuantiteDemande(stock.getQuantiteStock());
            
            savedCommande.setActeur(stock.getActeur());
            detailCommandeRepository.save(detailCommande);
        }

    }


    
}
