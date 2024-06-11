package projet.ais.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projet.ais.models.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device,String> {
    
}
