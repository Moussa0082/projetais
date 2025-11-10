package projet.ais.models;



import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Data
public class ParametreGeneraux {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idParametreGeneraux;

    @Schema(required = true)
@Column(nullable = false)
    private String sigleStructure;

    @Schema(required = true)
@Column(nullable = false)
    private String nomStructure;

    @Schema(required = true)
@Column(nullable = false)
    private String sigleSysteme;

    @Schema(required = true)
@Column(nullable = false)
    private String nomSysteme;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String descriptionSysteme;

    @Schema(required = true)
@Column(nullable = false)
    private String sloganSysteme;

    @Column(nullable = true)
    private String logoSysteme;

    @Schema(required = true)
@Column(nullable = false)
    private String adresseStructure;

    @Column(nullable = true)
    private String personneModif;

    @Schema(required = true)
@Column(nullable = false)
    private String emailStructure;

    @Schema(required = true)
@Column(nullable = false)
    private String telephoneStructure;

    @Column(nullable = true)
    private String whattsAppStructure;

    // @Column(nullable = true)
    // private String libelleNiveau1Pays;

    // @Column(nullable = true)
    // private String libelleNiveau2Pays;

    // @Column(nullable = true)
    // private String libelleNiveau3Pays;

    @Schema(required = true)
@Column(nullable = false)
    private String codeNiveauStructure;

    @Schema(required = true)
@Column(nullable = false)
    private String localiteStructure;

    // @Column(nullable = true)
    // private String monnaie;

    // @Column(nullable = true)
    // private String tauxDollar;

    // @Column(nullable = true)
    // private String tauxYuan;

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
