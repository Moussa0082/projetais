package projet.ais.models;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Niveau3Pays {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idNiveau3Pays;

    @Column(nullable = false)
    private String codeN3;

    @Column(nullable = false)
    private String nomN3;

    @Column(nullable = false)
    private String descriptionN3;

    @Column(nullable = false)
    private String statutN3;

    @ManyToOne
    @JoinColumn( name = "idNiveau2Pays") 
    private  Niveau2Pays niveau2Pays;
}

