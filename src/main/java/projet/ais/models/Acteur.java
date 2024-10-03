package projet.ais.models;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;



@Entity
@Getter
@Setter
// @Data
// @AllArgsConstructor
public class Acteur {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idActeur;

    @Column(name = "reset_token", nullable = true)
	private String resetToken;


    @Column(nullable = true)
    private String tokenCreationDate;

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

    @Column(nullable = true)
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

    @Column(nullable = true)
    private String emailActeur;

    private Boolean statutActeur = false ;

    private Boolean isConnected;

    @ManyToOne
    @JoinColumn(name = "idPays")
    private Pays pays;

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
    private List<Materiels> materiels;

    @OneToMany(mappedBy = "acteur")
    @JsonIgnore
    private List<MessageWa> messageList;

    // @OneToMany
    // (mappedBy = "acteur")
    // @JsonIgnore
    // private List<CategorieProduit> categorieProduits;

    // @OneToMany
    // (mappedBy = "acteur")
    // @JsonIgnore
    // private List<Filiere> filieresList;

    @OneToMany(mappedBy = "acteur")
    @JsonIgnore
    private List<ZoneProduction> zoneProductions;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "acteur_speculation",
        joinColumns = @JoinColumn(name = "id_acteur"),
        inverseJoinColumns = @JoinColumn(name = "id_speculation"))
    private List<Speculation> speculation;
  
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
   

   
   @OneToMany(mappedBy = "acteur")
   private List<Rating> ratings;

}

