package projet.ais.models;

import java.util.Date;

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

    @Column(nullable=true)
    private Date dateAjout;

    @Column(nullable=true)
    private Date dateModif;

    @Column(nullable=true)
    private String personneAjout;

    @Column(nullable = false)
    private String valeurConditionRenvoi;

    @Column(nullable = false)
    private String descriptionRenvoie;

    @Column(nullable = false)
    private boolean statutRenvoie = true;

    @OneToOne
    private ParametreFiche parametreFiche;
}
