package projet.ais.models;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import lombok.Data;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Data
public class ParametreFiche {
    
    @Id
    private String idParametreFiche;

    @Schema(required = true)
@Column(nullable = false)
    private String classeParametre;

    @Schema(required = true)
@Column(nullable = false)
    private String champParametre;

    @Schema(required = true)
@Column(nullable = false)
    private String codeParametre;

    @Schema(required = true)
@Column(nullable = false)
    private String libelleParametre;

    @Schema(required = true)
@Column(nullable = false)
    private String typeDonneeParametre;

    @Schema(required = true)
@Column(nullable = false)
    private List<String> listeDonneeParametre;

    @Schema(required = true)
@Column(nullable = false)
    private int valeurMax;

    @Column(nullable = true)
    private String personneModif;

    @Schema(required = true)
@Column(nullable = false)
    private int valeurMin;

    @Schema(required = true)
@Column(nullable = false)
    private int valeurObligatoire;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    @Schema(required = true)
@Column(nullable = false)
    private String critereChampParametre;

    @Schema(required = true)
@Column(nullable = false)
    private boolean statutParametre = true;

    @ManyToMany(mappedBy = "parametreFiche")
    @JsonIgnore
    private List<RegroupementParametre> regroupementParametre;
}
