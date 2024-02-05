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
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idFiliere;

    @Column(nullable = false)
    private String codeFiliere;

    @Column(nullable = false)
    private String libelleFiliere;

    @Column(nullable = false)
    private String descriptionFiliere;

    @Column(nullable = false)
    private boolean statutFiliere = true;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateAjout;

    @PrePersist
    public void prePersist() {
        dateAjout = LocalDateTime.now();
    }


    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateModif;

    public LocalDateTime updateDateModif(LocalDateTime dateModif) {
        this.dateModif = dateModif;
        return dateModif;
    }

    @Column(nullable=true)
    private String personneAjout;

    @OneToMany
    (mappedBy = "filiere")
    @JsonIgnore
    private List<CategorieProduit> categorieProduitList;
}

