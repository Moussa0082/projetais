

package projet.ais.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.Intrant;
import projet.ais.models.Superficie;

public interface IntrantRepository extends JpaRepository<Intrant , String> {

    Intrant findByIdIntrant(String idIntrant);

    List<Intrant> findByIdIntrantIn(List<String> idIntrants);

    List<Intrant> findAllByActeurIdActeur(String idIntrant);
    // List<Intrant> findAllBySpeculationIdSpeculation(String idSpeculation);
    // List<Intrant> findAllBySpeculation_CategorieProduit_IdCategorieProduit(String idCategorieProduit);
    List<Intrant> findAllByCategorieProduit_IdCategorieProduit(String idCategorieProduit);
    Collection<Intrant> findByNomIntrant(String nomIntrant);

    Page<Intrant> findByCategorieProduit_IdCategorieProduitAndStatutIntrant(String idCategorieProduit, boolean statutIntrant,
            Pageable pageable);

    Page<Intrant> findByActeur_IdActeur(String idActeur, Pageable pageable);

    Page<Intrant> findAllByStatutIntrant(boolean statutIntrant, Pageable pageable);
}
