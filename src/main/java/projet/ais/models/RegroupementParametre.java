package projet.ais.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class RegroupementParametre {
    
     @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idRegroupementParametre;

    @Column(nullable = false)
    private String libelleRegroupement;

    @Column(nullable = false)
    private String description;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    @Column(nullable = false)
    private boolean statutRegroupement = true;

    @OneToMany(mappedBy = "regroupementParametre", cascade = CascadeType.ALL)
    private List<ParametreFiche> parametreFiche;
}
