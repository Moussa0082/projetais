package projet.ais.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

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

    @OneToMany
    (mappedBy = "unite")
    @JsonIgnore
    private List<Stock> stockList;
}

