package projet.ais.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ParametreFiche {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idParametreFiche;

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
    private String listeDonneeParametre;

    @Column(nullable = false)
    private int valeurMax;

    @Column(nullable = false)
    private int valeurMin;

    @Column(nullable = false)
    private int valeurObligatoire;

    @Column(nullable=false)
    private Date dateAjout;

    @Column(nullable=false)
    private Date dateModif;

    @Column(nullable = false)
    private String critereChampParametre;
}
