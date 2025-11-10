package projet.ais.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Forme {
    
    @Id
    private String idForme;

    @Schema(required = true)
@Column(nullable = false)
    private String codeForme;

    @Schema(required = true)
@Column(nullable = false)
    private String libelleForme;

    @Column(nullable = true ,columnDefinition = "TEXT")
    private String descriptionForme;

    @Schema(required = true)
@Column(nullable = false)
    private boolean statutForme = true;
    
    @Column(nullable = true)
    private String dateAjout;

    @Schema(required = true)
@Column(nullable = false)
    private boolean hasAssociation = false;

    @OneToMany
    (mappedBy = "forme")
    @JsonIgnore
    private List<Intrant> intrants;
}
