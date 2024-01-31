package projet.ais.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Random;
import java.util.Optional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.IdGenerator;
import projet.ais.models.Continent;
import projet.ais.models.ParametreGeneraux;
import projet.ais.repository.ParametreGenerauxRepository;

@Service
public class ParametreGenerauxService {

    @Autowired
    private ParametreGenerauxRepository parametreGenerauxRepository;
        @Autowired
    IdGenerator idGenerator ;
    

     //Ajouter parametreGeneral 
        public ResponseEntity<String> createParametreGeneral(ParametreGeneraux parametreGeneraux,  MultipartFile imageFile1) throws Exception {
        
            String codeNiveauStructure = genererCode();
            String code = idGenerator.genererCode();

            parametreGeneraux.setCodeNiveauStructure(codeNiveauStructure);
            parametreGeneraux.setIdParametreGeneraux(code);

            ParametreGeneraux existantParamatreGeneraux = parametreGenerauxRepository.findByNomStructure(parametreGeneraux.getNomStructure());
            if (existantParamatreGeneraux == null) {
               // Vérifier si le paramètre general existe déjà
                  if (imageFile1 != null) {
                String imageLocation = "C:\\xampp\\htdocs\\ais";
                try {
                    Path imageRootLocation = Paths.get(imageLocation);
                    if (!Files.exists(imageRootLocation)) {
                        Files.createDirectories(imageRootLocation);
                    }
    
                    String imageName = UUID.randomUUID().toString() + "_" + imageFile1.getOriginalFilename();
                    Path imagePath = imageRootLocation.resolve(imageName);
                    Files.copy(imageFile1.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                    parametreGeneraux.setLogoSysteme(imageLocation);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }

                parametreGenerauxRepository.save(parametreGeneraux);
                return new ResponseEntity<>("Paramètre ajouté avec succès", HttpStatus.OK);
            } else {
                
                // Retourner un message d'erreur
                return new ResponseEntity<>("Paramètre déjà existant.", HttpStatus.BAD_REQUEST);
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
    
    
        //Modifier paramètre methode
       
    
         public ParametreGeneraux updateParametreGeneraux(ParametreGeneraux parametreGeneraux, String id, MultipartFile imageFile1) throws Exception{
    
 

         ParametreGeneraux parametreGenerauxExistant = parametreGenerauxRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Paramètre général introuvable "));
        
         if (imageFile1 != null) {
            String imageLocation = "C:\\xampp\\htdocs\\ais";
            try {
                Path imageRootLocation = Paths.get(imageLocation);
                if (!Files.exists(imageRootLocation)) {
                    Files.createDirectories(imageRootLocation);
                }

                String imageName = UUID.randomUUID().toString() + "_" + imageFile1.getOriginalFilename();
                Path imagePath = imageRootLocation.resolve(imageName);
                Files.copy(imageFile1.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                parametreGenerauxExistant.setLogoSysteme(imageLocation);
            } catch (IOException e) {
                throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
            }
        }

         parametreGenerauxExistant.setSigleStructure(parametreGeneraux.getSigleStructure());
         parametreGenerauxExistant.setNomStructure(parametreGeneraux.getNomStructure());
         parametreGenerauxExistant.setSigleSysteme(parametreGeneraux.getSigleStructure());
         parametreGenerauxExistant.setNomSysteme(parametreGeneraux.getNomSysteme());
         parametreGenerauxExistant.setSloganSysteme(parametreGeneraux.getSloganSysteme());
         parametreGenerauxExistant.setAdresseStructure(parametreGeneraux.getAdresseStructure());
         parametreGenerauxExistant.setEmailStructure(parametreGeneraux.getEmailStructure());
         parametreGenerauxExistant.setTelephoneStructure(parametreGeneraux.getTelephoneStructure());
         parametreGenerauxExistant.setWhattsAppStructure(parametreGeneraux.getWhattsAppStructure());
         parametreGenerauxExistant.setLibelleNiveau1Pays(parametreGeneraux.getLibelleNiveau1Pays());
         parametreGenerauxExistant.setLibelleNiveau2Pays(parametreGeneraux.getLibelleNiveau2Pays());
         parametreGenerauxExistant.setLibelleNiveau3Pays(parametreGeneraux.getLibelleNiveau3Pays());
         parametreGenerauxExistant.setLocaliteStructure(parametreGeneraux.getLocaliteStructure());


        return parametreGenerauxRepository.save(parametreGenerauxExistant);
      }
    
            //Recuperer la liste des paramètre généraux
         public List<ParametreGeneraux> getAllParametreGeneraux() throws Exception{
    
            List<ParametreGeneraux> parametreGenerauxList = parametreGenerauxRepository.findAll();
            if(parametreGenerauxList.isEmpty()){
                throw new EntityNotFoundException("Paramètre Généraux vide");
            }
    
            parametreGenerauxList = parametreGenerauxList
                    .stream().sorted((d1, d2) -> d2.getNomSysteme().compareTo(d1.getNomSysteme()))
                    .collect(Collectors.toList());
            return parametreGenerauxList;
        }
    

        //Get paramètre général by ID
    public ResponseEntity<?> findById(String id) {

    Optional<ParametreGeneraux> parametreGeneraux = parametreGenerauxRepository.findById(id);

    if (parametreGeneraux.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Le paramètre  avec l'ID " + id + " n'existe pas");
    }

    return ResponseEntity.ok(parametreGeneraux.get());     
   }
        
    
    
        //  Supprimer parametre généraux
          public String deleteByIdParametreGeneraux(String id){
            ParametreGeneraux parametreGeneraux = parametreGenerauxRepository.findByIdParametreGeneraux(id);
            if(parametreGeneraux == null){
                throw new EntityNotFoundException("Désolé le paramètre général à supprimer n'existe pas");
            }
            parametreGenerauxRepository.delete(parametreGeneraux);
            return "Paramètre général supprimé avec succèss";
        }
       
    
}
