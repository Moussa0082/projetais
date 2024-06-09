package projet.ais.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AlertesOffLine {
    
     @Id
    private String idAlerteOffLine;

    @Column(nullable = true)
    private String codeAlerteOffLine;

    @Column(nullable = false)
    private String titreAlerteOffLine;

    @Column(nullable = true)
    private String videoAlerteOffLine;

    @Column(nullable = true)
    private String photoAlerteOffLine;

    @Column(nullable = true)
    private String codePays;

    @Column(nullable = true)
    private String dateAjout;
 
    @Column(nullable = true)
    private String dateModif;

    @Column(nullable=true)
    private String personneModif;

    @Column(nullable = true)
    private String descriptionAlerteOffLine;

    @Column(nullable = true)
    private String audioAlerteOffLine;
    
    
    @Column
    private boolean statutAlerteOffLine = true;

    @Column
    private String pays;

}
