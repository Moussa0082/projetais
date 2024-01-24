package projet.ais.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Speculation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSpeculation;

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

    @OneToMany
    (mappedBy = "speculation")
    @JsonIgnore
    private List<Stock> stockList;
}

