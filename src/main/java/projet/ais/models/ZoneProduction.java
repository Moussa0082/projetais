package projet.ais.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class ZoneProduction {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idZoneProduction;

    @Schema(required = true)
@Column(nullable = false)
    private String codeZone;

    @Schema(required = true)
@Column(nullable = false)
    private String nomZoneProduction;

    @Schema(required = true)
@Column(nullable = false)
    private String latitude;

    @Schema(required = true)
@Column(nullable = false)
    private String longitude;

    @Column(nullable = true)
    private String photoZone;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    @Schema(required = true)
@Column(nullable = false)
    private boolean hasAssociation = false;

    @Column(nullable = true)
    private String personneModif;

    // @Schema(required = true)
@Column(nullable = false)
    private boolean statutZone = true;

    @OneToMany(mappedBy = "zoneProduction")
    @JsonIgnore
    private List<Stock> stockList;

    @ManyToOne
    @JoinColumn(name = "idActeur")
    private Acteur acteur;
}
