package projet.ais.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Data
public class Monnaie {
    
    @Id
    private String idMonnaie;

    @Schema(required = true)
@Column(nullable = false)
    private String codeMonnaie;

    @Schema(required = true)
@Column(nullable = false)
    private String libelle;

    @Schema(required = true)
@Column(nullable = false)
    private String sigle;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    @Schema(required = true)
@Column(nullable = false)
    private boolean statut = true;

    @Schema(required = true)
@Column(nullable = false)
    private boolean hasAssociation = false;

    @OneToMany(mappedBy = "monnaie")
    @JsonIgnore
    private List<Device> deviceList;

    @OneToMany(mappedBy = "monnaie")
    @JsonIgnore
    private List<Intrant> intrantsList;
    
    @OneToMany(mappedBy = "monnaie")
    @JsonIgnore
    private List<Stock> stocksList;
    
    @OneToMany(mappedBy = "monnaie")
    @JsonIgnore
    private List<Materiels> materielList;

    @OneToMany(mappedBy = "monnaie")
    @JsonIgnore
    private List<Vehicule> vehiculeList;
}
