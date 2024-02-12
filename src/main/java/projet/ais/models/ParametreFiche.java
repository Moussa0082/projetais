package projet.ais.models;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import lombok.Data;
import java.util.List;


@Entity
@Data
public class ParametreFiche {
    
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idParametreFiche;

    @Column(nullable = false)
    private String classeParametre;

    @Column(nullable = false)
    private String champParametre;

    @Column(nullable = false)
    private String codeParametre;

    @Column(nullable = false)
    private String libelleParametre;

    @Column(nullable = false)
    private String typeDonneeParametre;

    @Column(nullable = false)
    private List<String> listeDonneeParametre;

    @Column(nullable = false)
    private int valeurMax;

    @Column(nullable = true)
    private String personneModif;

    @Column(nullable = false)
    private int valeurMin;

    @Column(nullable = false)
    private int valeurObligatoire;

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
    private String critereChampParametre;

    @Column(nullable = false)
    private boolean statutParametre = true;

}
