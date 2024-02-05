package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.ais.models.MessageWa;

public interface MessageWaRepository extends JpaRepository<MessageWa , String> {
    
}
