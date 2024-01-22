package projet.ais.models;



import jakarta.persistence.*;

import java.util.List;

@Entity
public class Magasin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMagasin;

    @Column(nullable = false)
    private String codeMagasin;

    @Column(nullable = false)
    private String codeActeur;

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

    @Column(nullable = false)
    private String photo;

    @OneToOne
    private Stock stock;

    @ManyToOne
    @JoinColumn( name = "idActeur")
    private Acteur acteur;
}

