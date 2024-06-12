package projet.ais.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Acteur;
import projet.ais.models.Forme;
import projet.ais.models.Magasin;
import projet.ais.models.Materiel;
import projet.ais.repository.ActeurRepository;
import projet.ais.repository.MaterielRepository;
import java.time.format.DateTimeFormatter;


@Service
public class MaterielService {
    
    @Autowired
    MaterielRepository materielRepository;
    @Autowired
    IdGenerator idGenerator;
    @Autowired
    ActeurRepository acteurRepository;
    @Autowired
    CodeGenerator codeGenerator;
    @Autowired
    MessageService messageService;
    @Autowired
    FileUploade fileUploade;
    

    public Materiel createMateriel(Materiel materiel, MultipartFile imageFile) throws Exception{
        Acteur acteur = acteurRepository.findByIdActeur(materiel.getActeur().getIdActeur());
        
        if(acteur == null)
            throw new EntityNotFoundException("Acteur non disponible");
        
        if (imageFile != null) {
            String imageLocation = "/ais"; 
            try {
                    Path imageRootLocation = Paths.get(imageLocation);
                    if (!Files.exists(imageRootLocation)) {
                        Files.createDirectories(imageRootLocation);
                    }
    
                    String imageName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                    Path imagePath = imageRootLocation.resolve(imageName);
                    Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                    String onlineImagePath =fileUploade.uploadImageToFTP(imagePath, imageName);

                    materiel.setPhotoMateriel(imageName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }
            
        String codes  = codeGenerator.genererCode();
        String idCode = idGenerator.genererCode();

        materiel.setCodeMateriel(codes);
        materiel.setIdMateriel(idCode);
        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);
        materiel.setDateAjout(formattedDateTime);
        Materiel saveMateriel = materielRepository.save(materiel);
        // sendMessageToAllActeur(saveMateriel);
        return saveMateriel;
    }

    public ResponseEntity<String> sendMessageToAllActeur(Materiel materiel) {
        List<Acteur> allActeurs = acteurRepository.findAll();
       

        // TypeActeur transporteur = typeActeurRepository.findByLibelle("Transporteur");
        // TypeActeur fournisseur = typeActeurRepository.findByLibelle("Fournisseur");
        for (Acteur acteur : allActeurs) {
            // Acteur admins = acteurRepository.findByTypeActeurLibelle("admin");
            
            // if (acteur != admins) {}
            
            // Envoyer le message uniquement aux autres acteurs, pas à celui qui a ajouté le stock et pas aux transporteurs
            String mes = "Bonjour " + acteur.getNomActeur().toUpperCase() + " Un nouveau materiel de type location vient d'être ajouté " + " Nom : " + materiel.getNom();
                try {
                    messageService.sendMessageAndSave(acteur.getWhatsAppActeur(), mes,  acteur);
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());
                }
            
        
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


     public Page<Materiel> getAllMaterielPageable(Pageable pageable) {
        return materielRepository.findAllByStatutAndActeurStatutActeur(true,true,pageable);
    }

    // recuperer les materiels par  type materiel avec pagination
    public Page<Materiel> getMaterielByTypeMaterielWithPagination(String idTypeMateriel,Pageable pageable) {
        return materielRepository.findByTypeMateriel_IdTypeMaterielAndStatutAndActeurStatutActeur(idTypeMateriel, true, true, pageable);
    }

    // recuperer les magasins par  acteur avec pagination
    public Page<Materiel> getMaterielByActeurWithPagination(String idActeur,Pageable pageable) {
        return materielRepository.findByActeur_IdActeur(idActeur, pageable);
    }


    
    public Materiel updateMateriel(Materiel materiel, String id, MultipartFile imageFile) throws Exception{
        Materiel mat = materielRepository.findById(id).orElseThrow();

        mat.setDescription(materiel.getDescription());
        mat.setEtatMateriel(materiel.getEtatMateriel());
        mat.setLocalisation(materiel.getLocalisation());
        mat.setNom(materiel.getNom());
        mat.setPrixParHeure(materiel.getPrixParHeure());
        mat.setPersonneModif(materiel.getPersonneModif());

        if(materiel.getMonnaie() != null){
            mat.setMonnaie(materiel.getMonnaie());
        }
        
        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);
        mat.setDateModif(formattedDateTime);
        
        if (imageFile != null) {
            String imageLocation = "/ais"; 
            try {
                    Path imageRootLocation = Paths.get(imageLocation);
                    if (!Files.exists(imageRootLocation)) {
                        Files.createDirectories(imageRootLocation);
                    }
    
                    String imageName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                    Path imagePath = imageRootLocation.resolve(imageName);
                    Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                    String onlineImagePath =fileUploade.uploadImageToFTP(imagePath, imageName);

                    mat.setPhotoMateriel(imageName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }
            
        return materielRepository.save(mat);
    }

    public List<Materiel> getMateriels(){
        List<Materiel> materielList = materielRepository.findAll();

        if(materielList == null)
            throw new EntityNotFoundException("Aucune matériel trouvé");

        materielList = materielList
        .stream().sorted((m1,m2) -> m2.getNom().compareTo(m1.getNom()))
        .collect(Collectors.toList());
        return materielList;
    }

    public List<Materiel> getMaterielByActeur(String id){
        List<Materiel> materielList = materielRepository.findByActeurIdActeur(id);

        if(materielList.isEmpty())
            throw new EntityNotFoundException("Aucune matériel trouvé");

        materielList = materielList
        .stream().sorted((m1,m2) -> m2.getNom().compareTo(m1.getNom()))
        .collect(Collectors.toList());
        return materielList;
    }

    public List<Materiel> getMaterielByTypeMateriel(String id){
        List<Materiel> materielList = materielRepository.findAllByTypeMaterielIdTypeMateriel(id);

        if(materielList.isEmpty())
            throw new EntityNotFoundException("Aucune matériel trouvé");

        materielList = materielList
        .stream().sorted((m1,m2) -> m2.getNom().compareTo(m1.getNom()))
        .collect(Collectors.toList());
        return materielList;
    }

    public String deleteMateriel(String id){
        Materiel materiel = materielRepository.findById(id).orElseThrow(null);

        materielRepository.delete(materiel);

        return "Supprimé avec succèss";
    }

    public Materiel active(String id) throws Exception{
        Materiel mat = materielRepository.findById(id).orElseThrow(null);

       try {
        mat.setStatut(true);
       } catch (Exception e) {
        throw new Exception("Erreur lors de l'activation : " + e.getMessage());

       }
       return materielRepository.save(mat);
    }

    public Materiel desactive(String id) throws Exception{
        Materiel mat = materielRepository.findById(id).orElseThrow(null);

       try {
        mat.setStatut(false);
       } catch (Exception e) {
        throw new Exception("Erreur lors de la desactivation : " + e.getMessage());

       }
       return materielRepository.save(mat);
    }
}