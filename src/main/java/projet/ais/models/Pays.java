package projet.ais.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Pays {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPays;

    @Column(nullable = false)
    private String codePays;

    @Column(nullable = false)
    private String nomPays;

    @Column(nullable = false)
    private String descriptionPays;

    @Column(nullable = false)
    private String statutPays;

    @OneToMany
    (mappedBy = "pays")
    @JsonIgnore
    private List<Niveau1Pays> niveau1PaysList;

    @ManyToOne
    @JoinColumn( name = "idSousRegion")
    private  SousRegion sousRegion;
}
