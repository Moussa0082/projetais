package projet.ais.models;

import java.util.List;

import lombok.Data;

@Data
public class CommandeAvecStocks {
    private  Commande commande;
    private List<Stock> stocks;
    // private List<DetailCommande> detailsCommande;
    private List<Double> quantitesDemandees;
    
    // private List<Intrant> intrants;
    // private List<Double> quantitesStocks;
    // private List<Double> quantitesIntrants;

    // Constructeur, getters, setters
}
