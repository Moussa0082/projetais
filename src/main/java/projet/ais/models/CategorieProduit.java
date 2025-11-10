package projet.ais.models;



import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
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
    private String idCategorieProduit;

    @Schema(required = true)
@Column(nullable = false)
    private String codeCategorie;

    @Schema(required = true)
@Column(nullable = false)
    private String libelleCategorie;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String descriptionCategorie;

    @Schema(required = true)
@Column(nullable = false)
    private boolean statutCategorie = true;

    @Column(nullable=true)
    private String personneModif;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    @Schema(required = true)
@Column(nullable = false)
    private boolean hasAssociation = false;

    @OneToMany
    (mappedBy = "categorieProduit")
    @JsonIgnore
    private List<Speculation> speculationList;
    
    @ManyToOne
    @JoinColumn(name = "idFiliere")
    private Filiere filiere;

    @OneToMany
    (mappedBy = "categorieProduit")
    @JsonIgnore
    private List<Intrant> intrants;

    public CategorieProduit() {
        // Constructeur par défaut
    }
    
    public CategorieProduit(String idCategorieProduit, String codeCategorie, String libelleCategorie, 
                        String descriptionCategorie, boolean statutCategorie, String personneModif, 
                        String dateAjout, String dateModif, Filiere filiere, List<Speculation> speculationList, 
                        List<Intrant> intrants) {
    this.idCategorieProduit = idCategorieProduit;
    this.codeCategorie = codeCategorie;
    this.libelleCategorie = libelleCategorie;
    this.descriptionCategorie = descriptionCategorie;
    this.statutCategorie = statutCategorie;
    this.personneModif = personneModif;
    this.dateAjout = dateAjout;
    this.dateModif = dateModif;
    this.filiere = filiere;
    this.speculationList = speculationList;
    this.intrants = intrants;
}

}

