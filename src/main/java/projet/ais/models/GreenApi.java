package projet.ais.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class GreenApi {
    
    @Id
    private  String id;

    @Column(nullable = false)
    private String text;
    
}
