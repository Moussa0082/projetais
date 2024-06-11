package projet.ais.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
public class Pays {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idPays;

    @Column(nullable = false)
    private String codePays;

    @Column(nullable = false)
    private String nomPays;

    @Column(nullable = false)
    private String libelleNiveau1Pays;

    @Column(nullable = false)
    private String libelleNiveau2Pays;

    @Column(nullable = false)
    private String libelleNiveau3Pays;

    @Column(nullable = true)
    private String monnaie;

    // @Column(nullable = true)
    // private String tauxDollar;

    // @Column(nullable = true)
    // private String tauxYuan;
    
    // @Column(nullable = true)
    // private String tauxEuro;

    @Column(nullable = false)
    private String descriptionPays;

    @Column(nullable = true)
    private String whattsAppPays;


    @Column(nullable = true)
    private String personneModif;

    @Column(nullable = true)
    private boolean statutPays = true;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    @OneToMany
    (mappedBy = "pays")
    @JsonIgnore
    private List<Niveau1Pays> niveau1PaysList;

    @OneToMany
    (mappedBy = "pays")
    @JsonIgnore
    private List<Acteur> acteursList;

    @ManyToOne
    @JoinColumn( name = "idSousRegion")
    private  SousRegion sousRegion;

}
