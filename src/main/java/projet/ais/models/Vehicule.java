package projet.ais.models;

import java.util.*;


import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Vehicule {
    
   @Id
   private String idVehicule;

   @Column(nullable = false)
   private String nomVehicule;
   
   @Column(nullable = false)
   private String capaciteVehicule;

   @Column(nullable = true)
   private String codeVehicule;

   @Convert(converter = MapToJsonConverter.class)
   @Column(columnDefinition = "json")
   private Map<String, Integer> prixParDestination;

   @Column
   private boolean statutVehicule;
   
   @Column(nullable = true)
   private String photoVehicule;
   
   @Column(nullable = false)
   private String localisation;

   @Column(nullable = true)
   private String dateAjout;

   @Column(nullable = true)
   private String dateModif;
   
   @Column(nullable = false)
   private String etatVehicule;

   @Column(nullable=true)
   private String personneModif;

   @ManyToOne
   @JoinColumn(name = "idActeur")
   private Acteur acteur;

   @ManyToOne
   @JoinColumn(name = "idTypeVoiture")
   private TypeVoiture typeVoiture;

}
