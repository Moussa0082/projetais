package projet.ais.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Alertes {
    
     @Id
    private String idAlerte;

    @Column(nullable = true)
    private String codeAlerte;

    @Column(nullable = false)
    private String titreAlerte;

    @Column(nullable = true)
    private String videoAlerte;

    @Column(nullable = true)
    private String photoAlerte;

    @Column(nullable = true)
    private String codePays;

    @Column(nullable = true)
    private String dateAjout;
 
    @Column(nullable = true)
    private String dateModif;

    @Column(nullable=true)
    private String personneModif;

    @Column(nullable = true)
    private String descriptionAlerte;

    @Column(nullable = true)
    private String audioAlerte;
    
    
    @Column
   private boolean statutAlerte = true;

    @Column
    private String pays;

}
