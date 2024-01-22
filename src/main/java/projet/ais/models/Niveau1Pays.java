package projet.ais.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Niveau1Pays {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idNiveau1Pays;

    @Column(nullable = false)
    private String codeN1;

    @Column(nullable = false)
    private String nomN1;

    @Column(nullable = false)
    private String descriptionN1;

    @Column(nullable = false)
    private String statutN1;

    @ManyToOne
    @JoinColumn( name = "idPays")
    private Pays pays;

    @OneToMany
    (mappedBy = "niveau1Pays")
    @JsonIgnore
    private List<Niveau2Pays> niveau2PaysList;
}

