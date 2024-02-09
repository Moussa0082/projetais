package projet.ais.models;

import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Data;

@Entity
@Data
public class Intrant {

    @Id
    private String idIntrant;

    @Column(nullable = false)
    private String nomIntrant;

    @Column(nullable = false)
    private double quantiteIntrant;
    
    @Column(nullable = false)
    private String condeIntrant;

    
    @Column(nullable = true)
    private String descriptionIntrant;

    @Column(nullable = true)
    private String photoIntrant;
    
    @Column
    private boolean statutIntrant;

  @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateAjout;

    @PrePersist
    public void prePersist() {
        dateAjout = LocalDateTime.now();
    }

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateModif;

    public LocalDateTime updateDateModif(LocalDateTime dateModif) {
        this.dateModif = dateModif;
        return dateModif;
    }

    @Column(nullable=true)
    private String personneModif;

    @ManyToOne
    @JoinColumn(name = "idActeur")
    private Acteur acteur;
    
    @ManyToOne
    @JoinColumn(name = "idSuperficie")
    private Superficie superficie;

    
}
