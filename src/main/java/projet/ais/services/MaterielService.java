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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Acteur;
import projet.ais.models.CommandeMateriel;
import projet.ais.models.Materiel;
import projet.ais.repository.ActeurRepository;
import projet.ais.repository.CommandeMaterielRepository;
import projet.ais.repository.MaterielRepository;

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
    CommandeMaterielRepository commandeMaterielRepository;


    public Materiel createMateriel(Materiel materiel, MultipartFile imageFile) throws Exception{
        Acteur acteur = acteurRepository.findByIdActeur(materiel.getActeur().getIdActeur());
        
        if(acteur == null)
            throw new EntityNotFoundException("Acteur non disponible");
        
        if (imageFile != null) {
                String imageLocation = "C:\\xampp\\htdocs\\ais";
                try {
                    Path imageRootLocation = Paths.get(imageLocation);
                    if (!Files.exists(imageRootLocation)) {
                        Files.createDirectories(imageRootLocation);
                    }
    
                    String imageName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                    Path imagePath = imageRootLocation.resolve(imageName);
                    Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                    materiel.setPhotoMateriel("ais/" + imageName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }
        String codes  = codeGenerator.genererCode();
        String idCode = idGenerator.genererCode();

        materiel.setCodeMateriel(codes);
        materiel.setIdMateriel(idCode);
        materiel.setDateAjout(LocalDateTime.now());
        materiel.setDateModif(LocalDateTime.now());
        return materielRepository.save(materiel);
    }

    public Materiel updateMateriel(Materiel materiel, String id, MultipartFile imageFile) throws Exception{
        Materiel mat = materielRepository.findById(id).orElseThrow();

        mat.setDescription(materiel.getDescription());
        mat.setEtatMateriel(materiel.getEtatMateriel());
        mat.setLocalisation(materiel.getLocalisation());
        mat.setNom(materiel.getNom());
        mat.setPrix(materiel.getPrix());

        mat.setDateModif(LocalDateTime.now());
        
        if (imageFile != null) {
            String imageLocation = "C:\\xampp\\htdocs\\ais";
            try {
                Path imageRootLocation = Paths.get(imageLocation);
                if (!Files.exists(imageRootLocation)) {
                    Files.createDirectories(imageRootLocation);
                }

                String imageName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Path imagePath = imageRootLocation.resolve(imageName);
                Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                mat.setPhotoMateriel("ais/" + imageName);
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
        List<Materiel> materielList = materielRepository.findAll();

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
