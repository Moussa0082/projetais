package projet.ais.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class RenvoieParametre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRenvoiParametre;

    @Column(nullable = false)
    private String conditionRenvoi;

    @Column(nullable = false)
    private String valeurConditionRenvoi;

    @Column(nullable = false)
    private String descriptionRenvoie;

    @OneToOne
    private ParametreFicheDonnees parametreFicheDonnees;
}
