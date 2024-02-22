package projet.ais.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
public class Pays {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idPays;

    @Column(nullable = false)
    private String codePays;

    @Column(nullable = false)
    private String nomPays;

    @Column(nullable = false)
    private String descriptionPays;

    @Column(nullable = true)
    private String personneModif;

    @Column(nullable = true)
    private boolean statutPays = true;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    @OneToMany
    (mappedBy = "pays")
    @JsonIgnore
    private List<Niveau1Pays> niveau1PaysList;

    @ManyToOne
    @JoinColumn( name = "idSousRegion")
    private  SousRegion sousRegion;

}
