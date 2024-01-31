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

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateAjout;


    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateModif;

    @OneToMany
    (mappedBy = "sousRegion")
    @JsonIgnore
    private List<Pays> paysList;

    @ManyToOne
    @JoinColumn( name = "idContinent")
    private Continent continent;
}
