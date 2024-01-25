package projet.ais.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import projet.ais.CodeGenerator;
import projet.ais.models.Acteur;
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

    public Magasin createMagasin(Magasin magasin, MultipartFile imageFile) throws Exception{
        Acteur acteur = acteurRepository.findByIdActeur(magasin.getActeur().getIdActeur());

        Stock stock = stockRepository.findByIdStock(magasin.getStock().getIdStock());

        if(stock == null)
            throw new IllegalStateException("Aucune stock disponible");

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
            magasin.setCodeMagasin(codes);
        return magasinRepository.save(magasin);
    }

    public Magasin updateMagasin(Magasin magasin, MultipartFile imageFile, Integer id) throws Exception{
    
        Stock stock = stockRepository.findByIdStock(magasin.getStock().getIdStock());
        Magasin mag= magasinRepository.findById(id).orElseThrow(null);

        if(stock == null)
            throw new IllegalStateException("Aucune stock disponible");
        
        mag.setContactMagasin(magasin.getContactMagasin());
        mag.setLatitude(magasin.getLatitude());
        mag.setLongitude(magasin.getLongitude());
        mag.setLocaliteMagasin(magasin.getLocaliteMagasin());
        mag.setNomMagasin(magasin.getNomMagasin()); 
        if (magasin.getStock() != null) {
            mag.setStock(magasin.getStock());
        }

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
            
        return magasinRepository.save(magasin);
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

    public List<Magasin> getMagasinByActeur(Integer id) {
        List<Magasin> magasinList = magasinRepository.findByActeurIdActeur(id);

        if(magasinList.isEmpty())
            throw new IllegalStateException("Aucun magasin trouvé");
        
        magasinList = magasinList.
        stream().sorted((m1,m2) -> m2.getNomMagasin().compareTo(m1.getNomMagasin()))
        .collect(Collectors.toList());

        return magasinList;
    }
    public String supprimerMagagin(Integer id){
        Magasin magasin = magasinRepository.findById(id).orElseThrow(null);

        magasinRepository.delete(magasin);

        return "supprimé avec success";
    }
}
