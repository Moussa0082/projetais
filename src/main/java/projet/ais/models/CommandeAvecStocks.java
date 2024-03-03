package projet.ais.models;

import java.util.List;

import lombok.Data;

@Data
public class CommandeAvecStocks {
    private Commande commande;
    private List<Stock> stocks;

    // Constructeur, getters, setters
}
