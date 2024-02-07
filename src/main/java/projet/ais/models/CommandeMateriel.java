package projet.ais.models;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Data;

@Entity
@Data
public class CommandeMateriel {
    
    @Id
    private String idCommandeMateriel;

    @Column(nullable = false)
    private String proprietaire;

    @Column(nullable = false)
    private String codeCommande;

    @Column
    private boolean statutCommandeLivrer = false;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateCommande;

    @PrePersist
    public void prePersist() {
        dateCommande = LocalDateTime.now();
    }
    
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "idActeur")
    private Acteur acteur;

    @ManyToMany
    private List<Materiel> materielList;
    
}
