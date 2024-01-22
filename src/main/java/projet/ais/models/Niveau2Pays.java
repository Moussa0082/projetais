package projet.ais.models;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Niveau2Pays {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idNiveau2Pays;

    @Column(nullable = false)
    private String codeN2;

    @Column(nullable = false)
    private String nomN2;

    @Column(nullable = false)
    private String descriptionN2;

    @Column(nullable = false)
    private String statutN2;

    @ManyToOne
    @JoinColumn( name = "idNiveau1Pays")
    private Niveau1Pays niveau1Pays;

    @OneToMany
    (mappedBy = "niveau2Pays")
    @JsonIgnore
    private List<Niveau3Pays> niveau3PaysList;

}
