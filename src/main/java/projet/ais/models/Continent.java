package projet.ais.models;




import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
public class Continent {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idContinent;

    @Column(nullable = false)
    private String codeContinent;

    @Column(nullable = false)
    private String nomContinent;

    @Column(nullable = false)
    private String descriptionContinent;

    @Column(nullable = false)
    private boolean statutContinent = true;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    

    @OneToMany
    (mappedBy = "continent")
    @JsonIgnore
    private List<SousRegion> sousRegionList;
}

