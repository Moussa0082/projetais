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
public class Vehicule {
    
   @Id
   private String idVehicule;

   @Column(nullable = false)
   private String nomVehicule;
   
   @Column(nullable = false)
   private String capaciteVehicule;

   @Column
   private boolean statutVehicule;
   
   @Column(nullable = false)
   private String photoVehicule;
   
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

}
