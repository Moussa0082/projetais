package projet.ais.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Acteur;
import projet.ais.models.Commande;
import projet.ais.models.DetailCommande;
import projet.ais.models.Materiel;
import projet.ais.models.Stock;
import projet.ais.models.TypeActeur;
import projet.ais.repository.ActeurRepository;
import projet.ais.repository.AlerteRepository;
import projet.ais.repository.CommandeRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

     //Passer une commande methode
    public ResponseEntity<String> passerCommande(Commande commande, Stock stock){
        //Recuperer la commande par son id
        Commande cmdExistant = commandeRepository.findByIdCommande(commande.getIdCommande());
        if(cmdExistant != null){
             throw new IllegalArgumentException("Une commande avec l'id "+ cmdExistant + "existe déjà");
        }
        Stock stockExistantAuPanier = stockRepository.findByCommandeIdCommande(commande.getIdCommande());
        if(stockExistantAuPanier != null){
            throw new IllegalArgumentException("Ce produit existe déjà au panier");
        }
        
         commandeRepository.save(commande);
        return new ResponseEntity<>("Commande passer avec succès votre numéro de commande est "+ commande.getCodeCommande(), HttpStatus.OK);
    }

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
    
    // public Commande creerCommandeEtAjouterStocks(List<Stock> stocks, Commande commande) {
    //     // Créer la commande avec les détails nécessaires
    //     commande.setIdCommande(idGenerator.genererCode());
    //     commande.setCodeCommande(codeGenerator.genererCode());
    //     commande.setDateCommande(LocalDateTime.now());
    //     double quantiteDemandee = commande.getQuantiteDemande();
    //     double quantiteLivree = commande.getQuantiteLivree();
    //     double quantiteNonLivree = quantiteDemandee - quantiteLivree; // Calcul de la quantité non livrée
    //     commande.setQuantiteNonLivree(quantiteNonLivree);
    
    //     // Initialiser la liste de stocks dans la commande si elle est nulle
    //     // if (commande.getStock() == null) {
    //     //     commande.setStock(new ArrayList<>());
    //     // } 
        
    //     // Enregistrer la commande dans la base de données
    //     Commande savedCommande = commandeRepository.save(commande);
        
    //     // Pour chaque stock, associez-le à la commande
    //     for (Stock stock : stocks) {
    //         System.out.println("Liste de produit : "+ stock);
    //         // Associer le stock à la commande
    //         savedCommande.setNomProduit(stock.getNomProduit());
    //         savedCommande.setCodeProduit(stock.getCodeStock());
    //         stock.setQuantiteStock(stock.getQuantiteStock() - quantiteDemandee);
    
    //         stock.getCommande().add(savedCommande); // Assurez-vous que stock.getCommande() est initialisée
           
    //         stockRepository.save(stock);
    //     }
    //     // Enregistrer les stocks associés à la commande dans la base de données
    //     // System.out.println("Commande : "+ savedCommande);
    
    //     // Retourner la commande créée avec les stocks associés
    //     return savedCommande;
    // }
    public Commande creerCommandeAvecStocks(Commande commande) {
        // Générer un nouvel identifiant de commande et de code de commande
        String idCommande = idGenerator.genererCode();
        String codeCommande = codeGenerator.genererCode();
    
        // Associer l'identifiant et le code de commande à la nouvelle commande
        commande.setIdCommande(idCommande);
        commande.setCodeCommande(codeCommande);
    
        // Enregistrer la nouvelle commande dans la base de données
        commandeRepository.save(commande);
    
        // Récupérer la liste des stocks à partir de la commande
        List<Stock> stocks = commande.getStock();
    
        // Pour chaque stock, vérifier s'il est déjà associé à la commande
        for (Stock stock : stocks) {
            // Vérifier si le stock est déjà associé à la commande
            if (stock.getCommande() == null) {
                // Associer la commande au stock
                stock.setCommande(commande);
            } else {
                // Le stock est déjà associé à une commande
                // Vous pouvez choisir de lever une exception, ignorer le stock, ou mettre à jour la quantité existante
                // Pour l'exemple, nous mettons simplement à jour la quantité existante
                double quantiteExistante = stock.getQuantiteStock();
                double nouvelleQuantite = quantiteExistante + commande.getQuantiteDemande();
                stock.setQuantiteStock(nouvelleQuantite);
            }
    
            // Mettre à jour la quantité disponible du stock
            double nouvelleQuantite = stock.getQuantiteStock() - commande.getQuantiteDemande();
            stock.setQuantiteStock(nouvelleQuantite);
    
            // Enregistrer le stock dans la base de données
            stockRepository.save(stock);
        }
    
        // Retourner la commande créée avec les stocks associés
        return commande;
    }
    
    // public Commande creerCommandeAvecStocks(Commande commande) throws Exception {
    //     // Récupérer les produits du panier
    //     List<String> idStocks = new ArrayList<>();
    //     for (Stock stock : commande.getStock()) {
    //         idStocks.add(stock.getIdStock());
    //     }
    //     List<Stock> stockss = stockRepository.findByIdStockIn(idStocks);
    
    //     // Créer une liste pour stocker les identifiants des produits déjà présents dans le panier
    
    //     // Parcourir les produits du panier
    //     for (Stock produit : stockss) {
    //         // Rechercher l'identifiant du produit dans la table Commande
    //         Commande commandeExistante = commandeRepository.findByIdCommande(produit.getCommande().getIdCommande());
    
    //         // Si le produit n'existe pas dans la table Commande
    //         if (commandeExistante == null) {
    //             // Créer une nouvelle commande
    //             Commande nouvelleCommande = new Commande();
    //             nouvelleCommande.setIdCommande(idGenerator.genererCode());
    //              nouvelleCommande.setCodeCommande(codeGenerator.genererCode());
    //             // Enregistrer la nouvelle commande
    //             nouvelleCommande.setQuantiteNonLivree(nouvelleCommande.getQuantiteDemande() - nouvelleCommande.getQuantiteLivree());
    //             commandeRepository.save(nouvelleCommande);
    
    //             // Ajouter l'identifiant du produit à la liste des produits existants
    //             idStocks.add(produit.getCommande().getIdCommande());
    //         } else {
    //             // Augmenter la quantité commandée dans la commande existante
    //             commandeExistante.setQuantiteNonLivree(commandeExistante.getQuantiteDemande() - commandeExistante.getQuantiteDemande());
    
    //             // Mettre à jour la commande
    //             commandeRepository.save(commandeExistante);
    
    //             // Ajouter l'identifiant du produit à la liste des produits existants
    //             idStocks.add(produit.getCommande().getIdCommande());
    //         }
    //     }
    
    //     // Récupérer les stocks des produits du panier
    //     List<Stock> stocks = stockRepository.findByIdStockIn(idStocks);
    
    //     // Initialiser la liste de stocks dans la commande
    //     commande.setStock(stocks);

    
    //     // Associer chaque stock à la commande et mettre à jour les quantités
    //     for (Stock stock : stocks) {
    //         stock.setCommande(commande);
    //         stock.setQuantiteStock(stock.getQuantiteStock() - commande.getQuantiteDemande());
    //     }
        
    //     // Enregistrer la commande et les stocks
    //     commandeRepository.save(commande);
    //     stockRepository.saveAll(stocks);
        
    //     return commande;
    // }
    
    
    
    
    // public void ajouterStocksACommande(List<Stock> stocks, Commande commande) {
    //     double quantiteDemandee = commande.getQuantiteDemande();
    //     double quantiteLivree = commande.getQuantiteLivree();
    //     double quantiteNonLivree = quantiteDemandee - quantiteLivree; // Calcul de la quantité non livrée
        
    //     // Mettre à jour la quantité non livrée de la commande
    //     commande.setQuantiteNonLivree(quantiteNonLivree);
        
    //     // Pour chaque stock, associez-le à la commande et mettez à jour les informations de la commande
    //     for (Stock stock : stocks) {
    //         stock.setQuantiteStock(stock.getQuantiteStock() - quantiteDemandee);
    //         // Récupérer les informations du stock et mettre à jour les champs de la commande
    //         commande.getStock().add(stock);
    //         commande.setNomProduit(stock.getNomProduit()); // Exemple : mettre à jour le nom du produit de la commande
            
    //         // Récupérer d'autres informations du stock et les mettre à jour dans la commande si nécessaire
            
    //         // Enregistrer la commande dans la base de données
    //         commandeRepository.save(commande);
    //     }
    // }
    
    
    
    
    


    
}
