package projet.ais.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class Unite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUnite;

    @Column(nullable = false)
    private String codeUnite;

    @Column(nullable = false)
    private String nomUnite;

     @Column(nullable=false)
    private Date dateAjout;

    @Column(nullable=false)
    private Date dateModif;
    
    @OneToMany
    (mappedBy = "unite")
    @JsonIgnore
    private List<Stock> stockList;
}

