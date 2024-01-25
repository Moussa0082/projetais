package projet.ais.services;

import java.util.Random;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.models.Pays;
import projet.ais.models.SousRegion;
import projet.ais.repository.SousRegionRepository;

@Service
public class SousRegionService {
    

    @Autowired
    private SousRegionRepository sousRegionRepository;


      //  Ajouter sous region 
    public ResponseEntity<String> createSousRegion(SousRegion sousRegion) {
        
        SousRegion sousRegionExistant = sousRegionRepository.findByNomSousRegion(sousRegion.getNomSousRegion());
        if (sousRegionExistant == null) {
            // Générer un numéro aléatoire
            String codeSousRegion = genererCode();
            // Attribuer le numéro aléatoire au type d'acteur
                sousRegion.setCodeSousRegion(codeSousRegion);
            // Vérifier si la sous region existe déjà
            sousRegionRepository.save(sousRegion);
            return new ResponseEntity<>("Sous region ajouté avec succès", HttpStatus.OK);
        } else {
            
            // Retourner un message d'erreur
            return new ResponseEntity<>("Sous region déjà existant.", HttpStatus.BAD_REQUEST);
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



    //Modifier sous region methode
   

     public SousRegion updateSousRegion(SousRegion sousRegion, Integer id){

     SousRegion sousRegionExistant = sousRegionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Sous Region introuvable "));
     sousRegionExistant.setNomSousRegion(sousRegion.getNomSousRegion());
     sousRegionExistant.setStatutSousRegion(sousRegion.getStatutSousRegion());
     sousRegionExistant.setContinent(sousRegion.getContinent());
  

    return sousRegionRepository.save(sousRegionExistant);
  }

        //Recuperer la liste des sous region
     public List<SousRegion> getAllSousRegion() throws Exception{

        List<SousRegion> sousRegionList = sousRegionRepository.findAll();
        if(sousRegionList.isEmpty()){
            throw new EntityNotFoundException("Liste sous region vide");
        }

        sousRegionList = sousRegionList
                .stream().sorted((d1, d2) -> d2.getNomSousRegion().compareTo(d1.getNomSousRegion()))
                .collect(Collectors.toList());
        return sousRegionList;
    }

    


    //  Supprimer sous region
      public String deleteByIdSousRegion(Integer id){
        SousRegion sousRegion = sousRegionRepository.findByIdSousRegion(id);
        if(sousRegion == null){
            throw new EntityNotFoundException("Désolé la sous region à supprimer n'existe pas");
        }
        sousRegionRepository.delete(sousRegion);
        return "Sous region supprimé avec succèss";
    }
   

}
