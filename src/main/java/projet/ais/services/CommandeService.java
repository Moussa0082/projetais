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
import projet.ais.models.CommandeMateriel;
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
import projet.ais.repository.MaterielRepository;
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
    MaterielRepository materielRepository;

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
    
public String commandeMateriel(String idMateriel, String idActeur) throws Exception{
        Acteur ac = acteurRepository.findByIdActeur(idActeur);
        Materiel mat = materielRepository.findByIdMateriel(idMateriel);

        if(ac == null)
            throw new EntityNotFoundException("Aucun acteur trouvé");
        
        if(mat == null)
            throw new EntityNotFoundException("Aucun materiel trouvé");

            mat.setStatutCommande(true);
            
            try {
            String codes  = codeGenerator.genererCode();
            String idCode = idGenerator.genererCode();
            // Création de la commande
            Commande commande = new Commande();
            commande.setIdCommande(idCode);
            commande.setCodeCommande(codes);
            commande.setActeur(ac);
            commande.setCodeAcheteur(mat.getCodeMateriel());
            commande.setCodeCommande(mat.getCodeMateriel());
            commande.setNomProduit(mat.getNom());
            commande.setDescriptionCommande("Allocation de materiel");
            commande.setDateCommande(LocalDateTime.now());
            // Vous pouvez ajouter le matériel commandé à la liste des matériels de la commande
            commande.setMaterielList(Arrays.asList(mat));
            // Enregistrement de la commande
            commandeRepository.save(commande);
            // Envoi du message pour la commande
            String msg = "Bonjour  " + mat.getActeur().getNomActeur().toUpperCase() + " vous avez une nouvelle commande pour le matériel : "
                    + mat.getNom() + " de la part de M. " + ac.getNomActeur() + " Numéro de téléphone : "
                    + ac.getWhatsAppActeur() + " Adresse : " + ac.getAdresseActeur();
            // messageService.sendMessageAndSave(mat.getActeur().getWhatsAppActeur(), msg, ac.getNomActeur());
        } catch (Exception e) {
            throw new Exception("Erreur lors de la commande : " + e.getMessage());
        }
        return "Commande ajoutée avec succès";
    }

    public String annulerCommande(String idMateriel, String idActeur) throws Exception{
        Acteur ac = acteurRepository.findByIdActeur(idActeur);
        Materiel mat = materielRepository.findByIdMateriel(idMateriel);

        if(ac == null)
            throw new EntityNotFoundException("Aucun acteur trouvé");
        
        if(mat == null)
            throw new EntityNotFoundException("Aucun materiel trouvé");

            mat.setStatutCommande(false);
            try { 
            // Envoi du message pour la commande
            String msg = "Bonjour  " + ac.getNomActeur().toUpperCase() + " Votre commande de materiel " + mat.getNom().toUpperCase() + " a été annuler ";
            messageService.sendMessageAndSave(mat.getActeur().getWhatsAppActeur(), msg, ac.getNomActeur());
        } catch (Exception e) {
            throw new Exception("Erreur lors de la commande : " + e.getMessage());
        }
        return "Commande annuler avec succèss";
    }
    
    public Commande confirmerLivraison(String id){
        Commande commande = commandeRepository.findByIdCommande(id);

        commande.setStatutCommandeLivrer(true);

        return commandeRepository.save(commande);
        
    }

    public String confirmerCommande(String idCommande) throws Exception {

        Commande commande = commandeRepository.findByIdCommande(idCommande);

        commande.setStatutConfirmation(true);
        
        commandeRepository.save(commande);

        String msg = "Bonjour  " + commande.getActeur().getNomActeur().toUpperCase() + " Votre commande de materiel  a été confirmer ";
        try {
                messageService.sendMessageAndSave(commande.getActeur().getWhatsAppActeur(), msg, commande.getActeur().getNomActeur());
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }

        return "Commande confirmée avec succès";
    }

    public List<Commande> getAllCommandeByActeur(String idActeur) {
        // Récupérer tout les commandes de l'utilisateur depuis la base de données
        List<Commande> commande = commandeRepository.findByActeurIdActeur(idActeur);

        commande.sort(Comparator.comparing(Commande::getDateCommande).reversed());

        return commande;
    }

    public List<Commande> getCommandeByActeur(String id){
        List<Commande> commandeList =commandeRepository.findAll();

        if(commandeList.isEmpty())
            throw new EntityNotFoundException("Aucune commande trouvé");

            commandeList.sort(Comparator.comparing(Commande::getDateCommande).reversed());
        return commandeList;
    }
}
