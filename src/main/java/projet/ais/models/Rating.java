package projet.ais.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;


@Data
@Entity
public class Rating {

    @Id
    private String idRating;

    @Column(nullable = true)
    private int stars;

    @ManyToOne
    @JoinColumn(name = "idActeur")
    private Acteur acteur;

    @ManyToOne
    @JoinColumn(name = "idStock")
    private Stock stock;
    
}
