package projet.ais.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.*;

@Entity
@Data
public class Niveau1Pays {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idNiveau1Pays;

    @Schema(required = true)
@Column(nullable = false)
    private String codeN1;

    @Schema(required = true)
@Column(nullable = false)
    private String nomN1;

    @Column(nullable = true ,columnDefinition = "TEXT")
    private String descriptionN1;

    @Schema(required = true)
@Column(nullable = false)
    private boolean statutN1 = true;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    @Schema(required = true)
@Column(nullable = false)
    private boolean hasAssociation = false;

    @ManyToOne
    @JoinColumn( name = "idPays")
    private Pays pays;

    @OneToMany
    (mappedBy = "niveau1Pays")
    @JsonIgnore
    private List<Niveau2Pays> niveau2PaysList;

    @OneToMany
    (mappedBy = "niveau1Pays", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Magasin> magasin;

}

