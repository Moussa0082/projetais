package projet.ais.models;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Campagne {
    
    @Id
    private String idCampagne;

    @Schema(required = true)
@Column(nullable = false)
    private String codeCampagne;
   
    @Schema(required = true)
@Column(nullable = false)
    private String nomCampagne;

    @Column(nullable = true ,columnDefinition = "TEXT")
    private String description;

    @Column(nullable=true)
    private String personneModif;

    @Schema(required = true)
@Column(nullable = false)
    private boolean statutCampagne = true;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;
    
    @Column(nullable = true)
    private boolean hasAssociation = false;

    @OneToMany(mappedBy = "campagne")
    @JsonIgnore
    private List<Superficie> superficie;

    @ManyToOne
    private Acteur acteur;
}
