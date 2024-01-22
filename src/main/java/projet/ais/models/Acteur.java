package projet.ais.models;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity

public class Acteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idActeur;

    @Column(nullable = false)
    private String codeActeur;

    @Column(nullable = false)
    private String nomActeur;

    @Column(nullable = false)
    private String adresseActeur;

    @Column(nullable = false)
    private String telephoneActeur;

    @Column(nullable = true)
    private String whatsAppActeur;

    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String longitude;

    @Column(nullable = false)
    private String photoSiegeActeur;

    @Column(nullable = false)
    private String logoActeur;

    @Column(nullable = false)
    private String niveau3PaysActeur;

    @Column(nullable = false)
    private String localiteActeur;

    @Column(nullable = false)
    private String emailActeur;

    @Column(nullable = false)
    private String MaillonActeur;

    @Column(nullable = false)
    private String filiereActeur;

    @Column(nullable = false)
    private String statutActeur;

    @OneToMany
    (mappedBy = "acteur")
    @JsonIgnore
    private List<Stock> stockList;

    @OneToMany
    @JsonIgnore
    private List<Magasin> magasinList;

   @ManyToOne
   private TypeActeur typeActeur;
}

