package projet.ais.models;

import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Commande {

    @Id
    private String IdCommande;

    @Column(nullable = false)
    private String codeCommande;
    
    @Column(nullable = false)
    private String DescriptionCommande;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateCommande;

    @Column(nullable = false)
    private String codeAcheteur;
    
    @ManyToOne
    @JoinColumn(name = "idActeur")
    private Acteur acteur;
    
    
}
