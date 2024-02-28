package projet.ais.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class MessageWa {
    
    @Id
    private  String idMessage;

    @Column(nullable = false)
    private String codeMessage;
    
    @Column(nullable = true)
    private String personneModif;


    @Column(nullable = false)
    private String text;

    @Column(nullable = true)
    private String dateAjout;

    // @Column(nullable = false)
    // private String produitConcerner;
 
    @Column(nullable = false)
    private String ActeurConcerner;
    
}
