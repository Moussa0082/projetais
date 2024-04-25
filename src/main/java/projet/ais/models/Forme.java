package projet.ais.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @Column(nullable = false)
    private String codeForme;

    @Column(nullable = false)
    private String libelleForme;

    @Column(nullable = false)
    private String descriptionForme;

    @Column(nullable = false)
    private boolean statutForme = true;
    
    @Column(nullable = true)
    private String dateAjout;

    @OneToMany
    (mappedBy = "forme")
    @JsonIgnore
    private List<Stock> stocks;

    @OneToMany
    (mappedBy = "forme")
    @JsonIgnore
    private List<Intrant> intrants;
}
