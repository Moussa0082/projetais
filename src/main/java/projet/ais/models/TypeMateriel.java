package projet.ais.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name = "type_materiel")
public class TypeMateriel {
    
     @Id
    private String idTypeMateriel;

    @Column(nullable = false)
    private String codeTypeMateriel;

    @Column(nullable = false)
    private String nom;

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
    private List<Materiel> materiels;
}
