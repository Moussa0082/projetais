package projet.ais.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class ZoneProduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idZone;

    @Column(nullable = false)
    private String codeActeur;

    @Column(nullable = false)
    private String nomZone;

    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String longitude;

    @Column(nullable = false)
    private String photoZone;

    @OneToMany
    (mappedBy = "zoneProduction")
    @JsonIgnore
    private List<Stock> stockList;
}

