package projet.ais.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import projet.ais.CodeGenerator;
import projet.ais.models.ZoneProduction;
import projet.ais.repository.ZoneProductionRepository;
import com.sun.jdi.request.DuplicateRequestException;

import jakarta.persistence.EntityNotFoundException;


@Service
public class ZoneProductionService {
    
    @Autowired
    ZoneProductionRepository zoneProductionRepository;
    @Autowired
    CodeGenerator codeGenerator;

    public ZoneProduction createZoneProduction(ZoneProduction zoneProduction, MultipartFile imageFile) throws Exception{
        ZoneProduction zoneProductions = zoneProductionRepository.findByNomZoneProduction(zoneProduction.getNomZoneProduction());

        if(zoneProductions != null)
            throw new DuplicateRequestException("Cette zone de production existe déjà");

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
                    zoneProduction.setPhotoZone("ais/" + imageName);
                } catch (IOException e) {
                    throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
                }
            }
            String codes = codeGenerator.genererCode();
            zoneProduction.setCodeZone(codes);
        return zoneProductionRepository.save(zoneProduction);
    }

    public ZoneProduction updateZoneProduction(ZoneProduction zoneProduction, Integer id,MultipartFile imageFile) throws Exception{
        ZoneProduction zoneProductions = zoneProductionRepository.findById(id).orElseThrow(null);

        zoneProductions.setNomZoneProduction(zoneProduction.getNomZoneProduction());
        zoneProduction.setLatitude(zoneProduction.getLatitude());
        zoneProductions.setLongitude(zoneProduction.getLongitude());
        
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
                zoneProductions.setPhotoZone("ais/" + imageName);
            } catch (IOException e) {
                throw new Exception("Erreur lors du traitement du fichier image : " + e.getMessage());
            }
        }
        return zoneProductionRepository.save(zoneProductions);
    }

    public List<ZoneProduction> getZoneProduction(){
        List<ZoneProduction> zoneProductionList = zoneProductionRepository.findAll();

        if(zoneProductionList.isEmpty())
            throw new EntityNotFoundException("Speculation non trouvé");
        zoneProductionList = zoneProductionList
        .stream().sorted((z1,z2) -> z2.getNomZoneProduction().compareTo(z1.getNomZoneProduction()))
        .collect(Collectors.toList());

        return zoneProductionList;
    }

    public String deleteZoneProduction(Integer id){
        ZoneProduction zoneProduction = zoneProductionRepository.findById(id).orElseThrow(null);

        zoneProductionRepository.delete(zoneProduction);
        return "Supprimé avec success";
    }
}
