package projet.ais.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Unite {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idUnite;

    @Column(nullable = false)
    private String codeUnite;

    @Column(nullable = false)
    private String nomUnite;

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
    
    @Column(nullable = false)
    private boolean statutUnite = true;

    @Column(nullable=true)
    private String personneModif;
    
    @ManyToOne
    @JoinColumn( name = "idActeur")
    private Acteur acteur;
    
    @OneToMany
    (mappedBy = "unite")
    @JsonIgnore
    private List<Stock> stockList;
}

