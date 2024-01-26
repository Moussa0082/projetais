package projet.ais.models;



import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Magasin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMagasin;

    @Column(nullable = false)
    private String codeMagasin;

    @Column(nullable = false)
    private String nomMagasin;
    // @Column(nullable = false)
    // private String codeActeur;

    @Column(nullable = false)
    private String niveau3PaysMagasin;

    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String longitude;

    @Column(nullable = false)
    private String localiteMagasin;

    @Column(nullable = false)
    private String contactMagasin;

    @Column(nullable=false)
    private Date dateAjout;

    @Column(nullable=false)
    private Date dateModif;

    @Column(nullable = true)
    private String photo;

    @ManyToOne
    @JoinColumn( name = "idActeur")
    private Acteur acteur;
}

