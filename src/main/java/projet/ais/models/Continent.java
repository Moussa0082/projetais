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

    

    @OneToMany
    (mappedBy = "continent")
    @JsonIgnore
    private List<SousRegion> sousRegionList;
}

