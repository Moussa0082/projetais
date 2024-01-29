package projet.ais.models;


import jakarta.persistence.*;
import lombok.Data;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
public class Sortie_Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSortieStock;

    @Column(nullable = false)
    private Date dateSortie;

    @Column(nullable = false)
    private String  codeStock;

    @Column(nullable = false)
    private double quantiteSortie;

    @Column(nullable = false)
    private String codeActeurDestination;

    @Column(nullable = false)
    private int prixVente;


    @Column(nullable=true)
    private Date dateModif;

    @ManyToOne
    
    @JoinColumn(name = "idStock")
    private Stock stock;
}

