package projet.ais.models;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Superficie {
    
    @Id
    private String idSuperficie;

    @Column(nullable = false)
    private String codeSuperficie;

   

    @Column(nullable = false)
    private String localite;

    @Column(nullable=true)
    private String personneModif;
    
    @Column(nullable = false)
    private String superficieHa;

    @Column(nullable = false)
    private boolean statutSuperficie = true;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateSemi;

    // @Column(columnDefinition = "TIMESTAMP")
    // private LocalDateTime dateAjout;

    // @Column(columnDefinition = "TIMESTAMP")
    // private LocalDateTime dateModif;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    @ManyToOne
    private Acteur acteur;

    @Column(nullable = false)
    private List<String> intrants;


    @ManyToOne
    @JoinColumn(name = "idSpeculation")
    private Speculation speculation;

    @ManyToOne
    @JoinColumn(name = "idCampagne")
    private Campagne campagne;
}
