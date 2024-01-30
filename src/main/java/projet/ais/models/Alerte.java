package projet.ais.models;

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

    @Column(nullable = false)
    private String email;

    @Column(length = 2000, nullable = false)
    private String message;

    // Constructeur par défaut
    public Alerte() {
    }

    public Alerte(String email, String message, String sujet){
     this.email = email;
     this.message = message;
     this.sujet = sujet;
    }

  public Alerte(String email, String message){

    this.email = email;
    this.message = message;

  }


}
