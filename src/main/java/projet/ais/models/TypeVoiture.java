package projet.ais.models;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "type_voiture")
public class TypeVoiture {
    
    @Id
    private String idTypeVoiture;

    @Column(nullable = false)
    private String codeTypeVoiture;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = true)
    private int nombreSieges;

    @Column(nullable = true)
   private String description;

    @Column(nullable = true)
    private String dateAjout;
 
    @Column(nullable = true)
    private String dateModif;

    @Column(nullable = false)
    private boolean statutType = true;

    @OneToMany
    @JsonIgnore
    private List<Vehicule> vehicules;

    @ManyToOne
    @JoinColumn( name = "idActeur")
    private Acteur acteur;
}
