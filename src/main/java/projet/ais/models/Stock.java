package projet.ais.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idStock;

    @Column(nullable = false)
    private String codeStock;

    @Column(nullable = false)
    private String nomProduit;

    @Column(nullable = false)
    private String formeProduit;

    @Column(nullable = false)
    private Date dateProduction;

    @Column(nullable = false)
    private String quantiteStock;

    @Column(nullable = false)
    private String typeProduit;

    // @Column(nullable = false)
    // private String siteProduction;

    @Column(nullable = false)
    private String descriptionStock;

    @Column(nullable = false)
    private String photo;

    @ManyToOne
    @JoinColumn(name = "idZoneProduction")
    private ZoneProduction zoneProduction;

    @ManyToOne
    @JoinColumn(name = "idSpeculation")
    private Speculation speculation;

    @ManyToOne
    @JoinColumn(name = "idUnite")
    private Unite unite;

    @OneToOne
    // @JoinColumn(name = "idMagasin")
    private Magasin magasin;

    @ManyToOne
    @JoinColumn(name = "idActeur")
    private Acteur acteur;
}

