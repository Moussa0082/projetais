package projet.ais.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import projet.ais.repository.AbonnementRepository;

import projet.ais.models.Abonnement;

@Service
public class AbonnementScheduler {

    @Autowired
    private AbonnementRepository abonnementRepository;

   
    @Scheduled(cron = "0 0 0 * * ?") // Exécution tous les jours à 00h00
    @Transactional
    public void verifierTousLesAbonnements() {
        // Récupérer tous les abonnements de la base de données
        List<Abonnement> abonnements = abonnementRepository.findAll();

        for (Abonnement abonnement : abonnements) {
            verifierStatutAbonnement(abonnement);
            // Sauvegarder les modifications apportées à l'abonnement
            abonnementRepository.save(abonnement);
        }
    }

    private void verifierStatutAbonnement(Abonnement abonnement) {
        if (abonnement.getDateFin() != null) {
            // Si la date actuelle est après la date de fin, désactiver l'abonnement
            if (LocalDate.now().isAfter(abonnement.getDateFin())) {
                abonnement.setStatutAbonnement(false);
            } else {
                abonnement.setStatutAbonnement(true); // Activer si la date n'est pas dépassée
            }
        }
    }
}
