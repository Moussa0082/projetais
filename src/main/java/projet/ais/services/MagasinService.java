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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Acteur;
import projet.ais.models.CategorieProduit;
import projet.ais.models.Filiere;
import projet.ais.models.Forme;
import projet.ais.models.Intrant;
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
    @Autowired
    FileUploade fileUploade;

    public Magasin createMagasin(Magasin magasin, MultipartFile imageFile) throws Exception{
        Acteur acteur = acteurRepository.findByIdActeur(magasin.getActeur().getIdActeur());

        if(acteur == null)
            throw new IllegalStateException("Aucun acteur disponible");
        
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

                    magasin.setPhoto(imageName);
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

                    magasin.setPhoto(imageName);
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





    public Page<Magasin> getAllMagasinPageable(Pageable pageable) {
        return magasinRepository.findAllByStatutMagasin(true,pageable);
    }

     @Transactional
    public Page<Magasin> getAllMagasinPageableByPays( String niveau3PaysActeur, Pageable pageable) {
        // Fetch magasin from the specified country
        Page<Magasin> magasinsByPays = magasinRepository.findAllByStatutMagasinAndPaysAndActeurStatutActeurTrue(true,
            niveau3PaysActeur.trim().toLowerCase(),  pageable);

        List<Magasin> magasinList = new ArrayList<>(magasinsByPays.getContent());

        // If no magasin are found for the specified country, fetch magasin from other countries
        if (magasinList.isEmpty()) {
            Page<Magasin> magasinsFromOtherCountries = magasinRepository.findAllByStatutMagasinTrueAndActeurStatutActeurTrue(
                  pageable);

            return new PageImpl<>(magasinsFromOtherCountries.getContent(), pageable, magasinsFromOtherCountries.getTotalElements());
        }

        // Fetch magasins from other countries if needed to fill the page
        if (magasinList.size() < pageable.getPageSize()) {
            Pageable complementPageable = PageRequest.of(0, pageable.getPageSize() - magasinList.size());
            Page<Magasin> magasinsComplement = magasinRepository.findAllByStatutMagasinAndPaysNotAndActeurStatutActeurTrue(
                true, niveau3PaysActeur.trim().toLowerCase(), true,  complementPageable);
            magasinList.addAll(magasinsComplement.getContent());
        }

        return new PageImpl<>(magasinList, pageable, magasinsByPays.getTotalElements() + magasinList.size());
    }



    @Transactional
    public Page<Magasin> getMagasinByNiveau1PaysWithPagination(String niveau3PaysActeur, String idNiveau1Pays, Pageable pageable) {
        // Fetch magasins from the specified country and niveau1Pays
        Page<Magasin> magasinsByPaysAndNiveau1 = magasinRepository.findByNiveau1Pays_IdNiveau1PaysAndPaysAndStatutMagasinAndActeurStatutActeurTrue(idNiveau1Pays, niveau3PaysActeur, true, pageable);

        List<Magasin> magasinList = new ArrayList<>(magasinsByPaysAndNiveau1.getContent());

        // If no magasins are found for the specified country and niveau1Pays, fetch magasins from other countries
        if (magasinList.isEmpty()) {
            Page<Magasin> magasinsFromOtherCountries = magasinRepository.findAllByStatutMagasinTrueAndActeurStatutActeurTrue(pageable);

            return new PageImpl<>(magasinsFromOtherCountries.getContent(), pageable, magasinsFromOtherCountries.getTotalElements());
        }

        // Fetch magasins from other countries if needed to fill the page
        if (magasinList.size() < pageable.getPageSize()) {
            Pageable complementPageable = PageRequest.of(0, pageable.getPageSize() - magasinList.size());
            Page<Magasin> magasinsComplement = magasinRepository.findByNiveau1Pays_IdNiveau1PaysAndPaysNotAndStatutMagasinTrueAndActeurStatutActeurTrue(
                 niveau3PaysActeur.trim().toLowerCase(), idNiveau1Pays,  complementPageable);
            magasinList.addAll(magasinsComplement.getContent());
        }

        return new PageImpl<>(magasinList, pageable, magasinsByPaysAndNiveau1.getTotalElements() + magasinList.size());
    }


    @Transactional
    public void updatePaysForMagasins() {
        // Récupérer tous les magasin
        List<Magasin> magasins = magasinRepository.findAll();

        // Parcourir chaque magasin
        for (Magasin magasin : magasins) {
            // Récupérer l'acteur lié au intrant
            Acteur acteur = magasin.getActeur();

            if (acteur != null) {
                // Récupérer le niveau3Pays de l'acteur lié au stock
                String niveau3Pays = acteur.getNiveau3PaysActeur();

                // Mettre à jour la colonne pays du stock
                magasin.setPays(niveau3Pays);
            } else {
                // Gérer le cas où l'acteur est null
                System.out.println("L'acteur lié au magasin ID " + magasin.getIdMagasin() + " est null.");
            }
        }
    }




    //  // recuperer les magasins par  niveau1Pays avec pagination
    // public Page<Magasin> getMagasinByNiveau1PaysWithPagination(String idNiveau1Pays,Pageable pageable) {
    //     return magasinRepository.findByNiveau1Pays_IdNiveau1PaysAndStatutMagasin(idNiveau1Pays, true, pageable);
    // }

    // recuperer les magasins par  acteur avec pagination
    public Page<Magasin> getMagasinByActeurWithPagination(String idActeur,Pageable pageable) {
        return magasinRepository.findByActeur_IdActeur(idActeur, pageable);
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