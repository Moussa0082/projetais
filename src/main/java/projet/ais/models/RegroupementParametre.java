package projet.ais.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Data
public class RegroupementParametre {
    
     @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idRegroupementParametre;

    @Schema(required = true)
@Column(nullable = false)
    private String libelleRegroupement;

    @Schema(required = true)
@Column(nullable = false)
    private String description;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    @Schema(required = true)
@Column(nullable = false)
    private boolean statutRegroupement = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "regroupement_parametre_parametre_fiche",
        joinColumns = @JoinColumn(name = "id_regroupement_parametre"),
        inverseJoinColumns = @JoinColumn(name = "id_parametre_fiche"))
    private List<ParametreFiche> parametreFiche;
}
