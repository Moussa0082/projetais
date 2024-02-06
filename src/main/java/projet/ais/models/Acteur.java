package projet.ais.models;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.util.*;

import java.time.LocalDateTime;



@Entity
@Data
public class Acteur {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idActeur;

    @Column(name = "reset_token", nullable = true)
	private String resetToken;

    @Column(columnDefinition = "TIMESTAMP", nullable = true)
    private LocalDateTime tokenCreationDate;

    @Column(nullable = false)
    private String codeActeur;

    @Column(nullable = false)
    private String nomActeur;

    @Column(nullable = false)
    private String adresseActeur;

    @Column(nullable = false)
    private String telephoneActeur;

    @Column(nullable = true)
    private String whatsAppActeur;

    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String longitude;

    @Column(nullable = true)
    private String photoSiegeActeur;

    @Column(nullable = true)
    private String logoActeur;

    @Column(nullable = false)
    private String niveau3PaysActeur;
    
    @Column(nullable = false)
    private String password;


    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateAjout;

    @PrePersist
    public void prePersist() {
        dateAjout = LocalDateTime.now();
    }


    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateModif;

    public LocalDateTime updateDateModif(LocalDateTime dateModif) {
        this.dateModif = dateModif;
        return dateModif;
    }


    @Column(nullable=true)
    private String personneModif;

    @Column(nullable = false)
    private String localiteActeur;

    @Column(nullable = false)
    private String emailActeur;

    @Column(nullable = false)
    private String MaillonActeur;

    @Column(nullable = false)
    private String filiereActeur;

    @Column(nullable = true)
    private boolean statutActeur ;

    @OneToMany
    (mappedBy = "acteur")
    @JsonIgnore
    private List<Stock> stockList;

    @OneToMany
    (mappedBy = "acteur")
    @JsonIgnore
    private List<Magasin> magasinList;

    @ManyToMany
    // @JsonIgnore
    (mappedBy = "acteur")
    private List<TypeActeur> typeActeur;

   @OneToMany
   (mappedBy = "acteur")
   @JsonIgnore
   private List<Alerte> alerteList;

   @OneToMany
   (mappedBy = "acteur")
   @JsonIgnore
   private List<Intrant> intrantList;

   @OneToMany
   (mappedBy = "acteur")
   @JsonIgnore
   private List<Conseil> conseilList;

   @OneToMany
   (mappedBy = "acteur")
   @JsonIgnore
   private List<Vehicule> vehiculeList;
   

   @OneToMany
   (mappedBy = "acteur")
   @JsonIgnore
   private List<Commande> commandeList;

   @OneToOne(mappedBy = "acteur")
    private CommandeMateriel commandeMateriel;


   @OneToMany
   (mappedBy = "acteur")
   @JsonIgnore
   private List<Superficie> superficieList;
}

