package projet.ais.models;
import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class RegroupementParametre {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idRegroupement;

    @Column(nullable = false)
    private String parametreRegroupe;

     @Column(nullable=true)
    private Date dateAjout;

    @Column(nullable=true)
    private Date dateModif;

    @Column(nullable = false)
    private boolean statutRegroupement = true;

    @OneToOne
    private ParametreFiche parametreFiche;
}
