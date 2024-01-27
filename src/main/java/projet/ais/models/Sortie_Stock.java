package projet.ais.models;


import jakarta.persistence.*;

import java.util.*;

@Entity
public class Sortie_Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSortieStock;

    @Column(nullable = false)
    private Date dateSortie;

    @Column(nullable = false)
    private String  codeStock;

    @Column(nullable = false)
    private int quantiteSortie;

    @Column(nullable = false)
    private String codeActeurDestination;

    @Column(nullable = false)
    private int prixVente;

    @Column(nullable=true)
    private Date dateAjout;

    @Column(nullable=true)
    private Date dateModif;

    @ManyToMany
    private List<Stock> stock;
}

