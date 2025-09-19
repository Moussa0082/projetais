package projet.ais.models;



import java.time.LocalDateTime;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Magasin {

    @Id
    private String idMagasin;

    @Column(nullable = false)
    private String codeMagasin;

    @Column(nullable = false)
    private String nomMagasin;

    @Column(nullable = true)
    private String latitude;

    @Column(nullable = true)
    private String longitude;

    @Column(nullable = false)
    private String localiteMagasin;

    @Column(nullable = false)
    private String contactMagasin;

    @Column(nullable=true)
    private String personneModif;
    
    @Column(nullable = false)
    private boolean statutMagasin = true;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    @Column(nullable = true)
    private String photo;

    @Column(nullable = false)
    private boolean hasAssociation = false;

    @Column(nullable = true)
    private String pays;

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


