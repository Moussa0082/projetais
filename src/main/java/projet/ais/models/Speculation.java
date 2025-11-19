package projet.ais.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Data
public class Speculation {
    
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idSpeculation;
 
    @Column(nullable = true)
    private String codeSpeculation;

    @Schema(required = true)
@Column(nullable = false)
    private String nomSpeculation;

    @Column(nullable = true ,columnDefinition = "TEXT")
    private String descriptionSpeculation;

    @Schema(required = true)
@Column(nullable = false)
    private boolean statutSpeculation = true;

    @ManyToOne
    // @JsonIgnore
    @JoinColumn(name = "idCategorieProduit")
    private CategorieProduit categorieProduit;

    @Column(nullable = false)
    private boolean hasAssociation = false;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    @Column(nullable=true)
    private String personneModif; 
    
    @OneToMany
    (mappedBy = "speculation")
    @JsonIgnore
    private List<Stock> stockList;
    

    @ManyToMany(mappedBy = "speculation")
    @JsonIgnore
    private List<Acteur> acteur;

    
    @OneToMany
    (mappedBy = "speculation")
    @JsonIgnore
    private List<Superficie> superficies;

    public  Speculation(){}

    public Speculation(String idSpeculation,String codeSpeculation,String nomSpeculation,String descriptionSpeculation,boolean statutSpeculation, CategorieProduit categorieProduit,String dateAjout, String dateModif,String personneModif){
        this.idSpeculation = idSpeculation;
        this.codeSpeculation = codeSpeculation;
        this.nomSpeculation = nomSpeculation;
        this.descriptionSpeculation=descriptionSpeculation;
        this.categorieProduit = categorieProduit;
        this.statutSpeculation = statutSpeculation;
        this.dateAjout = dateAjout;
        this.dateModif = dateModif;
    }
}

