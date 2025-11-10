package projet.ais.models;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(required = true)
@Column(nullable = false)
    private String codeDevice;

    @Schema(required = true)
@Column(nullable = false)
    private String nomDevice;

    @Schema(required = true)
@Column(nullable = false)
    private String sigle;
    
    @Schema(required = true)
@Column(nullable = false)
    private double taux;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    @Schema(required = true)
@Column(nullable = false)
    private boolean statut = true;


    @ManyToOne
    @JoinColumn( name = "idMonnaie")
    private  Monnaie monnaie;
}
