package projet.ais.models;

import jakarta.persistence.*;

@Entity
public class RenvoieParametre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRenvoi;

    @Column(nullable = false)
    private String parametreConcerne;

    @Column(nullable = false)
    private String conditionRenvoi;

    @Column(nullable = false)
    private String valeurConditionRenvoi;

    @Column(nullable = false)
    private String descriptionRenvoie;

    @OneToOne
    private ParametreFicheDonnees parametreFicheDonnees;
}
