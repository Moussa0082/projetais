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
import projet.ais.models.TypeMateriel;
import projet.ais.repository.TypeMaterielRepository;

@Service
public class TypeMaterielService {
    
    @Autowired
    TypeMaterielRepository typeMaterielRepository;

    @Autowired
    CodeGenerator codeGenerator;
    @Autowired
    IdGenerator idGenerator ;
    
    public TypeMateriel create(TypeMateriel typeMateriel){
        TypeMateriel type = typeMaterielRepository.findByNom(typeMateriel.getNom());

        if(type != null)
            throw new EntityNotFoundException("Ce type de matériel existe déjà");
        
        String codes = codeGenerator.genererCode();
        String idCodes = idGenerator.genererCode();
        typeMateriel.setCodeTypeMateriel(codes);
        typeMateriel.setIdTypeMateriel(idCodes);

        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);  
        typeMateriel.setDateAjout(formattedDateTime);

        return typeMaterielRepository.save(typeMateriel);
    }

    public TypeMateriel updates(TypeMateriel typeMateriel, String id){
        TypeMateriel type = typeMaterielRepository.findById(id).orElseThrow(null);
       
        type.setNom(typeMateriel.getNom());
        type.setDescription(typeMateriel.getDescription());

        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);
        type.setDateModif(formattedDateTime);
 
        return typeMaterielRepository.save(type);
    }

    public List<TypeMateriel> getTypeMateriel(){
        List<TypeMateriel> typeMateriel = typeMaterielRepository.findAll();

        if(typeMateriel.isEmpty())
            throw new EntityNotFoundException("Aucun type de voiture trouvée");

        typeMateriel = typeMateriel
        .stream().sorted((t1,t2) -> t2.getNom().compareTo(t1.getNom()))
        .collect(Collectors.toList());

        return typeMateriel;
    }

    // public List<TypeMateriel> getTypeMaterielByActeur(String id){
    //     List<TypeMateriel> TypeMateriel = typeMaterielRepository.findByActeurIdActeur(id);

    //     if(TypeMateriel.isEmpty())
    //         throw new EntityNotFoundException("Aucun type de voiture trouvée");

    //     TypeMateriel = TypeMateriel
    //     .stream().sorted((t1,t2) -> t2.getNom().compareTo(t1.getNom()))
    //     .collect(Collectors.toList());

    //     return TypeMateriel;
    // }


      public String deleteType(String id){
        TypeMateriel typeMateriel = typeMaterielRepository.findById(id).orElseThrow(null);

        typeMaterielRepository.delete(typeMateriel);
        return "Supprimé avec success";
    }

    public TypeMateriel active(String id) throws Exception{
        TypeMateriel typeMateriel = typeMaterielRepository.findById(id).orElseThrow(null);

        try {
            typeMateriel.setStatutType(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation : " + e.getMessage());
        }
        return typeMaterielRepository.save(typeMateriel);
    }

    public TypeMateriel desactive(String id) throws Exception{
        TypeMateriel typeMateriel = typeMaterielRepository.findById(id).orElseThrow(null);

        try {
            typeMateriel.setStatutType(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la desactivation : " + e.getMessage());
        }
        return typeMaterielRepository.save(typeMateriel);
    }
}
