package projet.ais.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;
import projet.ais.models.Device;
import projet.ais.models.Monnaie;
import projet.ais.models.Niveau1Pays;
import projet.ais.repository.DeviceRepository;

@Service
public class DeviceService {
    
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    IdGenerator idGenerator;
    @Autowired
    CodeGenerator codeGenerator;

    
    public Device createDevice(Device device){
        
        String codes = codeGenerator.genererCode();
        String idcodes = idGenerator.genererCode();
        device.setIdDevice(idcodes);
        device.setCodeDevice(codes);

        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);  
        device.setDateAjout(formattedDateTime);
        return deviceRepository.save(device);
    }

    public Device updateDevice(Device device,String id){
        
        Device d = deviceRepository.findById(id).orElseThrow(null);

        d.setNomDevice(device.getNomDevice());
        d.setSigle(device.getSigle());
        d.setTaux(device.getTaux());

        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);  
        d.setDateModif(formattedDateTime);
        return deviceRepository.save(d);
    }

    public String deleteDevice(String id){
        Device device = deviceRepository.findById(id).orElseThrow(null);
        deviceRepository.delete(device);
        return "Supprimé avec success";
    }

    public List<Device> getAllDevice(){
        List<Device> devices = deviceRepository.findAll();

        if(devices.isEmpty())
            throw new EntityNotFoundException("Liste vide");

            devices = devices
        .stream().sorted((u1,u2) -> u2.getNomDevice().compareTo(u1.getNomDevice()))
        .collect(Collectors.toList());

        return devices;
    }

    public List<Device> getAllDeviceByMonnaie(String id){
        List<Device> devices = deviceRepository.findByMonnaie_idMonnaie(id);

        if(devices.isEmpty())
            throw new EntityNotFoundException("Liste vide");

            devices = devices
        .stream().sorted((u1,u2) -> u2.getNomDevice().compareTo(u1.getNomDevice()))
        .collect(Collectors.toList());

        return devices;
    }


    public Device active(String id) throws Exception{
        Device device = deviceRepository.findById(id).orElseThrow(null);

        try {
            device.setStatut(true);
        } catch (Exception e) {
            throw new Exception("Erreur lors de l'activation : " + e.getMessage());
        }
        return deviceRepository.save(device);
    }

    public Device desactive(String id) throws Exception{
        Device device = deviceRepository.findById(id).orElseThrow(null);

        try {
            device.setStatut(false);
        } catch (Exception e) {
            throw new Exception("Erreur lors de la desactivation : " + e.getMessage());
        }
        return deviceRepository.save(device);
    }
}
