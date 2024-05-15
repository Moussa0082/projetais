package projet.ais.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Acteur;
import projet.ais.models.Forme;
import projet.ais.repository.FormeRepository;

@Service
public class FormeService {
    
    @Autowired
    FormeRepository formeRepository;
    @Autowired
    CodeGenerator codeGenerator;
    @Autowired
    IdGenerator idGenerator ;

    public Forme createForme(Forme forme){
        Forme form = formeRepository.findBylibelleForme(forme.getLibelleForme());

        if(form != null)
            throw new DataIntegrityViolationException("Cette forme de produit existe déjà");

            String codes = codeGenerator.genererCode();
            String Idcodes = idGenerator.genererCode();

            forme.setCodeForme(codes);
            forme.setIdForme(Idcodes);

            return formeRepository.save(forme);
    }
    
    public List<Forme> getAllForme(){
        List<Forme> formes = formeRepository.findAll();

        if(formes.isEmpty()){
            throw new EntityNotFoundException("Aucun formes trouvé");
        }
        formes = formes
                .stream().sorted((d1, d2) -> d2.getLibelleForme().compareTo(d1.getLibelleForme()))
                .collect(Collectors.toList());
        return formes;
    }

    public Forme update (Forme forme, String id) {
        Forme formes = formeRepository.findById(id).orElseThrow(null);

        formes.setLibelleForme(forme.getLibelleForme());
        formes.setDescriptionForme(forme.getDescriptionForme());

        return formeRepository.save(formes);
    }


    public Page<Forme> getAllFormePageable(Pageable pageable) {
        return formeRepository.findAll(pageable);
    }


    public String deleteForme(String id){
        Forme formes = formeRepository.findById(id).orElseThrow();

        formeRepository.delete(formes);
        return "Supprimé avec success";
    }
    
    public Forme active(String id) throws Exception{
        Forme forme = formeRepository.findById(id).orElseThrow(null);

        try {
            forme.setStatutForme(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation : " + e.getMessage());
        }
        return formeRepository.save(forme);
    }

    public Forme desactive(String id) throws Exception{
        Forme forme = formeRepository.findById(id).orElseThrow(null);

        try {
            forme.setStatutForme(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la desactivation : " + e.getMessage());
        }
        return formeRepository.save(forme);
    }
}
