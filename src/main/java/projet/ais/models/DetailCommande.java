package projet.ais.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data

public class DetailCommande {

    @Id
    private String idDetailCommande;

    @Column(nullable = false)
    private String codeProduit;
    
    @Column(nullable = false)
    private double quantiteDemande;
    
    @Column(nullable = false)
    private double quantiteLivree;
    
    @Column(nullable = false)
    private String codeStock;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateDetailCommande;

    

}
