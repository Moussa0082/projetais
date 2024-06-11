package projet.ais.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import projet.ais.models.Device;
import projet.ais.models.Monnaie;
import projet.ais.services.DeviceService;

@RestController
@RequestMapping("api-koumi/Device")
public class DeviceController {
    
    @Autowired
    DeviceService deviceService;

    @PostMapping("/create")
    @Operation(summary = "Création de la device")
    public ResponseEntity<Device> saveDevice(@RequestBody Device device) {
        return new ResponseEntity<>(deviceService.createDevice(device), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Modification de la device ")
    public ResponseEntity<Device> updateDevice(@RequestBody Device device, @PathVariable String id) {
        return new ResponseEntity<>(deviceService.updateDevice(device,id), HttpStatus.CREATED);
    }

    @PutMapping("/desactiver/{id}")
    @Operation(summary = "Desactivation")
    public ResponseEntity<Device> desactiveDevice(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(deviceService.desactive(id), HttpStatus.CREATED);
    }

    @PutMapping("/activer/{id}")
    @Operation(summary = "Activation")
    public ResponseEntity<Device> activeDevice(@PathVariable String id) throws Exception {
        return new ResponseEntity<>(deviceService.active(id), HttpStatus.CREATED);
    }

    @GetMapping("/getAllDevice")
    public ResponseEntity<List<Device>> listeDevice() {
        return new ResponseEntity<>(deviceService.getAllDevice(), HttpStatus.CREATED);
    }
    
    @GetMapping("/getDeviseByMonnaie/{id}")
    public ResponseEntity<List<Device>> listeDeviceByMonnaie(@PathVariable String id) {
        return new ResponseEntity<>(deviceService.getAllDeviceByMonnaie(id), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression")
    public String deleteDevice(@PathVariable String id){
        return deviceService.deleteDevice(id);
    }
}
