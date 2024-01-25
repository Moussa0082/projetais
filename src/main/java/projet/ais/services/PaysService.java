package projet.ais.services;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.models.Niveau2Pays;
import projet.ais.models.Pays;
import projet.ais.repository.PaysRepository;

@Service
public class PaysService {

    @Autowired
    private PaysRepository paysRepository;


     //  Ajouter pays 
    public ResponseEntity<String> createPays(Pays pays) {
        
        Pays paysExistant = paysRepository.findByNomPays(pays.getNomPays());
        if (paysExistant == null) {
            // Générer un numéro aléatoire
            String codePays = genererCode();
            // Attribuer le numéro aléatoire au type d'acteur
                pays.setCodePays(codePays);
            // Vérifier si le pays existe déjà
            paysRepository.save(pays);
            return new ResponseEntity<>("Pays ajouté avec succès", HttpStatus.OK);
        } else {
            
            // Retourner un message d'erreur
            return new ResponseEntity<>("Pays déjà existant.", HttpStatus.BAD_REQUEST);
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


    //Liste Pays  par sous region
    public List<Pays> getAllPaysBySousRegion(Integer id){
        List<Pays>  paysList = paysRepository.findBySousRegionIdSousRegion(id);

        if(paysList.isEmpty()){
            throw new EntityNotFoundException("Aucun pays trouvé");
        }
        paysList = paysList
                .stream().sorted((d1, d2) -> d2.getSousRegion().getNomSousRegion().compareTo(d1.getSousRegion().getNomSousRegion()))
                .collect(Collectors.toList());
        return paysList;
    }

    //Modifier pays methode
   

     public Pays updatePays(Pays pays, Integer id){

     Pays paysExistant = paysRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("type d'acteur introuvable avec id :" +id));
    paysExistant.setNomPays(pays.getNomPays());
    paysExistant.setDescriptionPays(pays.getDescriptionPays());
    paysExistant.setStatutPays(pays.getStatutPays());
    paysExistant.setSousRegion(pays.getSousRegion());

    return paysRepository.save(paysExistant);
  }

        //Recuperer la liste des type acteur
     public List<Pays> getAllPays() throws Exception{

        List<Pays> paysList = paysRepository.findAll();
        if(paysList.isEmpty()){
            throw new EntityNotFoundException("Liste pays vide");
        }

        paysList = paysList
                .stream().sorted((d1, d2) -> d2.getNomPays().compareTo(d1.getNomPays()))
                .collect(Collectors.toList());
        return paysList;
    }

    


    //  Supprimer pays
      public String deleteByIdPays(Integer id){
        Pays pays = paysRepository.findByIdPays(id);
        if(pays == null){
            throw new EntityNotFoundException("Désolé le pays à supprimer n'existe pas");
        }
        paysRepository.delete(pays);
        return "Pays supprimé avec succèss";
    }
   
    
}
