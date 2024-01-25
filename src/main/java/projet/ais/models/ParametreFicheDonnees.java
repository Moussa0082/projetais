package projet.ais.models;


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
    private String libelleParametre;

    @Column(nullable = false)
    private String typeDonneeParametre;

    //    Liste à determiner
    @Column(nullable = false)
    private String ListeDonneesParametre;

    @Column
    private int valeurMaxParametre;

    @Column
    private int valeurMinParametre;

    @Column(nullable = false)
    private int valeurObligatoireParametre;

    @Column
    private String  critereChampParametre;

    @OneToOne
    private RegroupementParametre regroupementParametre;

    @OneToOne
    private RenvoieParametre renvoieParametre;
}

