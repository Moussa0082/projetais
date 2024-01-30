package projet.ais.services;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.CategorieProduit;
import projet.ais.models.Filiere;
import projet.ais.repository.CategorieProduitRepository;
import projet.ais.repository.FiliereRepository;
import com.sun.jdi.request.DuplicateRequestException;


@Service
public class CategorieService {
    
    @Autowired
    CategorieProduitRepository categorieProduitRepository;
   
    @Autowired
    FiliereRepository filiereRepository;
    @Autowired
    CodeGenerator codeGenerator;
    @Autowired
    IdGenerator idGenerator ;

    public CategorieProduit createCategorie(CategorieProduit categorieProduit){
        CategorieProduit categorieProduits = categorieProduitRepository.findBylibelleCategorie(categorieProduit.getLibelleCategorie());
        Filiere filiere  = filiereRepository.findByIdFiliere(categorieProduit.getFiliere().getIdFiliere());

        if(categorieProduits != null)
            throw new DuplicateRequestException("Cette catégorie existe déjà");
        
        if(filiere == null)
            throw new EntityNotFoundException("Ce filiere n'existe pas");
        
            String codes = codeGenerator.genererCode();
            String Idcodes = idGenerator.genererCode();
            categorieProduit.setCodeCategorie(codes);
            categorieProduit.setIdCategorieProduit(Idcodes);
            Date dates = new Date();
            Instant instant = dates.toInstant();
            ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
            categorieProduit.setDateAjout(dates);
            categorieProduit.setDateModif(dates);

        return categorieProduitRepository.save(categorieProduit);
    }

    public CategorieProduit updateCategorie(CategorieProduit categorieProduit, String id){
        CategorieProduit categorieProduits = categorieProduitRepository.findById(id).orElseThrow(null);

        categorieProduits.setDescriptionCategorie(categorieProduit.getDescriptionCategorie());
        categorieProduits.setLibelleCategorie(categorieProduit.getLibelleCategorie());
        categorieProduits.setFiliere(categorieProduit.getFiliere());
        categorieProduits.setDateAjout(categorieProduits.getDateAjout());
        Date dates = new Date();
            Instant instant = dates.toInstant();
            ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
            
            categorieProduits.setDateModif(dates);
        return categorieProduitRepository.save(categorieProduits);
    }

    public List<CategorieProduit> getAllCategorie(){
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();

        if(categorieProduitList.isEmpty())
            throw new EntityNotFoundException("Aucun categorie trouvé");

        categorieProduitList = categorieProduitList
            .stream().sorted((d1, d2) -> d2.getLibelleCategorie().compareTo(d1.getLibelleCategorie()))
            .collect(Collectors.toList());
        return categorieProduitList;
    }

    public List<CategorieProduit> getAllCategorieByIdFiliere(String id){
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findByFiliereIdFiliere(id);

        if(categorieProduitList.isEmpty())
            throw new EntityNotFoundException("Aucun categorie  trouvé");

            categorieProduitList = categorieProduitList
                .stream().sorted((d1, d2) -> d2.getLibelleCategorie().compareTo(d1.getLibelleCategorie()))
                .collect(Collectors.toList());
        return categorieProduitList;
    }

    public String deleteCategorie(String id){
        CategorieProduit categorieProduit = categorieProduitRepository.findById(id).orElseThrow(null);

        categorieProduitRepository.delete(categorieProduit); 
        return "Supprimé avec succèss";
    }

    public CategorieProduit active(String id) throws Exception{
        CategorieProduit cat = categorieProduitRepository.findById(id).orElseThrow(null);

        try {
            cat.setStatutCategorie(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation  de la categorie : " + e.getMessage());
        }
        return categorieProduitRepository.save(cat);
    }

    public CategorieProduit desactive(String id) throws Exception{
        CategorieProduit cat = categorieProduitRepository.findById(id).orElseThrow(null);

        try {
            cat.setStatutCategorie(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation  de la categorie : " + e.getMessage());
        }
        return categorieProduitRepository.save(cat);
    }
}
