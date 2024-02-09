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

import lombok.Data;

@Entity
@Data
public class Commande {

    @Id
    private String idCommande;

    @Column(nullable = false)
    private String codeCommande;
    
    @Column(nullable = true)
    private String descriptionCommande;
    
    @Column(nullable = false)
    private boolean statutCommande = false;


    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateCommande;

    @Column(nullable = true)
    private String codeProduit;
    
    @Column(nullable = false)
    private double quantiteDemande;
    
    @Column(nullable = false)
    private double quantiteLivree;

    //Ajouter
    @Column(nullable = true)
    private double quantiteNonLivree;

    @Column(nullable = true)
    private String nomProduit;


    @Column(nullable = true)
    private String codeAcheteur;

     @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateAjout;

    @PrePersist
    public void prePersist() {
        dateAjout = LocalDateTime.now();
        dateCommande = LocalDateTime.now();
    }


    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateModif;

    public LocalDateTime updateDateModif(LocalDateTime dateModif) {
        this.dateModif = dateModif;
        return dateModif;
    }
    
    
    @ManyToOne
    @JoinColumn(name = "idActeur")
    // @JsonIgnore
    private Acteur acteur;


    @OneToMany
    // @JsonIgnore
    (mappedBy = "commande")
    private List<Stock> stock; 

    
    @OneToMany
    (mappedBy = "commande")
    @JsonIgnore
    private List<DetailCommande> detailCommandeList;

}
