package projet.ais.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
// @AllArgsConstructor
public class TypeActeur {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idTypeActeur;

    @Column(nullable = false)
    private String libelle;
    
    @Column(nullable = false)
    private String codeTypeActeur;

    @Column(nullable = true)
    private boolean statutTypeActeur;

    @Column(nullable = false)
    private String descriptionTypeActeur;

    @Column(nullable = true)
    private String personneModif;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;
    // @Column(columnDefinition = "TIMESTAMP")
    // private LocalDateTime dateAjout;

    // @PrePersist
    // public void prePersist() {
    //     dateAjout = LocalDateTime.now();
    // }

    // @Column(columnDefinition = "TIMESTAMP")
    // private LocalDateTime dateModif;


    @ManyToMany(mappedBy = "typeActeur", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Acteur> acteur;


    public  TypeActeur(){}

    
       // Constructeur prenant les champs nécessaires
    public TypeActeur(String  idTypeActeur, String libelle, String codeTypeActeur, boolean statutTypeActeur, String descriptionTypeActeur, String dateAjout, String dateModif) {
        this.idTypeActeur = idTypeActeur;
        this.libelle = libelle;
        this.codeTypeActeur = codeTypeActeur;
        this.statutTypeActeur = statutTypeActeur;
        this.descriptionTypeActeur = descriptionTypeActeur;
        this.dateAjout = dateAjout;
        this.dateModif = dateModif;
    }

    
}

