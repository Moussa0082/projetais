package projet.ais.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Intrant {

    @Id
    private String idIntrant;

    @Column(nullable = false)
    private String nomIntant;

    @Column(nullable = false)
    private double quantiteIntrant;
    
    @Column(nullable = true)
    private String descriptionIntrant;
    
    @Column
    private boolean statutIntrant;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateAjout;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateModif;


    @Column(nullable=true)
    private String personneModif;

    @ManyToOne
    @JoinColumn(name = "idActeur")
    private Acteur acteur;
    
     


    
}
