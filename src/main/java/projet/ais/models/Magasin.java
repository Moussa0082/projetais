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

    @Column(nullable = true)
    private String codeMagasin;

    @Column(nullable = false)
    private String nomMagasin;
    // @Column(nullable = false)
    // private String codeActeur;

    @Column(nullable = false)
    private String niveau3PaysMagasin;

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

    @Column(columnDefinition = "TIMESTAMP",nullable = true)
    private LocalDateTime dateAjout;

    @PrePersist
    public void prePersist() {
        dateAjout = LocalDateTime.now();
    }


    @Column(columnDefinition = "TIMESTAMP",nullable = true)
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

    @OneToMany
    (mappedBy = "magasin")
    @JsonIgnore
    private List<Stock> stockList;
}

