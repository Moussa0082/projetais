package projet.ais.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import projet.ais.models.MessageWa;
import projet.ais.models.WhatsApp;


public interface  WhatsAppRepository extends CrudRepository<WhatsApp, Long> {
}
