package projet.ais.models;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Entity
@Data
public class Commande {

    @Id
    private String idCommande;

    @Column(nullable = true)
    private String codeCommande;
    
    @Column(nullable = true)
    private String descriptionCommande;
    
    @Column(nullable = false)
    private Boolean statutCommande = false;

    @Column
    private boolean statutCommandeLivrer = false;

    @Column
    private boolean statutConfirmation = false;
    
    private String dateCommande;

    private boolean isProprietaire;

    // @Column(nullable = true)
    // private String codeProduit;
    
    @Column(nullable = true)
    private double quantiteDemande;
    
    // @Column(nullable = true)
    // private double quantiteLivree;

    //Ajouter
    // @Column(nullable = true)
    // private double quantiteNonLivree;

    @Column(nullable = true)
    private String nomProduit;


    @Column(nullable = true)
    private String codeAcheteur;

    private String dateModif;
    
    @ManyToOne
    @JoinColumn(name = "idActeur")
    // @JsonIgnore
    // @JsonProperty
    private Acteur acteur;

    @ManyToOne
    @JoinColumn(name = "acteurProprietaire")
    // @JsonIgnore
    // @JsonProperty
    private Acteur acteurProprietaire;


    @Column(nullable = true)
    private String personneModif;

    @ManyToMany
    @JoinTable(name = "commande_stock",
    joinColumns = @JoinColumn(name = "id_commande"),
    inverseJoinColumns = @JoinColumn(name = "id_stock"))
    @JsonIgnore
    private List<Stock> stock;

    @ManyToMany
    @JoinTable(name = "commande_intrant",
        joinColumns = @JoinColumn(name = "id_commande"),
        inverseJoinColumns = @JoinColumn(name = "id_intrant"))
        @JsonIgnore
    private List<Intrant> intrant;

    
    @ManyToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Materiels> materielList;

    @OneToMany
    (mappedBy = "commande")
    @JsonIgnore
    private List<DetailCommande> detailCommandeList;

    public Commande() {}

}
