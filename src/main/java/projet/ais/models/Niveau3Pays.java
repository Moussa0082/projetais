package projet.ais.models;


import jakarta.persistence.*;
import lombok.Data;
import java.util.*;

@Entity
@Data
public class Niveau3Pays {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idNiveau3Pays;

    @Column(nullable = false)
    private String codeN3;

    @Column(nullable = false)
    private String nomN3;

    @Column(nullable = false)
    private String descriptionN3;

    @Column(nullable = false)
    private boolean statutN3 = true;

    @Column(nullable=true)
    private Date dateAjout;

    @Column(nullable=true)
    private Date dateModif;

    @ManyToOne
    @JoinColumn( name = "idNiveau2Pays") 
    private  Niveau2Pays niveau2Pays;
}

