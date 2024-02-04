package projet.ais.models;



import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
public class ParametreGeneraux {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idParametreGeneraux;

    @Column(nullable = false)
    private String sigleStructure;

    @Column(nullable = false)
    private String nomStructure;

    @Column(nullable = false)
    private String sigleSysteme;

    @Column(nullable = false)
    private String nomSysteme;

    @Column(nullable = false)
    private String descriptionSysteme;

    @Column(nullable = false)
    private String sloganSysteme;

    @Column(nullable = true)
    private String logoSysteme;

    @Column(nullable = false)
    private String adresseStructure;

    @Column(nullable = false)
    private String emailStructure;

    @Column(nullable = false)
    private String telephoneStructure;

    @Column(nullable = false)
    private String whattsAppStructure;

    @Column(nullable = false)
    private String libelleNiveau1Pays;

    @Column(nullable = false)
    private String libelleNiveau2Pays;

    @Column(nullable = false)
    private String libelleNiveau3Pays;

    @Column(nullable = false)
    private String codeNiveauStructure;

    @Column(nullable = false)
    private String localiteStructure;

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

}
