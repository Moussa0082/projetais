package projet.ais.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Conseil {

    @Id
    private String idConseil;

    @Column(nullable = true)
    private String codeConseil;

    @Column(nullable = false)
    private String titreConseil;

    @Column(nullable = true)
    private String videoConseil;

    @Column(nullable = true)
    private String photoConseil;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateAjout;


    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateModif;

    @Column(nullable=true)
    private String personneModif;

    @Column(nullable = true)
    private String descriptionConseil;

    @Column(nullable = true)
    private String audioConseil;
    
    
    @Column
    private boolean statutConseil;

     @ManyToOne
     @JoinColumn(name = "idActeur")
     private Acteur acteur;
    
}
