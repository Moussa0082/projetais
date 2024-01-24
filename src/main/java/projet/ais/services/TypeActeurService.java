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

import projet.ais.models.Acteur;
import projet.ais.models.Alerte;
import projet.ais.models.TypeActeur;
import projet.ais.repository.TypeActeurRepository;

@Service
public class TypeActeurService {

    @Autowired
    private TypeActeurRepository typeActeurRepository;

    public void genererCodeTypeActeur(){
        
        LocalDate localDate = LocalDate.now();
        int randomWithMathRandom = (int) ((Math.random() * (100 - 1)) + 1);

    }
  
    //  Ajouter type acteur 
      public ResponseEntity<String> createTypeActeur(TypeActeur typeActeur) {

    // Générer un numéro aléatoire
    String codeTypeActeur = genererCode();

    // Attribuer le numéro aléatoire au type d'acteur
    typeActeur.setCodeTypeActeur(codeTypeActeur);

    // Vérifier si le type d'acteur existe déjà
    if (typeActeurRepository.findByCodeTypeActeur(codeTypeActeur) == null) {

        // Enregistrer le type d'acteur
        typeActeurRepository.save(typeActeur);

        // Retourner un message de succès
        return new ResponseEntity<>("Type Acteur créé avec succès", HttpStatus.CREATED);
    } else {

        // Retourner un message d'erreur
        return new ResponseEntity<>("Type Acteur déjà existant.", HttpStatus.BAD_REQUEST);
    }
}

public String genererCode() {
    // Générer 2 lettres aléatoires
    String lettresAleatoires = genererLettresAleatoires(2);

    // Générer 3 chiffres aléatoires
    String chiffresAleatoires = genererChiffresAleatoires(3);

    // Obtenir la date actuelle
    String dateActuelle = getDateActuelle();

    // Concaténer les parties pour former le code final
    String codeFinal = lettresAleatoires + chiffresAleatoires + dateActuelle;

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

private String getDateActuelle() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    return dateFormat.format(new Date());
}

        //Modifier type acteur methode
    public TypeActeur updateTypeActeur(Integer id, TypeActeur typeActeur) {
        return typeActeurRepository.findById(id)
                .map(ta -> {
                    ta.setDescriptionTypeActeur(null);
                  return typeActeurRepository.save(ta);
                }).orElseThrow(() -> new RuntimeException(("Type Acteur non existant avec l'ID " + id)));
    
    }

        //Recuperer la liste des type acteur
     public List<TypeActeur> getAllActeur(){

        List<TypeActeur> typeActeurList = typeActeurRepository.findAll();

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
