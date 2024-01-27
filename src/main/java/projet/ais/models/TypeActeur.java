package projet.ais.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.*;

@Entity
@Data
public class TypeActeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTypeActeur;



    @Column(nullable = false)
    private String libelle;
    
    @Column(nullable = false)
    private String codeTypeActeur;

    @Column(nullable = false)
    private String descriptionTypeActeur;

    @Column(nullable=true)
    private Date dateAjout;

    @Column(nullable=true)
    private Date dateModif;

    @OneToMany
    (mappedBy = "typeActeur")
    @JsonIgnore
    private List<Acteur> acteurList;

}

