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
    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;
    // @Column(columnDefinition = "TIMESTAMP",nullable = true)
    // private LocalDateTime dateAjout;

    // @PrePersist
    // public void prePersist() {
    //     dateAjout = LocalDateTime.now();
    // }

    @Column(nullable=true)
    private String personneModif;

    // @Column(columnDefinition = "TIMESTAMP",nullable = true)
    // private LocalDateTime dateModif;

    // public LocalDateTime updateDateModif(LocalDateTime dateModif) {
    //     this.dateModif = dateModif;
    //     return dateModif;
    // }



    @ManyToOne
    @JoinColumn( name = "idActeur")
    private Acteur acteur;
    
    @OneToMany
    (mappedBy = "filiere")
    @JsonIgnore
    private List<CategorieProduit> categorieProduitList;
}

