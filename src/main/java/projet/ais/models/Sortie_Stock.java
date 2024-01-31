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

    @Column(nullable = false)
    private Date dateSortie;

    @Column(nullable = false)
    private String  codeSortie;

    @Column(nullable = false)
    private double quantiteSortie;

    @Column(nullable = false)
    private String codeActeurDestination;

   @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateAjout;


    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateModif;


    @Column(nullable=true)
    private String personneAjout;

    @ManyToOne
    @JoinColumn(name = "idStock")
    private Stock stock;
}

