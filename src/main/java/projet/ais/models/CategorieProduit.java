package projet.ais.models;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class CategorieProduit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCategorieProduit;

    @Column(nullable = false)
    private String codeCategorie;

    @Column(nullable = false)
    private String libelleCategorie;

    @Column(nullable = false)
    private String descriptionCategorie;

    @Column(nullable = false)
    private String statutCategorie;

//    @Column(nullable = false)
//    private String filiereCategorie;

    @OneToMany
    (mappedBy = "categorieProduit")
    @JsonIgnore
    private List<Speculation> speculationList;

    @ManyToOne
    @JoinColumn(name = "idFiliere")
    private Filiere filiere;
}

