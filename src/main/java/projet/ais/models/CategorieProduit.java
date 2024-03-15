package projet.ais.models;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class CategorieProduit {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idCategorieProduit;

    @Column(nullable = false)
    private String codeCategorie;

    @Column(nullable = false)
    private String libelleCategorie;

    @Column(nullable = false)
    private String descriptionCategorie;

    @Column(nullable = false)
    private boolean statutCategorie = true;
    
   

    @Column(nullable=true)
    private String personneModif;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    @OneToMany
    (mappedBy = "categorieProduit")
    @JsonIgnore
    private List<Speculation> speculationList;

    @ManyToOne
    @JoinColumn(name = "idFiliere")
    private Filiere filiere;

    // @OneToMany
    // (mappedBy = "categorieProduit")
    // private List<Magasin> magasin;



    // @ManyToOne
    // @JoinColumn(name = "idActeur")
    // private Acteur acteur;
}

