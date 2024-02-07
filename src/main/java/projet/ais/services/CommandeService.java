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
    
   public Commande passerCommande(Commande commande) throws Exception {
        
        // Commande cm = commandeRepository.findByIdCommande(commande.getIdCommande());
        // if(cm != null){

        //     throw new IllegalArgumentException("Une commande avec l'id " + cm + " existe déjà");
        // }
        
    // Vérifier si  a le même mail et le même type
      
    

            // Traitement du fichier image siege acteur
           
 
           
            // Acteur admins = acteurRepository.findByTypeActeurLibelle("Admin");
            commande.setIdCommande(idGenerator.genererCode());
            commande.setCodeCommande(codeGenerator.genererCode());
            commande.setStatutCommande(false);


                //  List<Stock> stocks = commande.getStocks();
    
                //  for (Stock stock : commande.getStocks()) {
                //      // Vérifier si le stock a suffisamment de quantité disponible
                //      double quantiteDisponible = stock.getQuantiteStock();
                    // if (quantiteDisponible < commande.getQuantiteDemande()) {
                    //     throw new Exception("Le produit " + stock.getNomProduit() + " est en rupture de stock et ne peut être ajouté à la commande.");
                    // }
                    
                    // Mettre à jour la quantité de stock disponible
                    // stock.setQuantiteStock(quantiteDisponible - commande.getQuantiteDemande());
                    
        
            // if (quantiteDisponible == 0) {
            //     throw new Exception("Le produit " + stock.getNomProduit() + " est en rupture de stock et ne peut être ajouté à la commande.");
            // }
            
            // Mettre à jour la quantité de stock
            // stock.setQuantiteStock(quantiteDisponible - commande.getQuantiteDemande());
            // commande.setNomProduit(stock.getNomProduit());
            // commande.setCodeProduit(stock.getCodeStock());
            // commande.setQuantiteNonLivree(commande.getQuantiteDemande() - commande.getQuantiteLivree());
            // // Associer le stock à la commande
            // commandeRepository.save(commande);
            //  stock.setCommande(commande);
            //  stockRepository.save(stock);
        // }

            return commande;
               
    }
    


    
}
