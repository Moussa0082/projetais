package projet.ais.models;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Commentaire{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCommentaire;

   

    @Column(nullable = true, columnDefinition = "TEXT")
    private String description;

     @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = false)
    private String acteur;

}
