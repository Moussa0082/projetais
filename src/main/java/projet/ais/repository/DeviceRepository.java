package projet.ais.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device,String> {
    
    List<Device> findByMonnaie_idMonnaie(String id);
}
