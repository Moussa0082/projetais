package projet.ais.models;
import jakarta.persistence.*;

@Entity
public class RegroupementParametre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRegroupement;

    @Column(nullable = false)
    private String parametreRegroupe;

    @OneToOne
    private ParametreFicheDonnees parametreFicheDonnees;
}
