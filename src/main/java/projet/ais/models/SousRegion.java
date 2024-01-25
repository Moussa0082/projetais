package projet.ais.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class SousRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSousRegion;

    @Column(nullable = false)
    private String codeSousRegion;

    @Column(nullable = false)
    private String nomSousRegion;

    @Column(nullable = false)
    private String statutSousRegion;

    @OneToMany
    (mappedBy = "sousRegion")
    @JsonIgnore
    private List<Pays> paysList;

    @ManyToOne
    @JoinColumn( name = "idContinent")
    private Continent continent;
}
