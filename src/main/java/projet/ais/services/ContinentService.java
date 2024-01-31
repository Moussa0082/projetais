package projet.ais.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.IdGenerator;
import projet.ais.models.CategorieProduit;
import projet.ais.models.Continent;
import projet.ais.repository.ContinentRepository;

@Service
public class ContinentService {

    @Autowired
    private ContinentRepository continentRepository;
        @Autowired
    IdGenerator idGenerator ;


        //  Ajouter continent 
        public ResponseEntity<String> createContinent(Continent continent) {
        
            Continent contientExistant = continentRepository.findByNomContinent(continent.getNomContinent());
            if (contientExistant == null) {
                // Générer un numéro aléatoire
                String codeContinent = genererCode();
                String code = idGenerator.genererCode();
                // Attribuer le numéro aléatoire au continent
                    continent.setCodeContinent(codeContinent);
                    continent.setIdContinent(code);
                // Vérifier si la sous region existe déjà
                continentRepository.save(continent);
                return new ResponseEntity<>("Continent ajouté avec succès", HttpStatus.OK);
            } else {
                
                // Retourner un message d'erreur

                return new ResponseEntity<>("Continent déjà existant.", HttpStatus.BAD_REQUEST);
            }
        }
    
    public String genererCode() {
        // Générer 2 lettres aléatoires
        String lettresAleatoires = genererLettresAleatoires(2);
    
        // Générer 3 chiffres aléatoires
        String chiffresAleatoires = genererChiffresAleatoires(3);
    
    
    
        // Concaténer les parties pour former le code final
        String codeFinal = lettresAleatoires + chiffresAleatoires ;
    
        return codeFinal;
    }
    
    private String genererLettresAleatoires(int longueur) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return genererChaineAleatoire(alphabet, longueur);
    }
    
    private String genererChiffresAleatoires(int longueur) {
        String chiffres = "0123456789";
        return genererChaineAleatoire(chiffres, longueur);
    }
    
    private String genererChaineAleatoire(String source, int longueur) {
        Random random = new Random();
        StringBuilder resultat = new StringBuilder();
        for (int i = 0; i < longueur; i++) {
            int index = random.nextInt(source.length());
            resultat.append(source.charAt(index));
        }
        return resultat.toString();
    }
    
    
    
        //Modifier Continent methode
       
    
         public Continent updateContinent(Continent continent, String id){
    
         Continent continentExistant = continentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Continent introuvable "));
         continentExistant.setNomContinent(continent.getNomContinent());
         continentExistant.setDescriptionContinent(continent.getDescriptionContinent());
    
        return continentRepository.save(continentExistant);
      }
    
            //Recuperer la liste des continent
         public List<Continent> getAllContinent() throws Exception{
    
            List<Continent> continentList = continentRepository.findAll();
            if(continentList.isEmpty()){
                throw new EntityNotFoundException("Liste continent vide");
            }
    
            continentList = continentList
                    .stream().sorted((d1, d2) -> d2.getNomContinent().compareTo(d1.getNomContinent()))
                    .collect(Collectors.toList());
            return continentList;
        }
    
        //activer un continent
          public Continent active(String id) throws Exception{
        Continent ct = continentRepository.findByIdContinent(id);
        if(ct == null){
            throw new IllegalStateException("Le continent à activer n'existe pas");
        }

        try {
            ct.setStatutContinent(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation du continent : " + e.getMessage());
        }
        return continentRepository.save(ct);
    }

    //Desactiver un continent
    public Continent desactive(String id) throws Exception{
        Continent ct = continentRepository.findByIdContinent(id);
        if(ct == null){
            throw new IllegalStateException("Le continent à désactiver n'existe pas");
        }

        try {
            ct.setStatutContinent(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la désactivation  du continent : " + e.getMessage());
        }
        return continentRepository.save(ct);
    }
    
    
        //  Supprimer sous region
          public String deleteByIdContinent(String id){
            Continent continent = continentRepository.findByIdContinent(id);
            if(continent == null){
                throw new EntityNotFoundException("Désolé le continent à supprimer n'existe pas");
            }
            continentRepository.delete(continent);
            return "Continent supprimé avec succèss";
        }
       
    
    
}
