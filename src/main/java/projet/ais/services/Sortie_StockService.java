package projet.ais.services;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.models.CategorieProduit;
import projet.ais.models.Sortie_Stock;
import projet.ais.models.Speculation;
import projet.ais.models.Stock;
import projet.ais.models.TypeActeur;
import projet.ais.repository.Sortie_StockRepository;
import projet.ais.repository.StockRepository;

@Service
public class Sortie_StockService {


    @Autowired
    private Sortie_StockRepository sortie_StockRepository;

    @Autowired
    private StockRepository stockRepository;
 
    
      public Sortie_Stock createSortieStock(Sortie_Stock sortie_Stock) throws Exception{

        Stock stock = stockRepository.findByIdStock(sortie_Stock.getStock().getIdStock());
       
        if(stock == null){

            throw new EntityNotFoundException("Ce produit n'existe pas");
        }
        
        double quantiteStock = stock.getQuantiteStock();
        double quantiteSortie = sortie_Stock.getQuantiteSortie();

        if(quantiteSortie >quantiteStock){
     throw new Exception("La quantite de sortie ne peut pas être supérieur à la quantité \n de stock disponible");
        }

        if(quantiteStock == 0){
     throw new Exception("La quantite de " + stock.getNomProduit() +  " est non disponible");
        }
        if(quantiteSortie <= quantiteStock){
        double newQuantity = quantiteStock - quantiteSortie;
        sortie_Stock.setQuantiteSortie(newQuantity);
        }
               stock.setQuantiteStock(quantiteStock - quantiteSortie);
               stockRepository.save(stock);
              return sortie_StockRepository.save(sortie_Stock);
        
    }
     

     public List<Sortie_Stock> getAllSortieStocksByStockId(int idStock) {
        return sortie_StockRepository.findByStockIdStock(idStock);
    }

    // public List<Sortie_Stock> getAllSortieStocksByActeur(Stock stock, Integer idActeur) {
    //     return sortie_StockRepository.findByStockAndActeurIdActeur(stock, idActeur);
    // }

    public List<Sortie_Stock> getSortieStocksBetweenDates(Date startDate, Date endDate) {
        return sortie_StockRepository.findByDateSortieBetween(startDate, endDate);
    }

      //  Supprimer historique sortie stock
      public String deleteByIdSortieStock(Integer id){
        Sortie_Stock sortieStock = sortie_StockRepository.findByIdSortieStock(id);
        if(sortieStock == null){
            throw new EntityNotFoundException("Désolé le sortie de stock à supprimer n'existe pas");
        }
        sortie_StockRepository.delete(sortieStock);
        return "Sortie stock supprimé avec succèss";
    }


}
