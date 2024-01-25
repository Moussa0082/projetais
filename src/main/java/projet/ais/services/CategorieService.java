package projet.ais.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.CodeGenerator;
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

    public CategorieProduit createCategorie(CategorieProduit categorieProduit){
        CategorieProduit categorieProduits = categorieProduitRepository.findBylibelleCategorie(categorieProduit.getLibelleCategorie());
        Filiere filiere  = filiereRepository.findByIdFiliere(categorieProduit.getFiliere().getIdFiliere());

        if(categorieProduits != null)
            throw new DuplicateRequestException("Cette catégorie existe déjà");
        
        if(filiere != null)
            throw new EntityNotFoundException("Ce filiere n'existe pas");
        
            String codes = codeGenerator.genererCode();
            categorieProduit.setCodeCategorie(codes);
        return categorieProduitRepository.save(categorieProduit);
    }

    public CategorieProduit updateCategorie(CategorieProduit categorieProduit, Integer id){
        CategorieProduit categorieProduits = categorieProduitRepository.findById(id).orElseThrow(null);

        categorieProduits.setDescriptionCategorie(categorieProduit.getDescriptionCategorie());
        categorieProduits.setLibelleCategorie(categorieProduit.getLibelleCategorie());
        
        
        return categorieProduitRepository.save(categorieProduit);
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

    public List<CategorieProduit> getAllCategorieByIdFiliere(Integer id){
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findByFiliereIdFiliere(id);

        if(categorieProduitList.isEmpty())
            throw new EntityNotFoundException("Aucun categorie  trouvé");

            categorieProduitList = categorieProduitList
                .stream().sorted((d1, d2) -> d2.getLibelleCategorie().compareTo(d1.getLibelleCategorie()))
                .collect(Collectors.toList());
        return categorieProduitList;
    }

    public String deleteCategorie(Integer id){
        CategorieProduit categorieProduit = categorieProduitRepository.findById(id).orElseThrow(null);

        categorieProduitRepository.save(categorieProduit); 
        return "Supprimé avec succèss";
    }
}
