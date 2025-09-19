package projet.ais.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

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

    @Column(nullable = false)
    private boolean hasAssociation = false;

    @ManyToOne
    @JoinColumn( name = "idActeur")
    private Acteur acteur;

    @OneToMany
    (mappedBy = "unite" , cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Stock> stockList;

}

