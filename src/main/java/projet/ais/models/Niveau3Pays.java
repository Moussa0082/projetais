package projet.ais.models;


import jakarta.persistence.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
public class Niveau3Pays {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idNiveau3Pays;

    @Schema(required = true)
@Column(nullable = false)
    private String codeN3;

    @Schema(required = true)
@Column(nullable = false)
    private String nomN3;

    @Column(nullable = true,columnDefinition = "TEXT")
    private String descriptionN3;


    @Column(nullable = true)
    private String personneModif;

    @Schema(required = true)
@Column(nullable = false)
    private boolean statutN3 = true;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    @Schema(required = true)
@Column(nullable = false)
    private boolean hasAssociation = false;

    @ManyToOne
    @JoinColumn( name = "idNiveau2Pays") 
    private  Niveau2Pays niveau2Pays;
}

