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
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idStock;

    @Column(nullable = true)
    private String codeStock;

    @Column(nullable = false)
    private String nomProduit;

    @Column(nullable = false)
    private String formeProduit;

    @Column(nullable = false)
    private LocalDateTime dateProduction;

    @Column(nullable = false)
    private double quantiteStock;

    @Column(nullable = false)
    private String typeProduit;

    // @Column(nullable = false)
    // private String siteProduction;

    @Column(nullable = false)
    private String descriptionStock;

    @Column(nullable = true)
    private String photo;

    @ManyToOne
    @JoinColumn(name = "idZoneProduction")
    private ZoneProduction zoneProduction;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateAjout;

    @PrePersist
    public void prePersist() {
        dateAjout = LocalDateTime.now();
        dateProduction = LocalDateTime.now();
    }


    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateModif;

    public LocalDateTime updateDateModif(LocalDateTime dateModif) {
        this.dateModif = dateModif;
        return dateModif;
    }
    
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
    
    @ManyToOne
    @JoinColumn(name = "idCommande")
    private Commande commande;

    @OneToMany
    (mappedBy = "stock")
    @JsonIgnore
    private List<Sortie_Stock> sortie_Stock;



}


