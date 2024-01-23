package projet.ais.models;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Filiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idFiliere;

    @Column(nullable = false)
    private String codeFiliere;

    @Column(nullable = false)
    private String libelleFiliere;

    @Column(nullable = false)
    private String descriptionFiliere;

    @Column(nullable = false)
    private boolean statutFiliere = true;

    @OneToMany
    (mappedBy = "filiere")
    @JsonIgnore
    private List<CategorieProduit> categorieProduitList;
}

