package projet.ais.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projet.ais.models.DetailCommande;
import projet.ais.models.Magasin;
import projet.ais.repository.CommandeRepository;
import projet.ais.repository.DetailCommandeRepository;

@Service
public class DetailCommandeService {


    @Autowired
    DetailCommandeRepository detailCommandeRepository;

    @Autowired
    CommandeRepository commandeRepository;


     public List<DetailCommande> getAllDetailCommandeByIdCommande(String idCommande) {
        List<DetailCommande> detailCommandeList = detailCommandeRepository.findByCommandeIdCommande(idCommande);

        if(detailCommandeList.isEmpty())
            throw new IllegalStateException("Aucun detail trouvé");
        
            detailCommandeList = detailCommandeList.
        stream().sorted((m1,m2) -> m2.getDateAjout().compareTo(m1.getDateAjout()))
        .collect(Collectors.toList());

        return detailCommandeList;
    }
    
}
