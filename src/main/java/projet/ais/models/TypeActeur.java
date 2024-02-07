package projet.ais.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
public class TypeActeur {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idTypeActeur;

    @Column(nullable = false)
    private String libelle;
    
    @Column(nullable = false)
    private String codeTypeActeur;

    @Column(nullable = true)
    private boolean statutTypeActeur;

    @Column(nullable = false)
    private String descriptionTypeActeur;

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

    @ManyToMany(mappedBy = "typeActeur", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Acteur> acteur;

}

