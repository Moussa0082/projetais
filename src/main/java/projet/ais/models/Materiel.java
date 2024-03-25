package projet.ais.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.Data;

@Data
@Entity
public class Materiel {
    
    @Id
    private String idMateriel;

    @Column(nullable = false)
    private String codeMateriel;

//     @Convert(converter = MapToJsonConverter.class)
//    @Column(columnDefinition = "json")
//    private Map<Integer, Integer> prixParHeure;

    @Column(nullable = false)
    private int prixParHeure;

    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = true)
    private String photoMateriel;
    
    @Column(nullable = false)
    private String etatMateriel;

    @Column(nullable = false)
    private String localisation;

    @Column(nullable=true)
    private String personneModif;
    
    @Column
    private boolean statut = true;

    @Column
    private boolean statutCommande = false;


    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    @ManyToOne
    @JoinColumn(name = "idActeur")
    private Acteur acteur;
    
    @ManyToOne
    @JoinColumn(name = "idTypeMateriel")
    private TypeMateriel typeMateriel;

    @ManyToMany()
    @JoinTable(name = "materiel_commande",
        joinColumns = @JoinColumn(name = "id_materiel"),
        inverseJoinColumns = @JoinColumn(name = "id_commande"))
    @JsonIgnore
    private List<Commande> commandes;

}
