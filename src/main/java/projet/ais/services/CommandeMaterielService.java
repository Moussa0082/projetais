package projet.ais.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Acteur;
import projet.ais.models.CommandeMateriel;
import projet.ais.models.Materiel;
import projet.ais.repository.ActeurRepository;
import projet.ais.repository.CommandeMaterielRepository;
import projet.ais.repository.MaterielRepository;

@Service
public class CommandeMaterielService {
    
      // @Autowired
    // ActeurRepository acteurRepository;

    @Autowired
    MaterielRepository materielRepository;
    @Autowired
    CommandeMaterielRepository commandeMaterielRepository;
    @Autowired
    MessageService messageService;
    @Autowired
    CodeGenerator codeGenerator;
    @Autowired
    IdGenerator idGenerator ;
    @Autowired
    ActeurRepository acteurRepository;
    
    Map<String, CommandeMateriel> paniersEnCours = new HashMap<>();

    public String ajouterAuPanier(String idActeur, String idMateriel) throws Exception {
        Acteur acteur = acteurRepository.findByIdActeur(idActeur);
        if(acteur == null)
            throw new EntityNotFoundException("Acteur non trouvé");
    
        Materiel materiel = materielRepository.findByIdMateriel(idMateriel);
        if(materiel == null)
            throw new EntityNotFoundException("Materiel non trouvé");
    
        // Vérifier si l'acteur a un panier en cours, sinon en créer un
        CommandeMateriel commande = paniersEnCours.getOrDefault(idActeur, new CommandeMateriel());
        if (commande.getMaterielList() == null) {
            commande.setMaterielList(new ArrayList<>()); // Initialisation de la liste si elle est nulle
        }
        commande.setActeur(acteur);
        commande.setProprietaire(materiel.getActeur().getNomActeur());
        commande.getMaterielList().add(materiel);
        paniersEnCours.put(idActeur, commande);
        
        String mes = "Bonjour vous avez reçu une nouvelle commande de materiel :" + commande.getMaterielList() + "de la part de " + commande.getActeur().getNomActeur() ;
    
        try {
            messageService.sendMessageAndSave(materiel.getActeur().getWhatsAppActeur(), mes, commande.getActeur().getNomActeur());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    
        return "Ajouter avec succès";
    }
    

    public CommandeMateriel confirmerPanier(String idActeur) throws Exception {
        // Récupérer le panier en cours de l'acteur
        CommandeMateriel panierEnCours = paniersEnCours.get(idActeur);

        if (panierEnCours == null || panierEnCours.getMaterielList().isEmpty()) {
            throw new IllegalStateException("Le panier est vide ou n'existe pas.");
        }

        // Sauvegarder le panier dans la base de données
        CommandeMateriel commande = new CommandeMateriel();
        commande.setActeur(panierEnCours.getActeur());
        commande.setMaterielList(new ArrayList<>(panierEnCours.getMaterielList()));
        String codes = idGenerator.genererCode();
        commande.setIdCommandeMateriel(codes);
        commandeMaterielRepository.save(commande);

        String acteur = commande.getProprietaire();
        String mes = "Bonjour vous avez reçu une nouvelle commande de materiel :" + commande.getMaterielList() + "de la part de " + commande.getActeur().getNomActeur() ;
        // try {
        //         messageService.sendMessageAndSave(acteur.getWhatsAppActeur(), mes, commande.getActeur().getNomActeur());
        //     } catch (Exception e) {
        //         throw new Exception(e.getMessage());
        //     }
        // Effacer le panier en cours après confirmation
        paniersEnCours.remove(idActeur);

        return commande;
    }

    public List<CommandeMateriel> getAllCommandeByActeur(String idActeur) {
        // Récupérer tous les paniers de l'utilisateur depuis la base de données
        List<CommandeMateriel> commande = commandeMaterielRepository.findByActeurIdActeur(idActeur);

        commande.sort(Comparator.comparing(CommandeMateriel::getDateCommande).reversed());

        return commande;
    }

    public String supprimerMateriel(String idActeur, String idMateriel) {
        List<CommandeMateriel> commandes = commandeMaterielRepository.findByActeurIdActeur(idActeur);
    
        if (!commandes.isEmpty()) {
            for (CommandeMateriel commande : commandes) {
                List<Materiel> materiels = commande.getMaterielList();
                materiels.removeIf(m -> m.getIdMateriel().equals(idMateriel));
                // Enregistrer les modifications pour chaque commande
                commandeMaterielRepository.save(commande);
            }
            return "Matériel supprimé avec succès";
        }
    
        return "Aucune commande trouvée pour cet acteur";
    }
    

    public String viderPanier(String idActeur) {
        // Récupérer toutes les commandes associées à l'acteur
        List<CommandeMateriel> commandes = commandeMaterielRepository.findByActeurIdActeur(idActeur);
    
        if (!commandes.isEmpty()) {
            // Parcourir toutes les commandes et vider la liste de matériel de chaque commande
            for (CommandeMateriel commande : commandes) {
                commande.getMaterielList().clear();
                // Enregistrer les modifications pour chaque commande
                commandeMaterielRepository.save(commande);
            }
            return "Paniers vidés avec succès";
        }
    
        return "Aucune commande trouvée pour cet acteur";
    }
    
}
