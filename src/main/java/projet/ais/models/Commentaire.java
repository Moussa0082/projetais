package projet.ais.models;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(required = true)
@Column(nullable = false)
    private String acteur;

}
