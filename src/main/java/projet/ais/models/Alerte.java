package projet.ais.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;



@Entity
@Data
public class Alerte {

    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
     private String id;

    @Column(nullable = true)
    private String sujet;

    @Column(nullable = true)
    private String email;

    @Column(length = 2000, nullable = false)
    private String message;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateAjout;

    @ManyToOne
    private Acteur acteur;

    // Constructeur par défaut
    public Alerte() {
    }

    public Alerte(String email, String message, String sujet){
     this.email = email;
     this.message = message;
     this.sujet = sujet;
    }

    public Alerte(Acteur acteur, String message, String sujet){
     this.acteur = acteur;
     this.message = message;
     this.sujet = sujet;
    }

    public Alerte(String email, String message, String sujet, Acteur acteur){
      this.email = email;
      this.message = message;
      this.sujet = sujet;
      this.acteur = acteur;
    }

  public Alerte(String email, String message){

    this.email = email;
    this.message = message;

  }




}
