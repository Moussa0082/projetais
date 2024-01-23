package projet.ais.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class TypeActeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTypeActeur;

    @Column(nullable = false)
    private String codeTypeActeur;

    @Column(nullable = false)
    private String descriptionTypeActeur;

    @OneToMany
    (mappedBy = "typeActeur")
    @JsonIgnore
    private List<Acteur> acteurList;

}

