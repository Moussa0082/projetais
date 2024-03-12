package projet.ais.models;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.*;


import java.util.*;


import java.time.LocalDateTime;



@Entity
@Getter
@Setter
// @AllArgsConstructor
public class Acteur {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idActeur;

    @Column(name = "reset_token", nullable = true)
	private String resetToken;

    @Column(columnDefinition = "TIMESTAMP", nullable = true)
    private LocalDateTime tokenCreationDate;

    @Column(nullable = true)
    private String codeActeur;

    @Column(nullable = false)
    private String nomActeur;

    @Column(nullable = false)
    private String adresseActeur;

    @Column(nullable = false)
    private String telephoneActeur;

    @Column(nullable = true)
    private String whatsAppActeur;

    @Column(nullable = true)
    private String latitude;

    @Column(nullable = true)
    private String longitude;

    @Column(nullable = true)
    private String photoSiegeActeur;

    @Column(nullable = true)
    private String logoActeur;

    @Column(nullable = false)
    private String niveau3PaysActeur;

    
    @Column(nullable = false)
    private String password;


    @Column(nullable = true)
    private String dateAjout;

    @Column(nullable = true)
    private String dateModif;


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

   
    private Boolean statutActeur  ;

    @OneToMany
    (mappedBy = "acteur")
    @JsonIgnore
    private List<Stock> stockList;

    @OneToMany(mappedBy = "acteur")
    @JsonIgnore
    private List<Unite> unite;

    @OneToMany
    (mappedBy = "acteur")
    @JsonIgnore
    private List<Magasin> magasinList;

    @OneToMany
    (mappedBy = "acteur")
    @JsonIgnore
    private List<TypeVoiture> typeVoitures;

    @OneToMany(mappedBy = "acteur")
    @JsonManagedReference
    @JsonIgnore
    private List<Materiel> materiels;

    @OneToMany(mappedBy = "acteur")
    @JsonIgnore
    private List<MessageWa> messageList;

    @OneToMany
    (mappedBy = "acteur")
    @JsonIgnore
    private List<CategorieProduit> categorieProduits;

    @OneToMany
    (mappedBy = "acteur")
    @JsonIgnore
    private List<Filiere> filieresList;

    @OneToMany
    (mappedBy = "acteur")
    @JsonIgnore
    private List<ZoneProduction> zoneProductions;

    @OneToMany
    (mappedBy = "acteur")
    @JsonIgnore
    private List<Speculation> speculationsList;

  
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "acteur_type_acteur",
        joinColumns = @JoinColumn(name = "id_acteur"),
        inverseJoinColumns = @JoinColumn(name = "id_type_acteur"))
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


   @OneToMany
   (mappedBy = "acteur")
   @JsonIgnore
   private List<Superficie> superficieList;

   @OneToMany
   (mappedBy = "acteur")
   @JsonIgnore
   private List<Campagne> campagnes;
}

