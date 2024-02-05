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

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateAjout;

    @PrePersist
    public void prePersist() {
        dateAjout = LocalDateTime.now();
    }


    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateModif;

    public LocalDateTime updateDateModif(LocalDateTime dateModif) {
        this.dateModif = dateModif;
        return dateModif;
    }

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
