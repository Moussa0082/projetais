package projet.ais.models;

import java.util.*;

import lombok.Data;

@Data
public class CommandeAvecStocksDTO {
    
    private Commande commande;
    private List<Stock> stock;
}
