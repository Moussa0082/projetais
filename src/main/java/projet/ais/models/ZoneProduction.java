package projet.ais.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
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

    @Column(nullable=true)
    private Date dateAjout;

    @Column(nullable=true)
    private Date dateModif;

    @Column(nullable=true)
    private String personneAjout;
    
    @Column(nullable = false)
    private boolean statutZone = true;
    
    @OneToMany
    (mappedBy = "zoneProduction")
    @JsonIgnore
    private List<Stock> stockList;
}

