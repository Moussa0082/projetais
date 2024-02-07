package projet.ais.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Acteur;
import projet.ais.models.Commande;
import projet.ais.models.DetailCommande;
import projet.ais.models.Materiel;
import projet.ais.models.Stock;
import projet.ais.repository.ActeurRepository;
import projet.ais.repository.AlerteRepository;
import projet.ais.repository.CommandeRepository;

import java.time.LocalDateTime;
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
     private ActeurRepository acteurRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private AlerteRepository alerteRepository;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private CodeGenerator codeGenerator;

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

    // public void ajouterStocksACommande(Commande commande) throws Exception {
    //     // Vérifier si la commande existe déjà
    //     Commande existingCommande = commandeRepository.findByIdCommande(commande.getIdCommande());
        
    //     // Si la commande n'existe pas encore, la sauvegarder avec un nouvel ID et un nouveau code
    //     if (existingCommande == null) {
    //         commande.setIdCommande(idGenerator.genererCode());
    //         commande.setCodeCommande(codeGenerator.genererCode());
    //         commande.setStatutCommande(false); // Set initial status
    //     }
        
    //     // Enregistrer la commande (ou la mise à jour de la commande existante)
    //     // Commande savedCommande = commandeRepository.save(commande);
        
    //     // Récupérer la liste de stocks de la commande
    //     List<Stock> stocks = commande.getStocks();
    
    //     for (Stock stock : stocks) {
    //         double quantiteStock = stock.getQuantiteStock();
        
    //         if (quantiteStock == 0) {
    //             throw new Exception("Le produit " + stock.getNomProduit() + " est en rupture de stock et ne peut être ajouté à la commande.");
    //         }
            
    //         // Mettre à jour la quantité de stock
    //         stock.setQuantiteStock(quantiteStock - commande.getQuantiteDemande());
    //         commande.setNomProduit(stock.getNomProduit());
    //         commande.setCodeProduit(stock.getCodeStock());
    //         commande.setQuantiteDemande(quantiteStock);
    //         commande.setQuantiteNonLivree(quantiteStock);
    //         // Associer le stock à la commande
    //         commandeRepository.save(commande);
    //          stock.setCommande(commande);
    //          stockRepository.save(stock);
    //     }
    // }
    
    public String commande(String idActeur, Commande commande, List<String> idStock) throws Exception {
        Acteur ac = acteurRepository.findByIdActeur(idActeur);
        
        if (ac == null)
            throw new EntityNotFoundException("Aucun acteur trouvé");
        
        // Récupérer les stocks correspondant aux identifiants donnés
        List<Stock> stocks = stockRepository.findByIdStockIn(idStock);
    
        if (stocks.isEmpty())
            throw new EntityNotFoundException("Aucun produit trouvé");
        
        try {
            String codes = codeGenerator.genererCode();
            String idCode = idGenerator.genererCode();
    
            // Création de la commande
            commande.setIdCommande(idCode);
            commande.setCodeCommande(codes);
            commande.setActeur(ac);
            commande.setDateCommande(LocalDateTime.now());
            
            // Ajouter chaque stock à la commande
            for (Stock stock : stocks) {
                double quantiteStock = stock.getQuantiteStock();
    
                if (quantiteStock == 0) {
                    throw new Exception("Le produit " + stock.getNomProduit() + " est en rupture de stock et ne peut être ajouté à la commande.");
                }
                
                // Renseigner les champs de la commande avec les informations du stock
                Commande commandeItem = new Commande();
                commandeItem.setNomProduit(stock.getNomProduit());
                commandeItem.setCodeProduit(stock.getCodeStock());
                commandeItem.setQuantiteDemande(quantiteStock);
                commandeItem.setQuantiteNonLivree(quantiteStock);
                
                // Associer le stock à la commande
                stock.setCommande(commande);
                
                // Mettre à jour la quantité de stock
                stock.setQuantiteStock(quantiteStock - commandeItem.getQuantiteDemande());
                
                // Enregistrer le stock mis à jour
                stockRepository.save(stock);
            }
            
            // Enregistrer la commande
            commandeRepository.save(commande);
            
            return "Commande ajoutée avec succès";
        } catch (Exception e) {
            throw new Exception("Erreur lors de la commande : " + e.getMessage());
        }
    }
    


    
}
