package projet.ais.models;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
public class Filiere {

    @Id
    private String idFiliere;

    @Column(nullable = false)
    private String codeFiliere;

    @Column(nullable = false)
    private String libelleFiliere;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descriptionFiliere;

    @Column(nullable = false)
    private boolean statutFiliere = true;
    
    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    @Column(nullable=true)
    private String personneModif;

    @Column(nullable = false)
    private boolean hasAssociation = false;

    @OneToMany
    (mappedBy = "filiere")
    @JsonIgnore
    private List<CategorieProduit> categorieProduitList;
}

