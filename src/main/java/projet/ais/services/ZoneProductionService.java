package projet.ais.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Acteur;
import projet.ais.models.Unite;
import projet.ais.models.ZoneProduction;
import projet.ais.repository.ActeurRepository;
import projet.ais.repository.ZoneProductionRepository;
import com.sun.jdi.request.DuplicateRequestException;

import jakarta.persistence.EntityNotFoundException;


@Service
public class ZoneProductionService {
    
    @Autowired
    ZoneProductionRepository zoneProductionRepository;
    @Autowired
    CodeGenerator codeGenerator;
    @Autowired
    IdGenerator idGenerator ;
    @Autowired
    ActeurRepository acteurRepository;

    public ZoneProduction createZoneProduction(ZoneProduction zoneProduction, MultipartFile imageFile) throws Exception{
        ZoneProduction zoneProductions = zoneProductionRepository.findByNomZoneProduction(zoneProduction.getNomZoneProduction());
        //  Acteur acteur = acteurRepository.findByIdActeur(zoneProduction.getActeur().getIdActeur());

        // if(acteur == null)
        //     throw new IllegalStateException("Aucun acteur trouvé");

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
            String idCodes = idGenerator.genererCode();

        zoneProduction.setCodeZone(codes);
        zoneProduction.setStatutZone(true);
        zoneProduction.setIdZoneProduction(idCodes);
        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);
        zoneProduction.setDateAjout(formattedDateTime);
        return zoneProductionRepository.save(zoneProduction);
    }

    public ZoneProduction updateZoneProduction(ZoneProduction zoneProduction, String id,MultipartFile imageFile) throws Exception{
        ZoneProduction zoneProductions = zoneProductionRepository.findById(id).orElseThrow(null);

        zoneProductions.setNomZoneProduction(zoneProduction.getNomZoneProduction());
        zoneProduction.setLatitude(zoneProduction.getLatitude());
        zoneProductions.setLongitude(zoneProduction.getLongitude());
        zoneProductions.setDateAjout(zoneProductions.getDateAjout());
        zoneProductions.setPersonneModif(zoneProduction.getPersonneModif() );
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
        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);       
        zoneProduction.setDateModif(formattedDateTime);
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

    // public List<ZoneProduction> getZoneProductionByActeur(String idActeur){
    //     List<ZoneProduction> zoneProductionList = zoneProductionRepository.findByActeurIdActeur(idActeur);

    //     if(zoneProductionList.isEmpty())
    //         throw new EntityNotFoundException("Speculation non trouvé");
    //     zoneProductionList = zoneProductionList
    //     .stream().sorted((z1,z2) -> z2.getNomZoneProduction().compareTo(z1.getNomZoneProduction()))
    //     .collect(Collectors.toList());

    //     return zoneProductionList;
    // }

     public List<ZoneProduction> getAllZoneByActeur(String id){
        List<ZoneProduction> zoneList = zoneProductionRepository.findByActeurIdActeur(id);

        if(zoneList.isEmpty())
             throw new EntityNotFoundException("Liste sous region vide");
             
             zoneList = zoneList
        .stream().sorted((u1,u2) -> u2.getNomZoneProduction().compareTo(u1.getNomZoneProduction()))
        .collect(Collectors.toList());

        return zoneList;
    }

    public String deleteZoneProduction(String id){
        ZoneProduction zoneProduction = zoneProductionRepository.findById(id).orElseThrow(null);

        zoneProductionRepository.delete(zoneProduction);
        return "Supprimé avec success";
    }

    public ZoneProduction active(String id) throws Exception{
        ZoneProduction zoneProduction = zoneProductionRepository.findById(id).orElseThrow(null);

        try {
            zoneProduction.setStatutZone(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation  de la zone de production : " + e.getMessage());
        }
        return zoneProductionRepository.save(zoneProduction);
    }

    public ZoneProduction desactive(String id) throws Exception{
        ZoneProduction zoneProduction = zoneProductionRepository.findById(id).orElseThrow(null);

        try {
            zoneProduction.setStatutZone(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la descativation de la zone de production : " + e.getMessage());
        }
        return zoneProductionRepository.save(zoneProduction);
    }
}
