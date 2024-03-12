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
import projet.ais.models.TypeVoiture;
import projet.ais.models.Unite;
import projet.ais.repository.TypeVoitureRepository;

@Service
public class TypeVoitureService {
    
    @Autowired
    private TypeVoitureRepository typeVoitureRepository;
    @Autowired
    CodeGenerator codeGenerator;
    @Autowired
    IdGenerator idGenerator ;

    public TypeVoiture create(TypeVoiture typeVoiture){
        TypeVoiture type = typeVoitureRepository.findByNom(typeVoiture.getNom());

        if(type != null)
            throw new EntityNotFoundException("Ce type de voiture existe déjà");
        
        String codes = codeGenerator.genererCode();
        String idCodes = idGenerator.genererCode();
        typeVoiture.setCodeTypeVoiture(codes);
        typeVoiture.setIdTypeVoiture(idCodes);

        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);  
        typeVoiture.setDateAjout(formattedDateTime);

        return typeVoitureRepository.save(typeVoiture);
    }

    public TypeVoiture updates(TypeVoiture typeVoiture, String id){
        TypeVoiture type = typeVoitureRepository.findById(id).orElseThrow(null);
       
        type.setNom(typeVoiture.getNom());
        type.setNombreSieges(typeVoiture.getNombreSieges());
        type.setDescription(typeVoiture.getDescription());

        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);
        type.setDateModif(formattedDateTime);

        return typeVoitureRepository.save(type);
    }

    public List<TypeVoiture> getTypeVoiture(){
        List<TypeVoiture> typeVoiture = typeVoitureRepository.findAll();

        if(typeVoiture.isEmpty())
            throw new EntityNotFoundException("Aucun type de voiture trouvée");

        typeVoiture = typeVoiture
        .stream().sorted((t1,t2) -> t2.getNom().compareTo(t1.getNom()))
        .collect(Collectors.toList());

        return typeVoiture;
    }

    public List<TypeVoiture> getTypeVoitureByActeur(String id){
        List<TypeVoiture> typeVoiture = typeVoitureRepository.findByActeurIdActeur(id);

        if(typeVoiture.isEmpty())
            throw new EntityNotFoundException("Aucun type de voiture trouvée");

        typeVoiture = typeVoiture
        .stream().sorted((t1,t2) -> t2.getNom().compareTo(t1.getNom()))
        .collect(Collectors.toList());

        return typeVoiture;
    }


      public String deleteType(String id){
        TypeVoiture typeVoiture = typeVoitureRepository.findById(id).orElseThrow(null);

        typeVoitureRepository.delete(typeVoiture);
        return "Supprimé avec success";
    }

    public TypeVoiture active(String id) throws Exception{
        TypeVoiture typeVoiture = typeVoitureRepository.findById(id).orElseThrow(null);

        try {
            typeVoiture.setStatutType(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation : " + e.getMessage());
        }
        return typeVoitureRepository.save(typeVoiture);
    }

    public TypeVoiture desactive(String id) throws Exception{
        TypeVoiture typeVoiture = typeVoitureRepository.findById(id).orElseThrow(null);

        try {
            typeVoiture.setStatutType(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la desactivation : " + e.getMessage());
        }
        return typeVoitureRepository.save(typeVoiture);
    }
}
