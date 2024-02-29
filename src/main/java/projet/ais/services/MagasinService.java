package projet.ais.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Acteur;
import projet.ais.models.Filiere;
import projet.ais.models.Magasin;
import projet.ais.models.Stock;
import projet.ais.repository.ActeurRepository;
import projet.ais.repository.MagasinRepository;
import projet.ais.repository.StockRepository;

@Service
public class MagasinService {
    
    @Autowired
    MagasinRepository magasinRepository;
    @Autowired
    ActeurRepository acteurRepository;
    @Autowired
    StockRepository stockRepository;
    @Autowired
    CodeGenerator codeGenerator;
      @Autowired
    IdGenerator idGenerator ;

    public Magasin createMagasin(Magasin magasin, MultipartFile imageFile) throws Exception{
        Acteur acteur = acteurRepository.findByIdActeur(magasin.getActeur().getIdActeur());

        if(acteur == null)
            throw new IllegalStateException("Aucun acteur disponible");
        
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
                    magasin.setPhoto("ais/" + imageName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }
            String codes = codeGenerator.genererCode();
            String idcodes = idGenerator.genererCode();
             String pattern = "yyyy-MM-dd HH:mm";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    LocalDateTime now = LocalDateTime.now();
    String formattedDateTime = now.format(formatter);
    magasin.setDateAjout(formattedDateTime);
            magasin.setCodeMagasin(codes);
            magasin.setIdMagasin(idcodes);

            
        return magasinRepository.save(magasin);
    }

    public Magasin updateMagasin(Magasin magasin, MultipartFile imageFile, String id) throws Exception{
    
        // Stock stock = stockRepository.findByIdStock(magasin.getStock().getIdStock());
        Magasin mag= magasinRepository.findById(id).orElseThrow(null);

       
        mag.setContactMagasin(magasin.getContactMagasin());
        mag.setLatitude(magasin.getLatitude());
        mag.setLongitude(magasin.getLongitude());
        mag.setLocaliteMagasin(magasin.getLocaliteMagasin());
        mag.setNomMagasin(magasin.getNomMagasin()); 
        mag.setPersonneModif(magasin.getPersonneModif());
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
                    mag.setPhoto("ais/" + imageName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }

            String pattern = "yyyy-MM-dd HH:mm";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(formatter);
            mag.setDateModif(formattedDateTime);
        return magasinRepository.save(mag);
    }

    public List<Magasin> getMagasin() {
        List<Magasin> magasinList = magasinRepository.findAll();

        if(magasinList.isEmpty())
            throw new IllegalStateException("Aucun magasin trouvé");
        
        magasinList = magasinList.
        stream().sorted((m1,m2) -> m2.getNomMagasin().compareTo(m1.getNomMagasin()))
        .collect(Collectors.toList());

        return magasinList;
    }

    public List<Magasin> getMagasinByActeur(String id) {
        List<Magasin> magasinList = magasinRepository.findByActeurIdActeur(id);

        if(magasinList.isEmpty())
            throw new IllegalStateException("Aucun magasin trouvé");
        
        magasinList = magasinList.
        stream().sorted((m1,m2) -> m2.getNomMagasin().compareTo(m1.getNomMagasin()))
        .collect(Collectors.toList());

        return magasinList;
    }

    public List<Magasin> listeMagasinByNiveau1PaysAndActeur( String idActeur, String idNiveau1Pays) throws Exception {
        List<Magasin> magasinList = magasinRepository.findAllByActeurIdActeurAndNiveau1PaysIdNiveau1Pays(idActeur, idNiveau1Pays);

        if(magasinList.isEmpty())
            throw new Exception("Aucun magasin trouvé");

        
        magasinList = magasinList.
        stream().sorted((m1,m2) -> m2.getNomMagasin().compareTo(m1.getNomMagasin()))
        .collect(Collectors.toList());

        return magasinList;
    }

    public List<Magasin> getMagasinByNiveau1Pays(String id) {
        List<Magasin> magasinList = magasinRepository.findByNiveau1PaysIdNiveau1Pays(id);

        if(magasinList.isEmpty())
            throw new IllegalStateException("Aucun magasin trouvé");
        
        magasinList = magasinList.
        stream().sorted((m1,m2) -> m2.getNomMagasin().compareTo(m1.getNomMagasin()))
        .collect(Collectors.toList());

        return magasinList;
    }



    public String supprimerMagagin(String id){
        Magasin magasin = magasinRepository.findById(id).orElseThrow(null);

        magasinRepository.delete(magasin);

        return "supprimé avec success";
    }

    public Magasin active(String id) throws Exception{
        Magasin mag = magasinRepository.findById(id).orElseThrow(null);

        try {
          mag.setStatutMagasin(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation  de la magasin : " + e.getMessage());
        }
        return magasinRepository.save(mag);
    }

    public Magasin desactive(String id) throws Exception{
        Magasin mag = magasinRepository.findById(id).orElseThrow(null);

        try {
        mag.setStatutMagasin(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de desactivation : " + e.getMessage());
        }
        return magasinRepository.save(mag);
    }
}
