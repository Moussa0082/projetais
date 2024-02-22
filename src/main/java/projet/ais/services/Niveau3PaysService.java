package projet.ais.services;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.IdGenerator;
import projet.ais.models.Niveau3Pays;
import projet.ais.repository.Niveau3PaysRepository;
import com.sun.jdi.request.DuplicateRequestException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class Niveau3PaysService {

    @Autowired
    private Niveau3PaysRepository niveau3PaysRepository;
    @Autowired
    IdGenerator idGenerator ;
    

    //  Ajouter niveau 3 pays 
    public Niveau3Pays createNiveau3Pays(Niveau3Pays niveau3Pays) {

        Niveau3Pays niveau3PaysExistant = niveau3PaysRepository.findByNomN3(niveau3Pays.getNomN3());
       
        if (niveau3PaysExistant != null) 
            throw new DuplicateRequestException("Cette niveau existe déjà");
        // Générer un numéro aléatoire
        String codeN3 = genererCode();
        String code = idGenerator.genererCode();
    
        // Attribuer le numéro aléatoire au niveau1
        niveau3Pays.setCodeN3(codeN3);
        niveau3Pays.setIdNiveau3Pays(code);
        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);       
        niveau3Pays.setDateModif(formattedDateTime);
    return niveau3PaysRepository.save(niveau3Pays);
        
    }

public String genererCode() {
    // Générer 2 lettres aléatoires
    String lettresAleatoires = genererLettresAleatoires(1);

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



    //Modifier niveau3Pays methode
   

     public Niveau3Pays updateNiveau3Pays(Niveau3Pays niveau3Pays, String id){

     Niveau3Pays niveau3PaysExistant= niveau3PaysRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Niveau1Pays introuvable"));
     niveau3PaysExistant.setNomN3(niveau3Pays.getNomN3());
    niveau3PaysExistant.setDescriptionN3(niveau3Pays.getDescriptionN3());
    niveau3PaysExistant.setNiveau2Pays(niveau3Pays.getNiveau2Pays());

    String pattern = "yyyy-MM-dd HH:mm";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    LocalDateTime now = LocalDateTime.now();
    String formattedDateTime = now.format(formatter);
    niveau3PaysExistant.setDateModif(formattedDateTime);

    return niveau3PaysRepository.save(niveau3PaysExistant);
  }

        //Recuperer la liste des niveau3Pays
     public List<Niveau3Pays> getAllNiveau3Pays() throws Exception{

        List<Niveau3Pays> niveau3PaysList = niveau3PaysRepository.findAll();
        if(niveau3PaysList.isEmpty()){
            throw new EntityNotFoundException("Liste  niveau 3 pays vide");
        }

        niveau3PaysList = niveau3PaysList
                .stream().sorted((d1, d2) -> d2.getNomN3().compareTo(d1.getNomN3()))
                .collect(Collectors.toList());
        return niveau3PaysList;
    }

      //Liste Niveau3Pays par pays
    public List<Niveau3Pays> getAllNiveau3PaysByIdNiveau2Pays(String id){
        List<Niveau3Pays>  niveau3PaysList = niveau3PaysRepository.findByNiveau2PaysIdNiveau2Pays(id);

        if(niveau3PaysList.isEmpty()){
            throw new EntityNotFoundException("Aucun niveau 3 pays trouvé");
        }
        niveau3PaysList = niveau3PaysList
                .stream().sorted((d1, d2) -> d2.getNomN3().compareTo(d1.getNomN3()))
                .collect(Collectors.toList());
        return niveau3PaysList;
    } 

           //Activer un niveau 3 pays
        public Niveau3Pays active(String id) throws Exception{
        Niveau3Pays n3 = niveau3PaysRepository.findByIdNiveau3Pays(id);
        if(n3 == null){
            throw new IllegalStateException("Niveau 3 pays non existant avec l'id" + id );
        }

        try {
          n3.setStatutN3(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation  du niveau 3 pays : " + e.getMessage());
        }
        return niveau3PaysRepository.save(n3);
    }

    //Desactiver niveau 3 pays
    public Niveau3Pays desactive(String id) throws Exception{
        Niveau3Pays n3 = niveau3PaysRepository.findByIdNiveau3Pays(id);
        if(n3 == null){
            throw new IllegalStateException("Niveau 3 pays non existant avec l'id" + id );
        }

        try {
        n3.setStatutN3(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de desactivation  du niveau 3 pays: " + e.getMessage());
        }
        return niveau3PaysRepository.save(n3);
    }

    //  Supprimer niveau 3 pays
      public String deleteByIdNiveau3Pays(String id){
        Niveau3Pays niveau3Pays = niveau3PaysRepository.findByIdNiveau3Pays(id);
        if(niveau3Pays == null){
            throw new EntityNotFoundException("Désolé le niveau 3 pays à supprimer n'existe pas");
        }
        niveau3PaysRepository.delete(niveau3Pays);
        return "Niveau 3 Pays supprimé avec succèss";
    }


    
}
