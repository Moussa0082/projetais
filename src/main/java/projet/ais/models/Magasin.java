package projet.ais.models;



import java.time.LocalDateTime;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;


@Entity
@Data
public class Magasin {

    @Id
    private String idMagasin;

    @Schema(required = true)
@Column(nullable = false)
    private String codeMagasin;

    @Schema(required = true)
@Column(nullable = false)
    private String nomMagasin;

    @Column(nullable = true)
    private String latitude;

    @Column(nullable = true)
    private String longitude;

    @Schema(required = true)
@Column(nullable = false)
    private String localiteMagasin;

    @Schema(required = true)
@Column(nullable = false)
    private String contactMagasin;

    @Column(nullable=true)
    private String personneModif;
    
    @Schema(required = true)
@Column(nullable = false)
    private boolean statutMagasin = true;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    @Column(nullable = true)
    private String photo;

    @Schema(required = true)
@Column(nullable = false)
    private boolean hasAssociation = false;

    @Column(nullable = true)
    private String pays;

    @Schema(required = true)
@Column(nullable = false)
    private int nbreView = 0 ;

    @ManyToOne
    @JoinColumn( name = "idActeur")
    private Acteur acteur;

    @ManyToOne
    @JoinColumn( name = "idNiveau1Pays")
    private Niveau1Pays niveau1Pays;

    @OneToMany
    (mappedBy = "magasin")
    @JsonIgnore
    private List<Stock> stockList;

}


