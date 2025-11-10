package projet.ais.models;



import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
public class Filiere {

    @Id
    private String idFiliere;

    @Schema(required = true)
@Column(nullable = false)
    private String codeFiliere;

    @Schema(required = true)
@Column(nullable = false)
    private String libelleFiliere;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String descriptionFiliere;

    @Schema(required = true)
@Column(nullable = false)
    private boolean statutFiliere = true;
    
    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    @Column(nullable=true)
    private String personneModif;

    @Schema(required = true)
@Column(nullable = false)
    private boolean hasAssociation = false;

    @OneToMany
    (mappedBy = "filiere")
    @JsonIgnore
    private List<CategorieProduit> categorieProduitList;
}

