package projet.ais.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Speculation {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idSpeculation;

    @Column(nullable = true)
    private String codeSpeculation;

    @Column(nullable = false)
    private String nomSpeculation;

    @Column(nullable = false)
    private String descriptionSpeculation;

    @Column(nullable = false)
    private boolean statutSpeculation = true;

    @ManyToOne
    @JoinColumn(name = "idCategorieProduit")
    private CategorieProduit categorieProduit;

    // @Column(columnDefinition = "TIMESTAMP",nullable = true)
    // private LocalDateTime dateAjout;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    @Column(nullable=true)
    private String personneModif; 
    
    @OneToMany
    (mappedBy = "speculation")
    @JsonIgnore
    private List<Stock> stockList;

    @ManyToOne
    @JsonIgnore
    @JoinColumn( name = "idActeur")
    private Acteur acteur;

    
    @OneToMany
    (mappedBy = "speculation")
    @JsonIgnore
    private List<Superficie> superficies;
}

