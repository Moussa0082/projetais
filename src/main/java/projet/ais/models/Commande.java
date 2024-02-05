package projet.ais.models;

import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
    
    @Column(nullable = false)
    private String descriptionCommande;
    
    @Column(nullable = false)
    private boolean statutCommande = false;


    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateCommande;


    @Column(nullable = false)
    private String codeAcheteur;

     @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateAjout;

    @PrePersist
    public void prePersist() {
        dateAjout = LocalDateTime.now();
        this.dateCommande = LocalDateTime.now();
    }


    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateModif;

    public LocalDateTime updateDateModif(LocalDateTime dateModif) {
        this.dateModif = dateModif;
        return dateModif;
    }
    
    
    @ManyToOne
    @JoinColumn(name = "idActeur")
    private Acteur acteur;

    @OneToMany
    (mappedBy = "commande")
    private List<Stock> stockList; 
    
    @OneToMany
    (mappedBy = "commande")
    @JsonIgnore
    private List<DetailCommande> detailCommandeList; 

}
