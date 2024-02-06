package projet.ais.models;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Campagne {
    
    @Id
    private String idCampagne;

    @Column(nullable = false)
    private String codeCampagne;
   
    @Column(nullable = false)
    private String nomCampagne;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean statutCampagne = true;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateAjout;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateModif;
    
    @OneToMany(mappedBy = "campagne")
    @JsonIgnore
    private List<Superficie> superficie;
}