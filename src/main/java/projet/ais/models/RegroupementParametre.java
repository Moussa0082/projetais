package projet.ais.models;
import java.time.LocalDateTime;
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

    @Column(nullable = false)
    private String libelle;


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

    @Column(nullable = false)
    private boolean statutRegroupement = true;

    @Column(nullable = true)
    private String personneModif;

    @OneToOne
    private ParametreFiche parametreFiche;
}
