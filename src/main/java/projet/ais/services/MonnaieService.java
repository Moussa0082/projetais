package projet.ais.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Monnaie;
import projet.ais.models.Unite;
import projet.ais.repository.MonnaieRepository;

@Service
public class MonnaieService {
    
    @Autowired
    private MonnaieRepository monnaieRepository;
    @Autowired
    IdGenerator idGenerator;
    @Autowired
    CodeGenerator codeGenerator;

    public Monnaie createMonnaie(Monnaie monnaie){
        // if(unites != null)
        //     throw new IllegalStateException("cet Unité existe déjà");
        
        String codes = codeGenerator.genererCode();
        String idcodes = idGenerator.genererCode();
        monnaie.setIdMonnaie(idcodes);
        monnaie.setCodeMonnaie(codes);

        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);  
        monnaie.setDateAjout(formattedDateTime);
        return monnaieRepository.save(monnaie);
    }

    public Monnaie updateMonnaie(Monnaie monnaie, String id){
        Monnaie m = monnaieRepository.findById(id).orElseThrow(null);

        m.setLibelle(monnaie.getLibelle());
        m.setSigle(monnaie.getSigle());

        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);  
        monnaie.setDateModif(formattedDateTime);
        return monnaieRepository.save(m);
    }

    public List<Monnaie> getAllMonnaies(){
        List<Monnaie> monnaies = monnaieRepository.findAll();

        if(monnaies.isEmpty())
            throw new EntityNotFoundException("Liste vide");

        monnaies = monnaies
        .stream().sorted((u1,u2) -> u2.getLibelle().compareTo(u1.getLibelle()))
        .collect(Collectors.toList());

        return monnaies;
    }

    public String deleteMonnaie(String id){
        Monnaie monnaie = monnaieRepository.findById(id).orElseThrow(null);

        monnaieRepository.delete(monnaie);
        return "Supprimé avec success";
    }

    public Monnaie active(String id) throws Exception{
        Monnaie monnaie = monnaieRepository.findById(id).orElseThrow(null);

        try {
            monnaie.setStatut(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation : " + e.getMessage());
        }
        return monnaieRepository.save(monnaie);
    }


    public Monnaie desactive(String id) throws Exception{
        Monnaie monnaie = monnaieRepository.findById(id).orElseThrow(null);

        try {

            monnaie.setStatut(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation : " + e.getMessage());
        }
        return monnaieRepository.save(monnaie);
    }
}
