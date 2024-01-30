package projet.ais.services;

import java.util.List;
import java.time.LocalDate;
import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Random;
import java.time.LocalDate;
import java.util.Date;
import java.text.SimpleDateFormat;

import java.time.format.DateTimeFormatter;

import jakarta.persistence.EntityNotFoundException;

import java.util.stream.Collectors;
import java.util.NoSuchElementException;

import projet.ais.IdGenerator;
import projet.ais.Exception.NoContentException;
import projet.ais.models.Acteur;
import projet.ais.models.Alerte;
import projet.ais.models.Filiere;
import projet.ais.models.TypeActeur;
import projet.ais.repository.TypeActeurRepository;

@Service
public class TypeActeurService {

    @Autowired
    private TypeActeurRepository typeActeurRepository;
    @Autowired
    IdGenerator idGenerator ;
   
  
    //  Ajouter type acteur 
    public ResponseEntity<String> createTypeActeur(TypeActeur typeActeur) {

        // Générer un numéro aléatoire
        String codeTypeActeur = genererCode();
        String idCode = idGenerator.genererCode();
        // typeActeur.setIdType(idCode);
        // Attribuer le numéro aléatoire au type d'acteur
        typeActeur.setCodeTypeActeur(codeTypeActeur);
    
        // Vérifier si le type d'acteur existe déjà
        TypeActeur typeActeurExistant = typeActeurRepository.findByLibelle(typeActeur.getLibelle());
        if (typeActeurExistant != null) {
    
            // Retourner un message d'erreur
            return new ResponseEntity<>("Type Acteur déjà existant.", HttpStatus.BAD_REQUEST);
        } else {
            typeActeurRepository.save(typeActeur);
            return new ResponseEntity<>("Type Acteur créé avec succès", HttpStatus.CREATED);
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



    //Modifier type acteur methode
   

     public TypeActeur updateTypeActeur(TypeActeur typeActeur, Integer id){

     TypeActeur typeActeurExistant= typeActeurRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("type d'acteur introuvable avec id :" +id));
    typeActeurExistant.setLibelle(typeActeur.getLibelle());
    typeActeurExistant.setDescriptionTypeActeur(typeActeur.getDescriptionTypeActeur());

    return typeActeurRepository.save(typeActeurExistant);
  }

        //Recuperer la liste des type acteur
     public List<TypeActeur> getAllTypeActeur() throws Exception{

        List<TypeActeur> typeActeurList = typeActeurRepository.findAll();
        if(typeActeurList.isEmpty()){
            throw new EntityNotFoundException("Liste type d'acteur vide");
        }

        typeActeurList = typeActeurList
                .stream().sorted((d1, d2) -> d2.getDescriptionTypeActeur().compareTo(d1.getDescriptionTypeActeur()))
                .collect(Collectors.toList());
        return typeActeurList;
    }

    


    //  Supprimer type acteur
      public String deleteByIdTypeActeur(Integer id){
        TypeActeur typeActeur = typeActeurRepository.findByIdTypeActeur(id);
        if(typeActeur == null){
            throw new EntityNotFoundException("Désolé le type d'acteur à supprimer n'existe pas");
        }
        typeActeurRepository.delete(typeActeur);
        return "Type Acteur supprimé avec succèss";
    }
   

}
