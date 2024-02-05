package projet.ais.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
public class Niveau1Pays {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idNiveau1Pays;

    @Column(nullable = false)
    private String codeN1;

    @Column(nullable = false)
    private String nomN1;

    @Column(nullable = false)
    private String descriptionN1;

    @Column(nullable = false)
    private boolean statutN1 = true;

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

    @ManyToOne
    @JoinColumn( name = "idPays")
    private Pays pays;

    @OneToMany
    (mappedBy = "niveau1Pays")
    @JsonIgnore
    private List<Niveau2Pays> niveau2PaysList;
}

