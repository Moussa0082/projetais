package projet.ais.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
public class SousRegion {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idSousRegion;

    @Column(nullable = false)
    private String codeSousRegion;

    @Column(nullable = false)
    private String nomSousRegion;

    @Column(nullable = false)
    private boolean statutSousRegion = true;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;
    
    @Column(nullable = true)
    private String personneModif;

    // @PrePersist
    // public void prePersist() {
    //     dateAjout = LocalDateTime.now();
    // }


    // @Column(columnDefinition = "TIMESTAMP")
    // private LocalDateTime dateModif;

    // public LocalDateTime updateDateModif(LocalDateTime dateModif) {
    //     this.dateModif = dateModif;
    //     return dateModif;
    // }

    @OneToMany
    (mappedBy = "sousRegion")
    @JsonIgnore
    private List<Pays> paysList;

    @ManyToOne
    @JoinColumn( name = "idContinent")
    private Continent continent;
}
