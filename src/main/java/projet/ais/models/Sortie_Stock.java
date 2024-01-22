package projet.ais.models;


import jakarta.persistence.*;

import java.util.Date;

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
}

