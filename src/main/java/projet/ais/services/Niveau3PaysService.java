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
import projet.ais.models.Niveau2Pays;
import projet.ais.models.Niveau3Pays;
import projet.ais.repository.Niveau3PaysRepository;

@Service
public class Niveau3PaysService {

    @Autowired
    private Niveau3PaysRepository niveau3PaysRepository;
    @Autowired
    IdGenerator idGenerator ;

    //  Ajouter niveau 3 pays 
    public ResponseEntity<String> createNiveau3Pays(Niveau3Pays niveau3Pays) {

        // Générer un numéro aléatoire
        String codeN3 = genererCode();
        String code = idGenerator.genererCode();
    
        // Attribuer le numéro aléatoire au niveau1
        niveau3Pays.setCodeN3(codeN3);
        niveau3Pays.setIdNiveau3Pays(code);
    
        // Vérifier si le niveau3Pays existe déjà
        Niveau3Pays niveau3PaysExistant = niveau3PaysRepository.findByNomN3(niveau3Pays.getNomN3());
        if (niveau3PaysExistant != null) {
    
            // Retourner un message d'erreur
            return new ResponseEntity<>("Niveau 3 Pays déjà existant.", HttpStatus.BAD_REQUEST);
        } else {
            niveau3PaysRepository.save(niveau3Pays);
            return new ResponseEntity<>("Niveau 3 Pays ajouté avec succès", HttpStatus.CREATED);
        }
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
    niveau3PaysExistant.setStatutN3(niveau3Pays.getStatutN3());
    niveau3PaysExistant.setNiveau2Pays(niveau3Pays.getNiveau2Pays());

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
