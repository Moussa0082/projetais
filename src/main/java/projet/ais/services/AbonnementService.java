package projet.ais.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Abonnement;
import projet.ais.models.Forme;
import projet.ais.repository.AbonnementRepository;

@Service
public class AbonnementService {
    
    @Autowired
    AbonnementRepository aRepository;
    @Autowired
    CodeGenerator codeGenerator;
    @Autowired
    IdGenerator idGenerator ;

    public Abonnement creerAbonnement(Abonnement abonnement) {
      
        abonnement.setDateAjout(LocalDate.now()); 

        String typeAbonnement = abonnement.getTypeAbonnement();
        
        if (abonnement.getDateAjout() != null && typeAbonnement != null) {
            switch (typeAbonnement.toLowerCase()) {
                case "semestriel":
                    abonnement.setDateFin(abonnement.getDateAjout().plusMonths(6));
                    break;
                case "annuel":
                    abonnement.setDateFin(abonnement.getDateAjout().plusMonths(12));
                    break;
                default:
                    throw new IllegalArgumentException("Type d'abonnement non reconnu : " + typeAbonnement);
            }
        } else {
            throw new IllegalStateException("La date d'ajout ou le type d'abonnement est null");
        }
       
        String codes = codeGenerator.genererCode();
        String idcodes = idGenerator.genererCode();
        
        abonnement.setCodeAbonnement(codes);
        abonnement.setIdAbonnement(idcodes);
        return aRepository.save(abonnement);
    }
    
    private Abonnement calculerDateFin(Abonnement abonnement) {
        String typeAbonnement = abonnement.getTypeAbonnement();

        if (abonnement.getDateAjout() != null && typeAbonnement != null) {
            switch (typeAbonnement.toLowerCase()) {
                case "semestriel":
                    abonnement.setDateFin(abonnement.getDateAjout().plusMonths(6));
                    break;
                case "annuel":
                    abonnement.setDateFin(abonnement.getDateAjout().plusMonths(12));
                    break;
                default:
                    throw new IllegalArgumentException("Type d'abonnement non reconnu : " + typeAbonnement);
            }
        } else {
            throw new IllegalStateException("La date d'ajout ou le type d'abonnement est null");
        }

        return abonnement;
    }


    public List<Abonnement> getAll(){
        List<Abonnement> aList = aRepository.findAll();

        if(aList.isEmpty()){
            throw new EntityNotFoundException("Liste vide");
        }

        return aList;
    }

    public List<Abonnement> getAllByActeur(String id){
        List<Abonnement> aList = aRepository.findByActeurIdActeur(id);

        if(aList.isEmpty()){
            throw new EntityNotFoundException("Liste vide");
        }

        aList.sort(Comparator.comparing(Abonnement::getDateAjout).reversed());
        return aList;
    }

  
    public Abonnement getLastAbonnementByActeur(String idActeur) {
        return aRepository.findTopByActeurIdActeurOrderByDateAjoutDesc(idActeur);
    }
    

     public Abonnement active(String id) throws Exception{
        Abonnement a = aRepository.findById(id).orElseThrow(null);

        try {
            a.setStatutAbonnement(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation : " + e.getMessage());
        }
        return aRepository.save(a);
    }

    public Abonnement desactive(String id) throws Exception{
        Abonnement a = aRepository.findById(id).orElseThrow(null);

        try {
            a.setStatutAbonnement(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la desactivation : " + e.getMessage());
        }
        return aRepository.save(a);
    }


    public String deleteAbonnement(String id){
        Abonnement a = aRepository.findById(id).orElseThrow(null);

        aRepository.delete(a);
        return "Supprimé avec succèss";
    }
}
