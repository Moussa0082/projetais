package projet.ais.repository;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.Commentaire;

@Repository
public interface CommentRepo extends JpaRepository<Commentaire,Long> {

    // Page<Commentaire> findCommentaire(Pageable pageable);
}
 