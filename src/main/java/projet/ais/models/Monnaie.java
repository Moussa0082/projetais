package projet.ais.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Monnaie {
    
    @Id
    private String idMonnaie;

    @Column(nullable = false)
    private String codeMonnaie;

    @Column(nullable = false)
    private String libelle;

    @Column(nullable = false)
    private String sigle;

    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;

    @Column(nullable = false)
    private boolean statut = true;


    @OneToMany(mappedBy = "monnaie")
    @JsonIgnore
    private List<Device> deviceList;

    @OneToMany(mappedBy = "monnaie")
    @JsonIgnore
    private List<Intrant> intrantsList;
    
    @OneToMany(mappedBy = "monnaie")
    @JsonIgnore
    private List<Stock> stocksList;
    
    @OneToMany(mappedBy = "monnaie")
    @JsonIgnore
    private List<Materiel> materielList;

    @OneToMany(mappedBy = "monnaie")
    @JsonIgnore
    private List<Vehicule> vehiculeList;
}
