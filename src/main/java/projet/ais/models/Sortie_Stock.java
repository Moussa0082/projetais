package projet.ais.models;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
public class Sortie_Stock {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idSortieStock;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateSortie;

    @Column(nullable = false)
    private String  codeSortie;

    @Column(nullable = false)
    private double quantiteSortie;
    
    @Column(nullable = false)
    private int prixVente;

    @Column(nullable = false)
    private String codeActeurDestination;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateAjout;

    @PrePersist
    public void prePersist() {
        dateAjout = LocalDateTime.now();
        dateSortie = LocalDateTime.now();
    }


    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateModif;

    public LocalDateTime updateDateModif(LocalDateTime dateModif) {
        this.dateModif = dateModif;
        return dateModif;
    }


    @Column(nullable=true)
    private String personneAjout;

    @ManyToOne
    @JoinColumn(name = "idStock")
    private Stock stock;
}

