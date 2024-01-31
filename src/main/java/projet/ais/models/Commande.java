package projet.ais.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Commande {
    
    @Id
    private String idMateriel;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String adresse;

    @Column(nullable = false)
    private String telephone;

    @ManyToOne
    @JoinColumn(name = "idActeur")
    private Acteur acteur;

    @ManyToOne
    @JoinColumn(name = "idStock")
    private Stock stock;
}
