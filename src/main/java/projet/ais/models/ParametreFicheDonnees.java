package projet.ais.models;


import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ParametreFicheDonnees {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idParametre;

    @Column(nullable = false)
    private String classeParametre;

    @Column(nullable = false)
    private String champParametre;

    @Column(nullable = false)
    private String codeParametre;


    @Column(nullable = false)
    private String libelle;

    @Column(nullable = false)
    private String typeDonnee;

    //    Liste à determiner
    @Column(nullable = false)
    private String listeDonnee;

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
    private String  critereChamp;

    // @OneToOne
    // private RegroupementParametre regroupementParametre;

    // @OneToOne
    // private RenvoieParametre renvoieParametre;
}

