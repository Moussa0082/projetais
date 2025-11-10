package projet.ais.models;

import java.util.*;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Data
public class Vehicule {

    @Id
    private String idVehicule;

    @Schema(required = true)
@Column(nullable = false)
    private String nomVehicule;

    @Schema(required = true)
@Column(nullable = false)
    private String capaciteVehicule;

    @Column(nullable = true)
    private String codeVehicule;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = true)
    private int nbKilometrage;

    @Schema(required = true)
@Column(nullable = false)
    private boolean hasAssociation = false;

    @Convert(converter = MapToJsonConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Integer> prixParDestination;

    @Column
    private boolean statutVehicule = true;

    @Column(nullable = true)
    private String pays;

    @Column(nullable = true)
    private String photoVehicule;

    @Schema(required = true)
@Column(nullable = false)
    private String localisation;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    @Schema(required = true)
@Column(nullable = false)
    private String etatVehicule;

    @Column(nullable = true)
    private String personneModif;

    @Schema(required = true)
@Column(nullable = false)
    private int nbreView = 0;

    @ManyToOne
    @JoinColumn(name = "idActeur")
    private Acteur acteur;

    @ManyToOne
    @JoinColumn(name = "idTypeVoiture")
    private TypeVoiture typeVoiture;

    @ManyToOne
    @JoinColumn(name = "idMonnaie")
    private Monnaie monnaie;

}
