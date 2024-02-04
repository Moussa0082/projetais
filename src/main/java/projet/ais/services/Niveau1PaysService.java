package projet.ais.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.IdGenerator;
import projet.ais.models.Acteur;
import projet.ais.models.Magasin;
import projet.ais.models.Niveau1Pays;
import projet.ais.models.TypeActeur;
import projet.ais.repository.Niveau1PaysRepository;

@Service
public class Niveau1PaysService {

    @Autowired
    private Niveau1PaysRepository niveau1PaysRepository;
        @Autowired
    IdGenerator idGenerator ;

    //  Ajouter type acteur 
    public ResponseEntity<String> createNiveau1Pays(Niveau1Pays niveau1Pays) {

        // Générer un numéro aléatoire
        String codeN1 = genererCode();
        String code = idGenerator.genererCode();
    
        // Attribuer le numéro aléatoire au niveau1
        niveau1Pays.setCodeN1(codeN1);
        niveau1Pays.setIdNiveau1Pays(code);
    
        // Vérifier si le niveau1Pays existe déjà
        Niveau1Pays niveau1PaysExistant = niveau1PaysRepository.findByNomN1(niveau1Pays.getNomN1());
        if (niveau1PaysExistant != null) {
    
            // Retourner un message d'erreur
            return new ResponseEntity<>("Niveau 1 Pays déjà existant.", HttpStatus.BAD_REQUEST);
        } else {
            niveau1PaysRepository.save(niveau1Pays);
            return new ResponseEntity<>("Niveau 1 Pays ajouté avec succès", HttpStatus.CREATED);
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



    //Modifier niveau1Pays methode
   

     public Niveau1Pays updateNiveau1Pays(Niveau1Pays niveau1Pays, String id){

     Niveau1Pays niveau1PaysExistant= niveau1PaysRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Niveau1Pays introuvable"));
     niveau1PaysExistant.setNomN1(niveau1Pays.getNomN1());
    niveau1PaysExistant.setDescriptionN1(niveau1Pays.getDescriptionN1());
    niveau1PaysExistant.setPays(niveau1Pays.getPays());
    niveau1PaysExistant.setDateModif(LocalDateTime.now());

    return niveau1PaysRepository.save(niveau1PaysExistant);
  }

        //Recuperer la liste des niveau1Pays
     public List<Niveau1Pays> getAllNiveau1Pays() throws Exception{

        List<Niveau1Pays> niveau1PaysList = niveau1PaysRepository.findAll();
        if(niveau1PaysList.isEmpty()){
            throw new EntityNotFoundException("Liste niveau 1 pays vide");
        }

        niveau1PaysList = niveau1PaysList
                .stream().sorted((d1, d2) -> d2.getNomN1().compareTo(d1.getNomN1()))
                .collect(Collectors.toList());
        return niveau1PaysList;
    }

      //Liste Niveau1Pays par pays
    public List<Niveau1Pays> getAllNiveau1PaysByPays(String id){
        List<Niveau1Pays>  niveau1PaysList = niveau1PaysRepository.findByPaysIdPays(id);

        if(niveau1PaysList.isEmpty()){
            throw new EntityNotFoundException("Aucun niveau 1 pays trouvé");
        }
        niveau1PaysList = niveau1PaysList
                .stream().sorted((d1, d2) -> d2.getNomN1().compareTo(d1.getNomN1()))
                .collect(Collectors.toList());
        return niveau1PaysList;
    } 

        //Activer un niveau 1 pays
        public Niveau1Pays active(String id) throws Exception{
        Niveau1Pays n1 = niveau1PaysRepository.findByIdNiveau1Pays(id);
        if(n1 == null){
            throw new IllegalStateException("Niveau 1 pays non existant avec l'id" + id );
        }

        try {
          n1.setStatutN1(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation  du niveau 1 pays : " + e.getMessage());
        }
        return niveau1PaysRepository.save(n1);
    }

    //Desactiver niveau 1 pays
    public Niveau1Pays desactive(String id) throws Exception{
        Niveau1Pays n1 = niveau1PaysRepository.findByIdNiveau1Pays(id);
        if(n1 == null){
            throw new IllegalStateException("Niveau 1 pays non existant avec l'id" + id );
        }

        try {
        n1.setStatutN1(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de desactivation  du niveau 1 pays: " + e.getMessage());
        }
        return niveau1PaysRepository.save(n1);
    }


    //  Supprimer niveau 1 pays
      public String deleteByIdNiveau1Pays(String id){
        Niveau1Pays niveau1Pays = niveau1PaysRepository.findByIdNiveau1Pays(id);
        if(niveau1Pays == null){
            throw new EntityNotFoundException("Désolé le niveau 1 pays à supprimer n'existe pas");
        }
        niveau1PaysRepository.delete(niveau1Pays);
        return "Niveau 1 Pays supprimé avec succèss";
    }
   
    
}
