package projet.ais.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class ZoneProduction {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idZoneProduction;

    @Column(nullable = false)
    private String codeZone;

    @Column(nullable = false)
    private String nomZoneProduction;

    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String longitude;

    @Column(nullable = true)
    private String photoZone;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

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

    @Column(nullable=true)
    private String personneModif;
    
    @Column(nullable = false)
    private boolean statutZone = true;
    
    @OneToMany
    (mappedBy = "zoneProduction")
    @JsonIgnore
    private List<Stock> stockList;

    @ManyToOne
    @JoinColumn( name = "idActeur")
    private Acteur acteur;
}

