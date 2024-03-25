package projet.ais.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
public class Stock {

    @Id
    private String idStock;

    @Column(nullable = true)
    private String codeStock;

    @Column(nullable = true)
    private String nomProduit;

    @Column(nullable = true)
    private String formeProduit;

    private String dateProduction;

    @Column(nullable = false)
    private double quantiteStock;

    @Column(nullable = true)
    private int prix;
    
    @Column(nullable = true)
    private String typeProduit;

    // @Column(nullable = false)
    // private String siteProduction;

    @Column(nullable = true)
    private String descriptionStock;

    @Column(nullable = true)
    private String photo;

    @ManyToOne
    @JoinColumn(name = "idZoneProduction")
    private ZoneProduction zoneProduction;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;
    
    @Column(nullable=true)
    private String personneModif;
    
    @Column(nullable = false)
    private boolean statutSotck = true;

    @ManyToOne
    @JoinColumn(name = "idSpeculation")
    private Speculation speculation;

    @ManyToOne
    @JoinColumn(name = "idUnite")
    private Unite unite;

    @ManyToOne
    @JoinColumn(name = "idMagasin")
    private Magasin magasin;

    @ManyToOne
    @JoinColumn(name = "idActeur")
    private Acteur acteur;
    
    @ManyToMany(mappedBy = "stock", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Commande>  commande;

    @OneToMany
    (mappedBy = "stock")
    @JsonIgnore
    private List<Sortie_Stock> sortie_Stock;

  

}


