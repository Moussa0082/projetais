package projet.ais.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class ZoneProduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idZoneProduction;

    @Column(nullable = false)
    private String codeZone;

    @Column(nullable = false)
    private String nomZoneProduction;

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

