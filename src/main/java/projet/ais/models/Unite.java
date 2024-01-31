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

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateModif;
    
    @Column(nullable = false)
    private boolean statutUnite = true;

    @Column(nullable=true)
    private String personneAjout;
    
    @OneToMany
    (mappedBy = "unite")
    @JsonIgnore
    private List<Stock> stockList;
}

