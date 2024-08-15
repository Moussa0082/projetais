package projet.ais.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Historique {
    @Id
    private String idHistorique;

    @Column(nullable = false)
    private String dateHistorique;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String libelle;

    @Column(nullable = false)
    private String acteur;

    @Column(nullable = false)
    private String localite;

    @Column(nullable = false)
    private String pays;

    @Column(nullable = false)
    private String detail;
}
