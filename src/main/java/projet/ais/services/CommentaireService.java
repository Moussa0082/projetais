package projet.ais.services;



import projet.ais.models.Commentaire;

import projet.ais.repository.CommentRepo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class CommentaireService {

    @Autowired
    private CommentRepo cRepository;

    public Commentaire createCommentaire(Commentaire c){
        String pattern = "yyyy-MM-dd HH:mm";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(formatter);
            c.setDateAjout(formattedDateTime);
            
        return cRepository.save(c);
    }
 
  
    
    public List<Commentaire> getAllCommentaire(){
        List<Commentaire> com = cRepository.findAll();

        if(com.isEmpty()){
            throw new IllegalArgumentException("Aucun commentaire trouvé");
        }

        com = com
        .stream().sorted((v1,v2) -> v2.getDateAjout().compareTo(v1.getDateAjout()))
        .collect(Collectors.toList());

        return com;
    }

    public Page<Commentaire> getAllCommentaireByPageable(Pageable pageable) {
        return cRepository.findAll(pageable);
    }

    public Commentaire getCommentaireById(long id){
        return cRepository.findById(id).orElseThrow(null);
    }

    public String deleteCommentaire(long id){
        Commentaire c = cRepository.findById(id).orElseThrow(null);

        if(c == null){
            throw new IllegalArgumentException("Aucun commentaire trouvé");
        }

        cRepository.delete(c);

        return "Commentaire supprimé avec succès";
    }
}