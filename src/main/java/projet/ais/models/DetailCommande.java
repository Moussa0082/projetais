package projet.ais.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Data;

@Entity
@Data

public class DetailCommande {

    @Id
    private String idDetailCommande;

    @Column(nullable = true)
    private String codeProduit;
    
    @Column(nullable = false)
    private Double quantiteDemande;
    
    @Column(nullable = true)
    private Double quantiteLivree;

    @Column(nullable = true)
    private Double quantiteNonLivree; // Utiliser la classe d'enveloppe Double au lieu du type primitif double

    @Column(nullable = true)
    private String nomProduit;

    private String dateAjout;
    

    // @Column(columnDefinition = "TIMESTAMP")
    // private LocalDateTime dateDetailCommande;

    @ManyToOne
    @JoinColumn(name = "idCommande")
    private Commande commande;

}
