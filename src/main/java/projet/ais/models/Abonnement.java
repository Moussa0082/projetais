package projet.ais.models;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Abonnement {
    
    @Id
    private String idAbonnement;

    @Column(nullable = false)
    private String codeAbonnement;

    @Column(nullable = false)
    private String typeAbonnement;
   
    @Column(nullable = false)
    private String modePaiement;

    @Column(nullable = true)
    private LocalDate dateAjout;

    @Column(nullable = true)
    private LocalDate dateFin;

    @Column(nullable = true)
    private int montant;

    private Boolean statutAbonnement = false ;

    @Column(nullable = false)
    private List<String> options;

    @ManyToOne
    @JoinColumn( name = "idActeur")
    private  Acteur acteur;

}
