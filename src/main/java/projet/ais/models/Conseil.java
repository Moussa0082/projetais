package projet.ais.models;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Data;

@Entity
@Data
public class Conseil {

    @Id
    private String idConseil;

    @Column(nullable = true)
    private String codeConseil;

    @Schema(required = true)
@Column(nullable = false)
    private String titreConseil;

    @Column(nullable = true)
    private String videoConseil;

    @Column(nullable = true)
    private String photoConseil;

    @Column(nullable = true)
    private String dateAjout;
 
    @Column(nullable = true)
    private String dateModif;

    @Column(nullable=true)
    private String personneModif;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String descriptionConseil;

    @Column(nullable = true)
    private String audioConseil;
    
    
    @Column
    private boolean statutConseil = true;


     @ManyToOne
     @JoinColumn(name = "idActeur")
     private Acteur acteur;
    
}
