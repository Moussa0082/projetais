package projet.ais.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.MessageWa;

public interface MessageWaRepository extends JpaRepository<MessageWa , String> {
    
     List<MessageWa> findByActeurIdActeur(String idActeur);
     
}
