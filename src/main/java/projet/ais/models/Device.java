package projet.ais.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Device {
    @Id
    private String idDevice;

    @Column(nullable = false)
    private String codeDevice;

    @Column(nullable = false)
    private String nomDevice;

    @Column(nullable = false)
    private String sigle;
    
    @Column(nullable = false)
    private double taux;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    @Column(nullable = false)
    private boolean statut = true;


    @ManyToOne
    @JoinColumn( name = "idMonnaie")
    private  Monnaie monnaie;
}
