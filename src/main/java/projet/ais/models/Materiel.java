package projet.ais.models;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Materiel {
    
    @Id
    private String idMateriel;

    @Column(nullable = false)
    private String codeMateriel;

    @Column(nullable = false)
    private int prix;

    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private String photoMateriel;
    
    @Column(nullable = false)
    private String EtatMateriel;

    @Column(nullable = false)
    private String localisation;

    @Column
    private boolean statut = true;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateAjout;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateModif;

    @ManyToOne
    @JoinColumn(name = "idActeur")
    private Acteur acteur;

    @ManyToMany
    @JsonIgnore
    private List<CommandeMateriel> commandes;

}
