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

    @Column(nullable = false)
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
    private String personneAjout;
    
    @OneToMany
    (mappedBy = "speculation")
    @JsonIgnore
    private List<Stock> stockList;

    @ManyToOne
    @JoinColumn( name = "idActeur")
    private Acteur acteur;
    
    @OneToMany
    (mappedBy = "speculation")
    @JsonIgnore
    private List<Superficie> superficies;
}

