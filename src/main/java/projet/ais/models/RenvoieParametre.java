package projet.ais.models;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class RenvoieParametre {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idRenvoiParametre;

    @Column(nullable = false)
    private String conditionRenvoi;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    @Column(nullable = true)
    private String personneModif;

    @Column(nullable = false)
    private String valeurConditionRenvoi;

    @Column(nullable = false)
    private String descriptionRenvoie;

    @Column(nullable = false)
    private boolean statutRenvoie = true;

    @OneToOne
    private ParametreFiche parametreFiche;
}
