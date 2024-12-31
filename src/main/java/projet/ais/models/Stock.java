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

    // @Column(nullable = true)
    // private String formeProduit;

    private String dateProduction;

    @Column(nullable = false)
    private double quantiteStock;

    @Column(nullable = true)
    private int prix;
    
    @Column(nullable = true)
    private String typeProduit;

    @Column(nullable = true)
    private String origineProduit;


    @Column(nullable = true ,columnDefinition = "TEXT")
    private String descriptionStock;

    @Column(nullable = true)
    private String photo;

    @ManyToOne
    @JoinColumn(name = "idZoneProduction")
    private ZoneProduction zoneProduction;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String formeProduit;

    @Column(nullable = true)
    private String dateModif;
    
    @Column(nullable=true)
    private String personneModif;

    @Column(nullable=true)
    private String pays;
    
    @Column(nullable = false)
    private boolean statutSotck = true;

    @Column(nullable = true)
    private int nbreView = 0;

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
    private List<DetailCommande> detailCommandes;

    // @OneToMany(mappedBy = "stock")
    // private List<Rating> ratings;

    @ManyToOne
    @JoinColumn( name = "idMonnaie")
    private  Monnaie monnaie;
   
}


