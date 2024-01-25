package projet.ais.services;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.models.Niveau1Pays;
import projet.ais.models.Niveau2Pays;
import projet.ais.repository.Niveau2PaysRepository;

@Service
public class Niveau2PaysService {

    @Autowired
    private Niveau2PaysRepository niveau2PaysRepository;


    
     //  Ajouter niveau 2 pays 
    public ResponseEntity<String> createNiveau2Pays(Niveau2Pays niveau2Pays) {

        // Générer un numéro aléatoire
        String codeN2 = genererCode();
    
        // Attribuer le numéro aléatoire au niveau1
        niveau2Pays.setCodeN2(codeN2);
    
        // Vérifier si le niveau2Pays existe déjà
        Niveau2Pays niveau2PaysExistant = niveau2PaysRepository.findByNomN2(niveau2Pays.getNomN2());
        if (niveau2PaysExistant != null) {
    
            // Retourner un message d'erreur
            return new ResponseEntity<>("Niveau2Pays déjà existant.", HttpStatus.BAD_REQUEST);
        } else {
            niveau2PaysRepository.save(niveau2Pays);
            return new ResponseEntity<>("Niveau2Pays ajouté avec succès", HttpStatus.CREATED);
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



    //Modifier niveau2Pays methode
   

     public Niveau2Pays updateNiveau2Pays(Niveau2Pays niveau2Pays, Integer id){

     Niveau2Pays niveau2PaysExistant= niveau2PaysRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Niveau1Pays introuvable"));
     niveau2PaysExistant.setNomN2(niveau2Pays.getNomN2());
    niveau2PaysExistant.setDescriptionN2(niveau2Pays.getDescriptionN2());
    niveau2PaysExistant.setStatutN2(niveau2Pays.getStatutN2());
    niveau2PaysExistant.setNiveau1Pays(niveau2Pays.getNiveau1Pays());

    return niveau2PaysRepository.save(niveau2PaysExistant);
  }

        //Recuperer la liste des niveau2Pays
     public List<Niveau2Pays> getAllNiveau2Pays() throws Exception{

        List<Niveau2Pays> niveau2PaysList = niveau2PaysRepository.findAll();
        if(niveau2PaysList.isEmpty()){
            throw new EntityNotFoundException("Liste  niveau 2 pays vide");
        }

        niveau2PaysList = niveau2PaysList
                .stream().sorted((d1, d2) -> d2.getNomN2().compareTo(d1.getNomN2()))
                .collect(Collectors.toList());
        return niveau2PaysList;
    }

      //Liste Niveau2Pays par pays
    public List<Niveau2Pays> getAllNiveau2PaysByIdNiveau1Pays(Integer id){
        List<Niveau2Pays>  niveau2PaysList = niveau2PaysRepository.findByNiveau1PaysIdNiveau1Pays(id);

        if(niveau2PaysList.isEmpty()){
            throw new EntityNotFoundException("Aucun niveau 2 pays trouvé");
        }
        niveau2PaysList = niveau2PaysList
                .stream().sorted((d1, d2) -> d2.getNomN2().compareTo(d1.getNomN2()))
                .collect(Collectors.toList());
        return niveau2PaysList;
    } 


    //  Supprimer niveau 2 pays
      public String deleteByIdNiveau2Pays(Integer id){
        Niveau2Pays niveau2Pays = niveau2PaysRepository.findByIdNiveau2Pays(id);
        if(niveau2Pays == null){
            throw new EntityNotFoundException("Désolé le niveau 2 pays à supprimer n'existe pas");
        }
        niveau2PaysRepository.delete(niveau2Pays);
        return "Niveau 2 Pays supprimé avec succèss";
    }


}
