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

    @Column(nullable = true)
    private String codeUnite;

    @Column(nullable = true)
    private String nomUnite;

    @Column(nullable = true)
    private String sigleUnite;

    @Column(nullable = true,columnDefinition = "TEXT")
    private String description;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;
    
    @Column(nullable = false)
    private boolean statutUnite = true;

    @Column(nullable=true)
    private String personneModif;
    
    @ManyToOne
    @JoinColumn( name = "idActeur")
    private Acteur acteur;
    
    @OneToMany
    (mappedBy = "unite" , cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Stock> stockList;
}

