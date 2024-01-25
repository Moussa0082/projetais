package projet.ais.models;




import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Continent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idContinent;

    @Column(nullable = false)
    private String codeContinent;

    @Column(nullable = false)
    private String nomContinent;

    @Column(nullable = false)
    private String descriptionContinent;

    @Column(nullable = false)
    private String statutContinent;

    @OneToMany
    (mappedBy = "continent")
    @JsonIgnore
    private List<SousRegion> sousRegionList;
}

