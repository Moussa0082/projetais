package projet.ais.models;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
public class Niveau2Pays {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idNiveau2Pays;

    @Column(nullable = false)
    private String codeN2;

    @Column(nullable = false)
    private String nomN2;

    @Column(nullable = false)
    private String descriptionN2;

    @Column(nullable = false)
    private boolean statutN2 = true;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateAjout;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateModif;

    @ManyToOne
    @JoinColumn( name = "idNiveau1Pays")
    private Niveau1Pays niveau1Pays;

    @OneToMany
    (mappedBy = "niveau2Pays")
    @JsonIgnore
    private List<Niveau3Pays> niveau3PaysList;

}
