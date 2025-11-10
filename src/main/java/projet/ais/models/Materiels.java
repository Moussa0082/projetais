package projet.ais.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import jakarta.persistence.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Data
public class Materiels {
    
    @Id
    private String idMateriel;

    @Schema(required = true)
@Column(nullable = false)
    private String codeMateriel;

    @Schema(required = true)
@Column(nullable = false)
    private int prixParHeure;

    @Schema(required = true)
@Column(nullable = false)
    private String nom;
    
    @Column(nullable = true,columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = true)
    private String photoMateriel;
    
    @Schema(required = true)
@Column(nullable = false)
    private String etatMateriel;

    @Schema(required = true)
@Column(nullable = false)
    private String localisation;

    @Schema(required = true)
@Column(nullable = false)
    private boolean hasAssociation = false;
    
    @Column(nullable=true)
    private String personneModif;
    
    @Column
    private boolean statut = true;

    @Column
    private boolean statutCommande = false;

    @Column(nullable=true)
    private String pays;


    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    @Schema(required = true)
@Column(nullable = false)
    private int nbreView = 0;

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

    @ManyToOne
    @JoinColumn( name = "idMonnaie")
    private  Monnaie monnaie;

    @ManyToOne
    @JoinColumn(name = "idSpeculation")
    private Speculation speculation;
}
