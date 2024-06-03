package projet.ais.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projet.ais.IdGenerator;
import projet.ais.models.ParametreFiche;
import projet.ais.models.RegroupementParametre;
import projet.ais.repository.RegroupementParametreRepository;

@Service
public class RegroupementParametreService {
    
    @Autowired
    private RegroupementParametreRepository regroupementParametreRepository;

    @Autowired
    private IdGenerator idGenerator;

    public RegroupementParametre createRegroupementParametres(RegroupementParametre regroupementParametres) {
        String idCodes = idGenerator.genererCode();
        regroupementParametres.setIdRegroupementParametre(idCodes);

        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String formattedDateTime = LocalDateTime.now().format(formatter);
        regroupementParametres.setDateAjout(formattedDateTime);

    
        return regroupementParametreRepository.save(regroupementParametres);
    }

    public RegroupementParametre updateRegroupementParametre(RegroupementParametre regroupementParametre, String id) {
        RegroupementParametre existingRegroupement = regroupementParametreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No regroupement found with id: " + id));
        
        existingRegroupement.setLibelleRegroupement(regroupementParametre.getLibelleRegroupement());
        existingRegroupement.setParametreFiche(regroupementParametre.getParametreFiche());

        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String formattedDateTime = LocalDateTime.now().format(formatter);
        existingRegroupement.setDateModif(formattedDateTime);

        // for (ParametreFiche parametre : existingRegroupement.getParametreFiche()) {
        //     parametre.setRegroupementParametre(existingRegroupement);
        // }

        return regroupementParametreRepository.save(existingRegroupement);
    }

    public List<RegroupementParametre> getAllRegroupementParametres() {
        List<RegroupementParametre> regroupementParam = regroupementParametreRepository.findAll();
        regroupementParam = regroupementParam.stream()
                .sorted((r1, r2) -> r2.getLibelleRegroupement().compareTo(r1.getLibelleRegroupement()))
                .collect(Collectors.toList());

        return regroupementParam;
    }

    public String deleteRegroupement(String id) {
        RegroupementParametre regroupementParam = regroupementParametreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No regroupement found with id: " + id));

        regroupementParametreRepository.delete(regroupementParam);
        return "Supprimé avec succès";
    }

    public RegroupementParametre active(String id) throws Exception {
        RegroupementParametre regroupementParam = regroupementParametreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No regroupement found with id: " + id));

        try {
            regroupementParam.setStatutRegroupement(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation : " + e.getMessage());
        }
        return regroupementParametreRepository.save(regroupementParam);
    }

    public RegroupementParametre desactive(String id) throws Exception {
        RegroupementParametre regroupementParam = regroupementParametreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No regroupement found with id: " + id));

        try {
            regroupementParam.setStatutRegroupement(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la désactivation : " + e.getMessage());
        }
        return regroupementParametreRepository.save(regroupementParam);
    }
}
