package projet.ais.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import projet.ais.models.DetailCommande;
import projet.ais.models.Intrant;
import projet.ais.models.Magasin;
import projet.ais.models.Stock;
import projet.ais.repository.CommandeRepository;
import projet.ais.repository.DetailCommandeRepository;
import projet.ais.repository.IntrantRepository;
import projet.ais.repository.StockRepository;

@Service
public class DetailCommandeService {


    @Autowired
    DetailCommandeRepository detailCommandeRepository;

    @Autowired
    CommandeRepository commandeRepository;
    @Autowired
    IntrantRepository intrantRepository;
    @Autowired
    StockRepository stockRepository;


     public List<DetailCommande> getAllDetailCommandeByIdCommande(String idCommande) {
        List<DetailCommande> detailCommandeList = detailCommandeRepository.findByCommandeIdCommande(idCommande);

        if(detailCommandeList.isEmpty())
            throw new IllegalStateException("Aucun detail trouvé");
        
            detailCommandeList = detailCommandeList.
        stream().sorted((m1,m2) -> m2.getDateAjout().compareTo(m1.getDateAjout()))
        .collect(Collectors.toList());

        return detailCommandeList;
    }



    @Transactional
    public void updateDetailCommandes() {
        List<DetailCommande> detailCommandes = detailCommandeRepository.findAll();

        for (DetailCommande detailCommande : detailCommandes) {
            String nomProduit = detailCommande.getNomProduit();
            
            if (nomProduit != null) {
                Optional<Intrant> optionalIntrant = intrantRepository.findByNomIntrant(nomProduit);
                Optional<Stock> optionalStock = stockRepository.findByNomProduit(nomProduit);
                if (optionalStock.isPresent()) {
                    Stock stock = optionalStock.get();
                    detailCommande.setStock(stock);
                    detailCommande.setIsStock(true);
                } else {
                    if (optionalIntrant.isPresent()) {
                        Intrant intrant = optionalIntrant.get();
                        detailCommande.setIntrant(intrant);
                        detailCommande.setIsStock(false);
                    } else {
                        // If neither stock nor intrant found, set isStock to null
                        detailCommande.setIsStock(null);
                    }
                }

                detailCommandeRepository.save(detailCommande);
            }
        }
}


   public void updateDetailStatut(){
    List<DetailCommande> details = detailCommandeRepository.findAll();

     for(DetailCommande detail : details){
        double quantiteDemande = detail.getQuantiteDemande();
        double quantiteLivree = detail.getQuantiteLivree();

        // Utilisation d'une tolérance pour la comparaison des doubles
        if (Math.abs(quantiteDemande - quantiteLivree) < 0.0001) {
            detail.setIsDelivered(true);
            System.out.println("id true : " + detail.getIdDetailCommande());
        } else {
            detail.setIsDelivered(false);
            System.out.println("id false : " + detail.getIdDetailCommande());
        }
    }
   }

}