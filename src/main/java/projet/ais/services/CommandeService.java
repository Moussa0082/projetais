package projet.ais.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import projet.ais.models.Commande;
import projet.ais.models.DetailCommande;
import projet.ais.models.Stock;
import projet.ais.repository.AlerteRepository;
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
    private EmailService emailService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private AlerteRepository alerteRepository;

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

    public void ajouterStocksACommande(Commande commande, List<Stock> stocks) throws Exception {
        // Vérifier si la commande existe déjà
        Commande existingCommande = commandeRepository.findByIdCommande(commande.getIdCommande());
        if (existingCommande != null) {
            // Si la commande existe déjà, vous pouvez choisir de lever une exception
            throw new IllegalStateException("Commande déjà existante avec l'id " + existingCommande.getIdCommande());

            // Ou vous pouvez choisir de mettre à jour la commande existante avec les informations fournies
            
            // Vous pouvez mettre à jour la commande existante ici si nécessaire
        }
        commande.setStatutCommande(false);
        // Enregistrer la commande si elle n'existe pas encore
        Commande savedCommande = commandeRepository.save(commande);
        
        
        // Associer les stocks à la commande
        for (Stock stock : stocks) {
            double quantiteStock = stock.getQuantiteStock();
            if (quantiteStock == 0) {
                throw new Exception("La quantite de " + stock.getNomProduit() + " est non disponible");
            }
    
            // Créer un détail de commande
            DetailCommande detailCommande = new DetailCommande();
            detailCommande.setNomProduit(stock.getNomProduit());
            detailCommande.setQuantiteDemande(quantiteStock);
            detailCommande.setCodeProduit(stock.getCodeStock());
            detailCommande.setCommande(savedCommande);
            detailCommande.setQuantiteDemande(quantiteStock);
            
            // Mettre à jour la quantité de stock
            stock.setQuantiteStock(quantiteStock - detailCommande.getQuantiteDemande());
            
            // Associer l'acteur de la commande
            savedCommande.setActeur(stock.getActeur());
            
            // Enregistrer le détail de la commande
            detailCommandeRepository.save(detailCommande);
        }
    }
    
    


    
}
