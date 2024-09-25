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

    @OneToMany
    (mappedBy = "categorieProduit")
    @JsonIgnore
    private List<Intrant> intrants;

    // @OneToMany
    // (mappedBy = "categorieProduit")
    // private List<Magasin> magasin;



    // @ManyToOne
    // @JoinColumn(name = "idActeur")
    // private Acteur acteur;

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

