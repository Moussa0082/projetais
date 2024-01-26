package projet.ais.models;
import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class RegroupementParametre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRegroupement;

    @Column(nullable = false)
    private String parametreRegroupe;

     @Column(nullable=false)
    private Date dateAjout;

    @Column(nullable=false)
    private Date dateModif;
    
    @OneToOne
    private ParametreFicheDonnees parametreFicheDonnees;
}
