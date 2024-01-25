package projet.ais.models;


import jakarta.persistence.*;
import lombok.Data;



@Entity
@Data
public class Alerte {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
     private int id;

    @Column(nullable = true)
    private String sujet;

    @Column(nullable = false)
    private String email;

    @Column(length = 2000, nullable = false)
    private String message;

    public Alerte(String mailur, String message, String sujet){
  
    }

  public Alerte(String email, String message){

  }


}
