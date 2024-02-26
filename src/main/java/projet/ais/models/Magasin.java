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
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idMagasin;

    @Column(nullable = false)
    private String codeMagasin;

    @Column(nullable = false)
    private String nomMagasin;
    // @Column(nullable = false)
    // private String codeActeur;

    @Column(nullable = false)
    private String niveau3PaysMagasin;

    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String longitude;

    @Column(nullable = false)
    private String localiteMagasin;

    @Column(nullable = false)
    private String contactMagasin;

    @Column(nullable=true)
    private String personneModif;
    
    @Column(nullable = false)
    private boolean statutMagasin = true;

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

    @Column(nullable = true)
    private String photo;

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

    // @ManyToOne
    // @JoinColumn(name = "idCategorieProduit")
    // @JsonIgnore
    // private CategorieProduit categorieProduit;


}


